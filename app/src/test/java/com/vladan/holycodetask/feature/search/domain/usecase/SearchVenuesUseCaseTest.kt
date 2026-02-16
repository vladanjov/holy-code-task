package com.vladan.holycodetask.feature.search.domain.usecase

import app.cash.turbine.test
import com.vladan.holycodetask.core.common.AppError
import com.vladan.holycodetask.core.common.Resource
import com.vladan.holycodetask.feature.search.domain.model.Venue
import com.vladan.holycodetask.feature.search.domain.repository.SearchRepository
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test

class SearchVenuesUseCaseTest {

    private lateinit var repository: SearchRepository
    private lateinit var useCase: SearchVenuesUseCase

    @Before
    fun setUp() {
        repository = mockk()
        useCase = SearchVenuesUseCase(repository)
    }

    @Test
    fun `invoke delegates to repository with correct parameters`() = runTest {
        // Given
        val query = "coffee"
        val latLng = "44.787197,20.457273"
        every { repository.searchVenues(query, latLng) } returns flowOf(Resource.Loading)

        // When
        useCase(query, latLng).test {
            awaitItem()
            awaitComplete()
        }

        // Then
        verify(exactly = 1) { repository.searchVenues(query, latLng) }
    }

    @Test
    fun `invoke emits Loading then Success when repository succeeds`() = runTest {
        // Given
        val venues = listOf(
            Venue(
                fsqId = "1",
                name = "Coffee Shop",
                categoryName = "Cafe",
                address = "123 Main St",
                distance = 150,
            ),
            Venue(
                fsqId = "2",
                name = "Tea House",
                categoryName = "Tea Room",
                address = null,
                distance = 300,
            ),
        )
        every { repository.searchVenues("coffee", "44.0,20.0") } returns flowOf(
            Resource.Loading,
            Resource.Success(venues),
        )

        // When & Then
        useCase("coffee", "44.0,20.0").test {
            assertTrue(awaitItem() is Resource.Loading)

            val success = awaitItem()
            assertTrue(success is Resource.Success)
            assertEquals(venues, (success as Resource.Success).data)

            awaitComplete()
        }
    }

    @Test
    fun `invoke emits Loading then Error when repository fails with network error`() = runTest {
        // Given
        every { repository.searchVenues("coffee", "44.0,20.0") } returns flowOf(
            Resource.Loading,
            Resource.Error(AppError.NetworkError),
        )

        // When & Then
        useCase("coffee", "44.0,20.0").test {
            assertTrue(awaitItem() is Resource.Loading)

            val error = awaitItem()
            assertTrue(error is Resource.Error)
            assertEquals(AppError.NetworkError, (error as Resource.Error).error)

            awaitComplete()
        }
    }

    @Test
    fun `invoke emits Loading then Error when repository fails with http error`() = runTest {
        // Given
        val httpError = AppError.HttpError(code = 401, message = "Unauthorized")
        every { repository.searchVenues("coffee", "44.0,20.0") } returns flowOf(
            Resource.Loading,
            Resource.Error(httpError),
        )

        // When & Then
        useCase("coffee", "44.0,20.0").test {
            assertTrue(awaitItem() is Resource.Loading)

            val error = awaitItem()
            assertTrue(error is Resource.Error)
            assertEquals(httpError, (error as Resource.Error).error)

            awaitComplete()
        }
    }

    @Test
    fun `invoke emits Success with empty list when no venues found`() = runTest {
        // Given
        every { repository.searchVenues("xyznonexistent", "44.0,20.0") } returns flowOf(
            Resource.Loading,
            Resource.Success(emptyList()),
        )

        // When & Then
        useCase("xyznonexistent", "44.0,20.0").test {
            assertTrue(awaitItem() is Resource.Loading)

            val success = awaitItem()
            assertTrue(success is Resource.Success)
            assertTrue((success as Resource.Success).data.isEmpty())

            awaitComplete()
        }
    }

    @Test
    fun `invoke emits Error with cached data when repository returns error with data`() = runTest {
        // Given
        val cachedVenues = listOf(
            Venue(
                fsqId = "1",
                name = "Cached Cafe",
                categoryName = "Cafe",
                address = "456 Old St",
                distance = 200,
            ),
        )
        every { repository.searchVenues("coffee", "44.0,20.0") } returns flowOf(
            Resource.Loading,
            Resource.Error(AppError.NetworkError, data = cachedVenues),
        )

        // When & Then
        useCase("coffee", "44.0,20.0").test {
            assertTrue(awaitItem() is Resource.Loading)

            val error = awaitItem()
            assertTrue(error is Resource.Error)
            assertEquals(AppError.NetworkError, (error as Resource.Error).error)
            assertEquals(cachedVenues, error.data)

            awaitComplete()
        }
    }
}
