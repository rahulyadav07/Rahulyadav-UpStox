package com.rahulyadav.rahulyadav_upstox.data.api

import com.rahulyadav.rahulyadav_upstox.data.model.UserHoldingResponse
import retrofit2.Response

/**
 * API service interface following Interface Segregation Principle
 * Defines contract for portfolio API operations
 */
interface IPortfolioApiService {
    
    /**
     * Fetch user holdings from the API
     * @return Response containing UserHoldingResponse
     */
    suspend fun getUserHoldings(): Response<UserHoldingResponse>
}


