package com.rahulyadav.rahulyadav_upstox.ui.viewmodel

import com.rahulyadav.rahulyadav_upstox.data.model.Stocks
import com.rahulyadav.rahulyadav_upstox.data.model.PortfolioSummary

/**
 * Sealed class representing all possible UI states for Portfolio screen
 */
sealed class PortfolioUiState {
    object Loading : PortfolioUiState()
    data class Success(
        val holdings: List<Stocks>,
        val portfolioSummary: PortfolioSummary,
        val isSummaryExpanded: Boolean = false,
        val selectedStocks: Stocks? = null
    ) : PortfolioUiState()
    data class Error(val message: String) : PortfolioUiState()
}
