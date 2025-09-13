package com.rahulyadav.rahulyadav_upstox.data.repository

import com.rahulyadav.rahulyadav_upstox.data.api.PortfolioApiService
import com.rahulyadav.rahulyadav_upstox.data.model.Stocks

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.io.IOException

/**
 * Testable version of PortfolioRepository for unit testing
 */
class TestablePortfolioRepository(
    private val apiService: PortfolioApiService
) {
    
    /**
     * Fetch user holdings from the API
     * @return Result containing either the list of holdings or an error
     */
    suspend fun getUserHoldings(): Result<List<Stocks>> = withContext(Dispatchers.IO) {
        try {
            val response = apiService.getUserHoldings()
            
            if (response.isSuccessful) {
                val holdings = response.body()?.data?.userHolding ?: emptyList()
                Result.success(holdings)
            } else {
                Result.failure(
                    IOException("API Error: ${response.code()} - ${response.message()}")
                )
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}


