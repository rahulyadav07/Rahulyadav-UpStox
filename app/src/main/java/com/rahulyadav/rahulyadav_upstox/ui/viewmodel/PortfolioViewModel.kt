package com.rahulyadav.rahulyadav_upstox.ui.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.rahulyadav.rahulyadav_upstox.data.model.Stocks
import com.rahulyadav.rahulyadav_upstox.data.model.PortfolioSummary
import com.rahulyadav.rahulyadav_upstox.data.repository.IPortfolioRepository
import kotlinx.coroutines.launch

class PortfolioViewModel(
    private val repository: IPortfolioRepository
) : ViewModel(), IPortfolioViewModel {

    private val _uiState = MutableLiveData<PortfolioUiState>()
    override val uiState: LiveData<PortfolioUiState> = _uiState
    
    init {
        _uiState.value = PortfolioUiState.Loading
        loadUserHoldings()
    }

    override fun loadUserHoldings() {
        viewModelScope.launch {
            _uiState.value = PortfolioUiState.Loading
            
            repository.getUserHoldings()
                .onSuccess { holdingsList ->
                    val portfolioSummary = PortfolioSummary.fromHoldings(holdingsList)
                    _uiState.value = PortfolioUiState.Success(
                        holdings = holdingsList,
                        portfolioSummary = portfolioSummary,
                        isSummaryExpanded = false,
                        selectedStocks = null
                    )
                }
                .onFailure { exception ->
                    _uiState.value = PortfolioUiState.Error(
                        message = exception.message ?: "Unknown error occurred"
                    )
                }
        }
    }
    
    override fun refreshHoldings() {
        loadUserHoldings()
    }
    
    override fun toggleSummaryExpanded() {
        val currentState = _uiState.value
        if (currentState is PortfolioUiState.Success) {
            _uiState.value = currentState.copy(
                isSummaryExpanded = !currentState.isSummaryExpanded
            )
        }
    }
    
    override fun selectHolding(stocks: Stocks) {
        val currentState = _uiState.value
        if (currentState is PortfolioUiState.Success) {
            _uiState.value = currentState.copy(
                selectedStocks = stocks
            )
        }
    }
    
    override fun clearSelectedHolding() {
        val currentState = _uiState.value
        if (currentState is PortfolioUiState.Success) {
            _uiState.value = currentState.copy(
                selectedStocks = null
            )
        }
    }
}
