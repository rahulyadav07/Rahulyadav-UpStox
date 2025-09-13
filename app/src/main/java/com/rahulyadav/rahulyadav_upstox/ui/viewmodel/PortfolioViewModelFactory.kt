package com.rahulyadav.rahulyadav_upstox.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.rahulyadav.rahulyadav_upstox.data.repository.IPortfolioRepository

/**
 * ViewModel Factory following Factory Pattern
 * Creates ViewModel instances with proper dependencies
 */
class PortfolioViewModelFactory(
    private val repository: IPortfolioRepository
) : ViewModelProvider.Factory {
    
    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(PortfolioViewModel::class.java)) {
            return PortfolioViewModel(repository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class: ${modelClass.name}")
    }
}


