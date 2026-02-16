package com.vladan.holycodetask.feature.details.data.repository

import app.cash.turbine.test
import com.vladan.holycodetask.core.common.AppError
import com.vladan.holycodetask.core.common.Resource
import com.vladan.holycodetask.core.database.entity.VenueEntity
import com.vladan.holycodetask.core.network.dto.PlaceDetailsDto
import com.vladan.holycodetask.feature.details.data.local.DetailsLocalDataSource
import com.vladan.holycodetask.feature.details.data.remote.DetailsRemoteDataSource
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.every
import io.mockk.mockk
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test
import java.io.IOException

class DetailsRepositoryImplTest {

    private lateinit var remoteDataSource: DetailsRemoteDataSource
    private lateinit var localDataSource: DetailsLocalDataSource
    private lateinit var repository: DetailsRepositoryImpl

    private lateinit var dbFlow: MutableSharedFlow<VenueEntity?>

    private val fsqId = "abc123"

    @Before
    fun setUp() {
        remoteDataSource = mockk()
        localDataSource = mockk(relaxUnitFun = true)
        dbFlow = MutableSharedFlow()
        every { localDataSource.observeVenueById(fsqId) } returns dbFlow
        repository = DetailsRepositoryImpl(remoteDataSource, localDataSource)
    }

    private fun createEntity(
        fsqId: String = this.fsqId,
        name: String = "Test Venue",
        distance: Int? = 150,
    ) = VenueEntity(
        fsqId = fsqId,
        name = name,
        categoryName = "Cafe",
        categoryIconUrl = "https://example.com/icon.png",
        address = "123 Main St",
        formattedAddress = "123 Main St, Belgrade",
        locality = "Belgrade",
        distance = distance,
        latitude = 44.8176,
        longitude = 20.4633,
        description = "A nice cafe",
        tel = "+381111234567",
        website = "https://example.com",
        hoursDisplay = "Mon-Fri 8:00-20:00",
        openNow = true,
        photosJson = null,
        socialFacebookId = null,
        socialInstagram = "testcafe",
        socialTwitter = "testcafe",
    )

    private fun createDto(
        fsqId: String = this.fsqId,
        name: String = "Test Venue Updated",
    ) = PlaceDetailsDto(
        fsqPlaceId = fsqId,
        name = name,
        latitude = 44.8176,
        longitude = 20.4633,
        description = "A nice cafe updated",
        tel = "+381111234567",
        website = "https://example.com",
    )

    @Test
    fun `emits Loading first`() = runTest {
        // Given
        coEvery { remoteDataSource.getPlaceDetails(fsqId) } coAnswers {
            kotlinx.coroutines.awaitCancellation()
        }

        // When & Then
        repository.getVenueDetails(fsqId).test {
            assertTrue(awaitItem() is Resource.Loading)
            cancelAndIgnoreRemainingEvents()
        }
    }

    @Test
    fun `emits cached data from DB observation`() = runTest {
        // Given
        val cached = createEntity()
        coEvery { remoteDataSource.getPlaceDetails(fsqId) } coAnswers {
            kotlinx.coroutines.awaitCancellation()
        }

        // When & Then
        repository.getVenueDetails(fsqId).test {
            assertTrue(awaitItem() is Resource.Loading)

            dbFlow.emit(cached)

            val success = awaitItem()
            assertTrue(success is Resource.Success)
            assertEquals(cached.name, (success as Resource.Success).data.name)

            cancelAndIgnoreRemainingEvents()
        }
    }

    @Test
    fun `emits fresh data after network updates DB`() = runTest {
        // Given
        val cached = createEntity(name = "Old Name")
        val fresh = createEntity(name = "New Name", distance = 150)
        coEvery { localDataSource.getVenueById(fsqId) } returns cached
        coEvery { remoteDataSource.getPlaceDetails(fsqId) } returns createDto(name = "New Name")

        // When & Then
        repository.getVenueDetails(fsqId).test {
            assertTrue(awaitItem() is Resource.Loading)

            dbFlow.emit(cached)
            val first = awaitItem()
            assertTrue(first is Resource.Success)
            assertEquals("Old Name", (first as Resource.Success).data.name)

            dbFlow.emit(fresh)
            val second = awaitItem()
            assertTrue(second is Resource.Success)
            assertEquals("New Name", (second as Resource.Success).data.name)

            cancelAndIgnoreRemainingEvents()
        }
    }

    @Test
    fun `network preserves distance from cached entity`() = runTest {
        // Given
        val cached = createEntity(distance = 250)
        coEvery { localDataSource.getVenueById(fsqId) } returns cached
        coEvery { remoteDataSource.getPlaceDetails(fsqId) } returns createDto()

        // When
        repository.getVenueDetails(fsqId).test {
            assertTrue(awaitItem() is Resource.Loading)
            dbFlow.emit(cached)
            awaitItem()

            // Then
            coVerify {
                localDataSource.cacheVenue(match { it.distance == 250 })
            }

            cancelAndIgnoreRemainingEvents()
        }
    }

    @Test
    fun `emits Error when network fails and no cache exists`() = runTest {
        // Given
        coEvery { remoteDataSource.getPlaceDetails(fsqId) } throws IOException("No network")

        // When & Then
        repository.getVenueDetails(fsqId).test {
            assertTrue(awaitItem() is Resource.Loading)

            val error = awaitItem()
            assertTrue(error is Resource.Error)
            assertEquals(AppError.NetworkError, (error as Resource.Error).error)

            cancelAndIgnoreRemainingEvents()
        }
    }

    @Test
    fun `emits Error when network fails even when cache exists`() = runTest {
        // Given
        val cached = createEntity()
        coEvery { remoteDataSource.getPlaceDetails(fsqId) } throws IOException("No network")

        // When & Then
        repository.getVenueDetails(fsqId).test {
            assertTrue(awaitItem() is Resource.Loading)

            val error = awaitItem()
            assertTrue(error is Resource.Error)
            assertEquals(AppError.NetworkError, (error as Resource.Error).error)

            dbFlow.emit(cached)
            val success = awaitItem()
            assertTrue(success is Resource.Success)
            assertEquals(cached.name, (success as Resource.Success).data.name)

            cancelAndIgnoreRemainingEvents()
        }
    }

    @Test
    fun `filters null DB emissions`() = runTest {
        // Given
        coEvery { remoteDataSource.getPlaceDetails(fsqId) } coAnswers {
            kotlinx.coroutines.awaitCancellation()
        }

        // When & Then
        repository.getVenueDetails(fsqId).test {
            assertTrue(awaitItem() is Resource.Loading)

            dbFlow.emit(null)
            expectNoEvents()

            val entity = createEntity()
            dbFlow.emit(entity)
            val success = awaitItem()
            assertTrue(success is Resource.Success)

            cancelAndIgnoreRemainingEvents()
        }
    }
}
