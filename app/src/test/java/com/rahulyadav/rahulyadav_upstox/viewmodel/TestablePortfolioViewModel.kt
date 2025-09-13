package com.rahulyadav.upstoxassignment.ui.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.rahulyadav.rahulyadav_upstox.data.model.PortfolioSummary
import com.rahulyadav.rahulyadav_upstox.data.model.Stocks
import com.rahulyadav.rahulyadav_upstox.data.repository.PortfolioRepository

import kotlinx.coroutines.launch

/**
 * Testable version of PortfolioViewModel for unit testing
 */
class TestablePortfolioViewModel(
    private val repository: PortfolioRepository
) : ViewModel() {
    
    // UI State - Individual LiveData for backward compatibility with tests
    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> = _isLoading
    
    private val _error = MutableLiveData<String?>()
    val error: LiveData<String?> = _error
    
    private val _holdings = MutableLiveData<List<Stocks>>()
    val holdings: LiveData<List<Stocks>> = _holdings
    
    private val _portfolioSummary = MutableLiveData<PortfolioSummary?>()
    val portfolioSummary: LiveData<PortfolioSummary?> = _portfolioSummary
    
    private val _isSummaryExpanded = MutableLiveData<Boolean>()
    val isSummaryExpanded: LiveData<Boolean> = _isSummaryExpanded
    
    private val _selectedStocks = MutableLiveData<Stocks?>()
    val selectedStocks: LiveData<Stocks?> = _selectedStocks
    
    init {
        _isSummaryExpanded.value = false
    }
    
    /**
     * Load user holdings from the API
     */
    fun loadUserHoldings() {
        viewModelScope.launch {
            _isLoading.value = true
            _error.value = null
            
            repository.getUserHoldings()
                .onSuccess { holdingsList ->
                    _holdings.value = holdingsList
                    _portfolioSummary.value = PortfolioSummary.fromHoldings(holdingsList)
                }
                .onFailure { exception ->
                    _error.value = exception.message ?: "Unknown error occurred"
                }
            
            _isLoading.value = false
        }
    }
    
    /**
     * Refresh the holdings data
     */
    fun refreshHoldings() {
        loadUserHoldings()
    }
    
    /**
     * Toggle the expanded state of portfolio summary
     */
    fun toggleSummaryExpanded() {
        _isSummaryExpanded.value = !(_isSummaryExpanded.value ?: false)
    }
    
    /**
     * Select a holding item to show its details
     */
    fun selectHolding(stocks: Stocks) {
        _selectedStocks.value = stocks
    }
    
    /**
     * Clear selected holding
     */
    fun clearSelectedHolding() {
        _selectedStocks.value = null
    }
    
    /**
     * Clear error message
     */
    fun clearError() {
        _error.value = null
    }
}


