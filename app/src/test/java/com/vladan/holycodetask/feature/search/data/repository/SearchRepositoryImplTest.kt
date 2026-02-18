package com.vladan.holycodetask.feature.search.data.repository

import app.cash.turbine.test
import com.vladan.holycodetask.core.common.AppError
import com.vladan.holycodetask.core.common.Resource
import com.vladan.holycodetask.core.database.entity.VenueEntity
import com.vladan.holycodetask.core.network.dto.PlaceDto
import com.vladan.holycodetask.feature.search.data.local.SearchLocalDataSource
import com.vladan.holycodetask.feature.search.data.remote.SearchRemoteDataSource
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.every
import io.mockk.mockk
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.runTest
import okhttp3.ResponseBody.Companion.toResponseBody
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test
import retrofit2.HttpException
import retrofit2.Response
import java.io.IOException

class SearchRepositoryImplTest {

    private lateinit var remoteDataSource: SearchRemoteDataSource
    private lateinit var localDataSource: SearchLocalDataSource
    private lateinit var repository: SearchRepositoryImpl

    private val query = "coffee"
    private val latLng = "44.787197,20.457273"

    @Before
    fun setUp() {
        remoteDataSource = mockk()
        localDataSource = mockk(relaxUnitFun = true)
        repository = SearchRepositoryImpl(remoteDataSource, localDataSource)
    }

    private fun createEntity(
        fsqId: String = "1",
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
        description = null,
        tel = null,
        website = null,
        hoursDisplay = null,
        openNow = null,
        photosJson = null,
        socialFacebookId = null,
        socialInstagram = null,
        socialTwitter = null,
    )

    private fun createDto(
        fsqId: String = "1",
        name: String = "Test Venue",
        distance: Int? = 150,
    ) = PlaceDto(
        fsqPlaceId = fsqId,
        name = name,
        distance = distance,
    )

    // region Success path
    @Test
    fun `emits Loading first`() = runTest {
        // Given
        coEvery { remoteDataSource.searchPlaces(query, latLng) } returns emptyList()
        every { localDataSource.observeSearchResults(query) } returns MutableSharedFlow()

        // When & Then
        repository.searchVenues(query, latLng).test {
            assertTrue(awaitItem() is Resource.Loading)
            cancelAndIgnoreRemainingEvents()
        }
    }

    @Test
    fun `emits Success with venues from DB after network succeeds`() = runTest {
        // Given
        val dtos = listOf(createDto("1", "Cafe A"), createDto("2", "Cafe B"))
        val entities = listOf(
            createEntity("1", "Cafe A"),
            createEntity("2", "Cafe B"),
        )
        coEvery { remoteDataSource.searchPlaces(query, latLng) } returns dtos
        every { localDataSource.observeSearchResults(query) } returns flowOf(entities)

        // When & Then
        repository.searchVenues(query, latLng).test {
            assertTrue(awaitItem() is Resource.Loading)

            val success = awaitItem()
            assertTrue(success is Resource.Success)
            val venues = (success as Resource.Success).data
            assertEquals(2, venues.size)
            assertEquals("Cafe A", venues[0].name)
            assertEquals("Cafe B", venues[1].name)

            awaitComplete()
        }
    }

    @Test
    fun `caches network results to local data source`() = runTest {
        // Given
        val dtos = listOf(createDto("1", "Cafe A"))
        coEvery { remoteDataSource.searchPlaces(query, latLng) } returns dtos
        every { localDataSource.observeSearchResults(query) } returns flowOf(emptyList())

        // When
        repository.searchVenues(query, latLng).test {
            cancelAndConsumeRemainingEvents()
        }

        // Then
        coVerify { localDataSource.cacheSearchResults(query, any()) }
    }

    @Test
    fun `emits Success with empty list when no venues found`() = runTest {
        // Given
        coEvery { remoteDataSource.searchPlaces(query, latLng) } returns emptyList()
        every { localDataSource.observeSearchResults(query) } returns flowOf(emptyList())

        // When & Then
        repository.searchVenues(query, latLng).test {
            assertTrue(awaitItem() is Resource.Loading)

            val success = awaitItem()
            assertTrue(success is Resource.Success)
            assertTrue((success as Resource.Success).data.isEmpty())

            awaitComplete()
        }
    }

