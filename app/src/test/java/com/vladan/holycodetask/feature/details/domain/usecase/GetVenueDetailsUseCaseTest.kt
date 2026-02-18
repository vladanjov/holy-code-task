package com.vladan.holycodetask.feature.details.domain.usecase

import app.cash.turbine.test
import com.vladan.holycodetask.core.common.AppError
import com.vladan.holycodetask.core.common.Resource
import com.vladan.holycodetask.feature.details.domain.model.VenueDetails
import com.vladan.holycodetask.feature.details.domain.repository.DetailsRepository
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test

class GetVenueDetailsUseCaseTest {

    private lateinit var repository: DetailsRepository
    private lateinit var useCase: GetVenueDetailsUseCase

    @Before
    fun setUp() {
        repository = mockk()
        useCase = GetVenueDetailsUseCase(repository)
    }

    private fun createVenueDetails(
        fsqId: String = "abc123",
        name: String = "Test Venue",
    ) = VenueDetails(
        fsqId = fsqId,
        name = name,
        categoryName = "Cafe",
        categoryIconUrl = "https://example.com/icon.png",
        address = "123 Main St, Belgrade",
        description = "A nice cafe",
        tel = "+381111234567",
        website = "https://example.com",
        hoursDisplay = "Mon-Fri 8:00-20:00",
        openNow = true,
        photos = listOf("https://example.com/photo1.jpg"),
        latitude = 44.8176,
        longitude = 20.4633,
        socialInstagram = "testcafe",
        socialTwitter = "testcafe",
    )

    @Test
    fun `invoke delegates to repository with correct fsqId`() = runTest {
        // Given
        val fsqId = "abc123"
        every { repository.getVenueDetails(fsqId) } returns flowOf(Resource.Loading)

        // When
        useCase(fsqId).test {
            awaitItem()
            awaitComplete()
        }

        // Then
        verify(exactly = 1) { repository.getVenueDetails(fsqId) }
    }

    @Test
    fun `invoke emits Loading then Success when repository succeeds`() = runTest {
        // Given
        val venueDetails = createVenueDetails()
        every { repository.getVenueDetails("abc123") } returns flowOf(
            Resource.Loading,
            Resource.Success(venueDetails),
        )

        // When & Then
        useCase("abc123").test {
            assertTrue(awaitItem() is Resource.Loading)

            val success = awaitItem()
            assertTrue(success is Resource.Success)
            assertEquals(venueDetails, (success as Resource.Success).data)

            awaitComplete()
        }
    }

    @Test
    fun `invoke emits Loading then Error on network failure`() = runTest {
        // Given
        every { repository.getVenueDetails("abc123") } returns flowOf(
            Resource.Loading,
            Resource.Error(AppError.NetworkError),
        )

        // When & Then
        useCase("abc123").test {
            assertTrue(awaitItem() is Resource.Loading)

            val error = awaitItem()
            assertTrue(error is Resource.Error)
            assertEquals(AppError.NetworkError, (error as Resource.Error).error)

            awaitComplete()
        }
    }

    @Test
    fun `invoke emits Loading then Error on http failure`() = runTest {
        // Given
        val httpError = AppError.HttpError(code = 404, message = "Not Found")
        every { repository.getVenueDetails("nonexistent") } returns flowOf(
            Resource.Loading,
            Resource.Error(httpError),
        )

        // When & Then
        useCase("nonexistent").test {
            assertTrue(awaitItem() is Resource.Loading)

            val error = awaitItem()
            assertTrue(error is Resource.Error)
            assertEquals(httpError, (error as Resource.Error).error)

            awaitComplete()
        }
    }

    @Test
    fun `invoke emits Error with cached data when repository returns error with data`() = runTest {
        // Given
        val cachedDetails = createVenueDetails()
        every { repository.getVenueDetails("abc123") } returns flowOf(
            Resource.Loading,
            Resource.Error(AppError.NetworkError, data = cachedDetails),
        )

        // When & Then
        useCase("abc123").test {
            assertTrue(awaitItem() is Resource.Loading)

            val error = awaitItem()
            assertTrue(error is Resource.Error)
            assertEquals(cachedDetails, (error as Resource.Error).data)

            awaitComplete()
        }
    }

    @Test
    fun `invoke emits Success with venue details containing null optional fields`() = runTest {
        // Given
        val minimalDetails = VenueDetails(
            fsqId = "abc123",
            name = "Minimal Venue",
            categoryName = "",
            categoryIconUrl = "",
            address = "",
            description = "",
            tel = "",
            website = "",
            hoursDisplay = "",
            openNow = null,
            photos = emptyList(),
            latitude = null,
            longitude = null,
            socialInstagram = "",
            socialTwitter = "",
        )
        every { repository.getVenueDetails("abc123") } returns flowOf(
            Resource.Loading,
            Resource.Success(minimalDetails),
        )

        // When & Then
        useCase("abc123").test {
            assertTrue(awaitItem() is Resource.Loading)

            val success = awaitItem()
            assertTrue(success is Resource.Success)
            assertEquals(minimalDetails, (success as Resource.Success).data)

            awaitComplete()
        }
    }
}
