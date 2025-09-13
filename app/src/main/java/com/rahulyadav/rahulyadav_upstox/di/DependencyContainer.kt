package com.rahulyadav.rahulyadav_upstox.di

import com.rahulyadav.rahulyadav_upstox.data.api.ApiClient
import com.rahulyadav.rahulyadav_upstox.data.api.IPortfolioApiService
import com.rahulyadav.rahulyadav_upstox.data.repository.IPortfolioRepository
import com.rahulyadav.rahulyadav_upstox.data.repository.PortfolioRepository

/**
 * Simple dependency injection container following Dependency Inversion Principle
 * Manages object creation and dependencies
 */
object DependencyContainer {
    

    private val apiService: IPortfolioApiService by lazy { ApiClient.portfolioApiService }
    
    private val repository: IPortfolioRepository by lazy {
        PortfolioRepository(apiService)
    }
    

    fun getPortfolioRepository(): IPortfolioRepository = repository

}


