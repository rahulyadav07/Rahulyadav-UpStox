package com.rahulyadav.rahulyadav_upstox.data.model

/**
 * Data class representing the portfolio summary calculations
 */
data class PortfolioSummary(
    val currentValue: Double,
    val totalInvestment: Double,
    val totalPnL: Double,
    val todaysPnL: Double
) {

    fun getTotalPnLPercentage(): Double {
        return if (totalInvestment != 0.0) {
            (totalPnL / totalInvestment) * 100
        } else 0.0
    }
    

    fun getTodaysPnLPercentage(): Double {
        return if (totalInvestment != 0.0) {
            (todaysPnL / totalInvestment) * 100
        } else 0.0
    }
    
    companion object {

        fun fromHoldings(stocks: List<Stocks>): PortfolioSummary {
            val currentValue = stocks.sumOf { it.getCurrentValue() }
            val totalInvestment = stocks.sumOf { it.getTotalInvestment() }
            val totalPnL = currentValue - totalInvestment
            val todaysPnL = stocks.sumOf { it.getTodaysPnL() }
            
            return PortfolioSummary(
                currentValue = currentValue,
                totalInvestment = totalInvestment,
                totalPnL = totalPnL,
                todaysPnL = todaysPnL
            )
        }
    }
}


