package com.rahulyadav.rahulyadav_upstox.ui.viewmodel

import androidx.lifecycle.LiveData
import com.rahulyadav.rahulyadav_upstox.data.model.Stocks

/**
 * ViewModel interface following Interface Segregation Principle
 * Defines contract for portfolio UI operations
 */
interface IPortfolioViewModel {
    
    val uiState: LiveData<PortfolioUiState>
    
    /**
     * Load user holdings from data source
     */
    fun loadUserHoldings()
    
    /**
     * Refresh the holdings data
     */
    fun refreshHoldings()
    
    /**
     * Toggle the expanded state of portfolio summary
     */
    fun toggleSummaryExpanded()
    
    /**
     * Select a holding item to show its details
     */
    fun selectHolding(stocks: Stocks)
    
    /**
     * Clear selected holding
     */
    fun clearSelectedHolding()
}