    @Test
    fun `re-emits when DB observation emits updated data`() = runTest {
        // Given
        val dtos = listOf(createDto("1", "Cafe A"))
        val dbFlow = MutableSharedFlow<List<VenueEntity>>()
        coEvery { remoteDataSource.searchPlaces(query, latLng) } returns dtos
        every { localDataSource.observeSearchResults(query) } returns dbFlow

        // When & Then
        repository.searchVenues(query, latLng).test {
            assertTrue(awaitItem() is Resource.Loading)

            dbFlow.emit(listOf(createEntity("1", "Cafe A")))
            val first = awaitItem()
            assertTrue(first is Resource.Success)
            assertEquals(1, (first as Resource.Success).data.size)

            dbFlow.emit(listOf(createEntity("1", "Cafe A"), createEntity("2", "Cafe B")))
            val second = awaitItem()
            assertTrue(second is Resource.Success)
            assertEquals(2, (second as Resource.Success).data.size)

            cancelAndIgnoreRemainingEvents()
        }
    }

    // endregion

    // region Network failure

    @Test
    fun `emits Error with NetworkError on IOException`() = runTest {
        // Given
        coEvery { remoteDataSource.searchPlaces(query, latLng) } throws IOException("No network")
        coEvery { localDataSource.getSafeSearchResults(query) } returns null

        // When & Then
        repository.searchVenues(query, latLng).test {
            assertTrue(awaitItem() is Resource.Loading)

            val error = awaitItem()
            assertTrue(error is Resource.Error)
            assertEquals(AppError.NetworkError, (error as Resource.Error).error)
            assertEquals(null, error.data)

            awaitComplete()
        }
    }

    @Test
    fun `emits Error with cached data on NetworkError when cache exists`() = runTest {
        // Given
        val cachedEntities = listOf(createEntity("1", "Cached Cafe"))
        coEvery { remoteDataSource.searchPlaces(query, latLng) } throws IOException("No network")
        coEvery { localDataSource.getSafeSearchResults(query) } returns cachedEntities

        // When & Then
        repository.searchVenues(query, latLng).test {
            assertTrue(awaitItem() is Resource.Loading)

            val error = awaitItem()
            assertTrue(error is Resource.Error)
            assertEquals(AppError.NetworkError, (error as Resource.Error).error)

            val cachedVenues = error.data
            assertEquals(1, cachedVenues!!.size)
            assertEquals("Cached Cafe", cachedVenues[0].name)

            awaitComplete()
        }
    }

    @Test
    fun `emits Error with HttpError on HttpException`() = runTest {
        // Given
        val httpException = HttpException(Response.error<Any>(401, "".toResponseBody(null)))
        coEvery { remoteDataSource.searchPlaces(query, latLng) } throws httpException

        // When & Then
        repository.searchVenues(query, latLng).test {
            assertTrue(awaitItem() is Resource.Loading)

            val error = awaitItem()
            assertTrue(error is Resource.Error)
            val appError = (error as Resource.Error).error
            assertTrue(appError is AppError.HttpError)
            assertEquals(401, (appError as AppError.HttpError).code)
            assertEquals(null, error.data)

            awaitComplete()
        }
    }

    @Test
    fun `does not observe DB on network failure`() = runTest {
        // Given
        coEvery { remoteDataSource.searchPlaces(query, latLng) } throws IOException("No network")
        coEvery { localDataSource.getSafeSearchResults(query) } returns null

        // When
        repository.searchVenues(query, latLng).test {
            cancelAndConsumeRemainingEvents()
        }

        // Then
        coVerify(exactly = 0) { localDataSource.observeSearchResults(any()) }
    }

    // endregion

    // region Mapping

    @Test
    fun `maps VenueEntity fields correctly to Venue`() = runTest {
        // Given
        val entity = createEntity(fsqId = "42", name = "My Cafe")
        val dtos = listOf(createDto("42", "My Cafe"))
        coEvery { remoteDataSource.searchPlaces(query, latLng) } returns dtos
        every { localDataSource.observeSearchResults(query) } returns flowOf(listOf(entity))

        // When & Then
        repository.searchVenues(query, latLng).test {
            assertTrue(awaitItem() is Resource.Loading)

            val success = awaitItem() as Resource.Success
            val venue = success.data[0]
            assertEquals("42", venue.fsqId)
            assertEquals("My Cafe", venue.name)
            assertEquals("Cafe", venue.categoryName)
            assertEquals("123 Main St, Belgrade", venue.address)
            assertEquals(150, venue.distance)

            awaitComplete()
        }
    }

    @Test
    fun `maps address to address field when formattedAddress is null`() = runTest {
        // Given
        val entity = createEntity().copy(formattedAddress = null, address = "Plain St")
        val dtos = listOf(createDto())
        coEvery { remoteDataSource.searchPlaces(query, latLng) } returns dtos
        every { localDataSource.observeSearchResults(query) } returns flowOf(listOf(entity))

        // When & Then
        repository.searchVenues(query, latLng).test {
            assertTrue(awaitItem() is Resource.Loading)

            val success = awaitItem() as Resource.Success
            assertEquals("Plain St", success.data[0].address)

            awaitComplete()
        }
    }

    // endregion
}
