package com.rahulyadav.rahulyadav_upstox.data.repository

import com.rahulyadav.rahulyadav_upstox.data.model.Stocks

/**
 * Repository interface following Dependency Inversion Principle
 * Defines contract for portfolio data operations
 */
interface IPortfolioRepository {
    

    suspend fun getUserHoldings(): Result<List<Stocks>>
}


