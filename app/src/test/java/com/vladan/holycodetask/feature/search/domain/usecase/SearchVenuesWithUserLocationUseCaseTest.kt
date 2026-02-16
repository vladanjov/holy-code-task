package com.vladan.holycodetask.feature.search.domain.usecase

import app.cash.turbine.test
import com.vladan.holycodetask.core.common.AppError
import com.vladan.holycodetask.core.common.GetUserLocationUseCase
import com.vladan.holycodetask.core.common.Resource
import com.vladan.holycodetask.feature.search.domain.model.Venue
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test

class SearchVenuesWithUserLocationUseCaseTest {

    private lateinit var searchVenuesUseCase: SearchVenuesUseCase
    private lateinit var getUserLocationUseCase: GetUserLocationUseCase
    private lateinit var useCase: SearchVenuesWithUserLocationUseCase

    @Before
    fun setUp() {
        searchVenuesUseCase = mockk()
        getUserLocationUseCase = mockk()
        useCase = SearchVenuesWithUserLocationUseCase(searchVenuesUseCase, getUserLocationUseCase)
    }

    private val testVenues = listOf(
        Venue(
            fsqId = "1",
            name = "Coffee Shop",
            categoryName = "Cafe",
            address = "123 Main St",
            distance = 150,
        ),
    )

    @Test
    fun `invoke gets user location and passes it to search use case`() = runTest {
        // Given
        coEvery { getUserLocationUseCase() } returns "44.8176,20.4633"
        every { searchVenuesUseCase("coffee", "44.8176,20.4633") } returns flowOf(Resource.Loading)

        // When
        useCase("coffee").test {
            awaitItem()
            awaitComplete()
        }

        // Then
        coVerify(exactly = 1) { getUserLocationUseCase() }
        verify(exactly = 1) { searchVenuesUseCase("coffee", "44.8176,20.4633") }
    }

    @Test
    fun `invoke emits Loading then Success with venues`() = runTest {
        // Given
        coEvery { getUserLocationUseCase() } returns "44.0,20.0"
        every { searchVenuesUseCase("coffee", "44.0,20.0") } returns flowOf(
            Resource.Loading,
            Resource.Success(testVenues),
        )

        // When & Then
        useCase("coffee").test {
            assertTrue(awaitItem() is Resource.Loading)

            val success = awaitItem()
            assertTrue(success is Resource.Success)
            assertEquals(testVenues, (success as Resource.Success).data)

            awaitComplete()
        }
    }

    @Test
    fun `invoke emits Loading then Error when search fails`() = runTest {
        // Given
        coEvery { getUserLocationUseCase() } returns "44.0,20.0"
        every { searchVenuesUseCase("coffee", "44.0,20.0") } returns flowOf(
            Resource.Loading,
            Resource.Error(AppError.NetworkError),
        )

        // When & Then
        useCase("coffee").test {
            assertTrue(awaitItem() is Resource.Loading)

            val error = awaitItem()
            assertTrue(error is Resource.Error)
            assertEquals(AppError.NetworkError, (error as Resource.Error).error)

            awaitComplete()
        }
    }

    @Test
    fun `invoke emits Success with empty list when no venues match`() = runTest {
        // Given
        coEvery { getUserLocationUseCase() } returns "44.8176,20.4633"
        every { searchVenuesUseCase("pizza", "44.8176,20.4633") } returns flowOf(
            Resource.Loading,
            Resource.Success(emptyList()),
        )

        // When & Then
        useCase("pizza").test {
            assertTrue(awaitItem() is Resource.Loading)

            val success = awaitItem()
            assertTrue(success is Resource.Success)
            assertTrue((success as Resource.Success).data.isEmpty())

            awaitComplete()
        }
    }

    @Test
    fun `invoke propagates exception when getUserLocation throws`() = runTest {
        // Given
        coEvery { getUserLocationUseCase() } throws SecurityException("Permission denied")

        // When & Then
        try {
            useCase("coffee")
            assertTrue("Should have thrown", false)
        } catch (e: SecurityException) {
            assertEquals("Permission denied", e.message)
        }
    }
}
