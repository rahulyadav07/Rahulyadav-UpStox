package com.rahulyadav.rahulyadav_upstox.data.repository

import com.rahulyadav.rahulyadav_upstox.data.api.PortfolioApiService
import com.rahulyadav.rahulyadav_upstox.data.model.Stocks
import com.rahulyadav.rahulyadav_upstox.data.model.UserHoldingData
import com.rahulyadav.rahulyadav_upstox.data.model.UserHoldingResponse

import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import org.junit.Assert.*
import org.junit.Before
import org.junit.Test
import retrofit2.Response

class PortfolioRepositoryTest {

    private lateinit var repository: TestablePortfolioRepository
    private lateinit var mockApiService: PortfolioApiService

    @Before
    fun setup() {
        mockApiService = mockk()
        repository = TestablePortfolioRepository(mockApiService)
    }

    @Test
    fun `getUserHoldings returns success when API call succeeds`() = runTest {
        // Given
        val mockStocks = listOf(
            Stocks("MAHABANK", 990, 38.05, 35.0, 40.0),
            Stocks("ICICI", 100, 118.25, 110.0, 105.0)
        )
        val mockResponse = Response.success(
            UserHoldingResponse(
                UserHoldingData(mockStocks)
            )
        )
        coEvery { mockApiService.getUserHoldings() } returns mockResponse

        // When
        val result = repository.getUserHoldings()

        // Then
        assertTrue(result.isSuccess)
        assertEquals(mockStocks, result.getOrNull())
    }

    @Test
    fun `getUserHoldings returns failure when API call fails`() = runTest {
        // Given
        val mockResponse = Response.error<UserHoldingResponse>(500, mockk())
        coEvery { mockApiService.getUserHoldings() } returns mockResponse

        // When
        val result = repository.getUserHoldings()

        // Then
        assertTrue(result.isFailure)
        assertTrue(result.exceptionOrNull()?.message?.contains("API Error") == true)
    }

    @Test
    fun `getUserHoldings returns failure when network exception occurs`() = runTest {
        // Given
        val exception = Exception("Network error")
        coEvery { mockApiService.getUserHoldings() } throws exception

        // When
        val result = repository.getUserHoldings()

        // Then
        assertTrue(result.isFailure)
        assertEquals("Network error", result.exceptionOrNull()?.message)
    }

    @Test
    fun `getUserHoldings returns empty list when response body is null`() = runTest {
        // Given
        val mockResponse = Response.success<UserHoldingResponse>(null)
        coEvery { mockApiService.getUserHoldings() } returns mockResponse

        // When
        val result = repository.getUserHoldings()

        // Then
        assertTrue(result.isSuccess)
        assertEquals(emptyList<Stocks>(), result.getOrNull())
    }
}
