package com.rahulyadav.rahulyadav_upstox.data.repository

import com.rahulyadav.rahulyadav_upstox.data.api.IPortfolioApiService
import com.rahulyadav.rahulyadav_upstox.data.model.Stocks
import com.rahulyadav.rahulyadav_upstox.data.repository.IPortfolioRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.io.IOException

/**
 * Repository implementation following Single Responsibility and Dependency Inversion Principles
 * Handles portfolio data operations with proper abstraction
 */
class PortfolioRepository(
    private val apiService: IPortfolioApiService
) : IPortfolioRepository {
    
    /**
     * Fetch user holdings from the API
     * @return Result containing either the list of holdings or an error
     */
    override suspend fun getUserHoldings(): Result<List<Stocks>> = withContext(Dispatchers.IO) {
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
