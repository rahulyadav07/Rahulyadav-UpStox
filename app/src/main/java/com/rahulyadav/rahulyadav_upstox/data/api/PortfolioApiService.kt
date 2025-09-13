package com.rahulyadav.rahulyadav_upstox.data.api

import com.rahulyadav.rahulyadav_upstox.data.model.UserHoldingResponse
import retrofit2.Response
import retrofit2.http.GET

/**
 * Retrofit API service implementation for portfolio data
 * Implements IPortfolioApiService interface
 */
interface PortfolioApiService : IPortfolioApiService {
    
    /**
     * Fetch user holdings from the API
     * @return Response containing UserHoldingResponse
     */
    @GET("/")
    override suspend fun getUserHoldings(): Response<UserHoldingResponse>
}
