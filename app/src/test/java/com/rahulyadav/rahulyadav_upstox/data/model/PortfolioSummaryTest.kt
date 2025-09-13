package com.rahulyadav.rahulyadav_upstox.data.model

import org.junit.Assert.*
import org.junit.Test

class PortfolioSummaryTest {

    @Test
    fun `fromHoldings creates correct summary`() {
        // Given
        val stocks = listOf(
            Stocks("STOCK1", 100, 50.0, 45.0, 55.0), // Current: 5000, Investment: 4500, PnL: 500, Today: 500
            Stocks("STOCK2", 200, 30.0, 35.0, 25.0)  // Current: 6000, Investment: 7000, PnL: -1000, Today: -1000
        )

        // When
        val summary = PortfolioSummary.fromHoldings(stocks)

        // Then
        assertEquals(11000.0, summary.currentValue, 0.01)
        assertEquals(11500.0, summary.totalInvestment, 0.01)
        assertEquals(-500.0, summary.totalPnL, 0.01)
        assertEquals(-500.0, summary.todaysPnL, 0.01)
    }

    @Test
    fun `getTotalPnLPercentage calculates correctly for profit`() {
        // Given
        val summary = PortfolioSummary(11000.0, 10000.0, 1000.0, 500.0)

        // When
        val percentage = summary.getTotalPnLPercentage()

        // Then
        assertEquals(10.0, percentage, 0.01)
    }

    @Test
    fun `getTotalPnLPercentage calculates correctly for loss`() {
        // Given
        val summary = PortfolioSummary(9000.0, 10000.0, -1000.0, -500.0)

        // When
        val percentage = summary.getTotalPnLPercentage()

        // Then
        assertEquals(-10.0, percentage, 0.01)
    }

    @Test
    fun `getTotalPnLPercentage returns zero when total investment is zero`() {
        // Given
        val summary = PortfolioSummary(1000.0, 0.0, 1000.0, 500.0)

        // When
        val percentage = summary.getTotalPnLPercentage()

        // Then
        assertEquals(0.0, percentage, 0.01)
    }

    @Test
    fun `getTodaysPnLPercentage calculates correctly`() {
        // Given
        val summary = PortfolioSummary(11000.0, 10000.0, 1000.0, 500.0)

        // When
        val percentage = summary.getTodaysPnLPercentage()

        // Then
        assertEquals(5.0, percentage, 0.01)
    }

    @Test
    fun `fromHoldings handles empty list`() {
        // Given
        val stocks = emptyList<Stocks>()

        // When
        val summary = PortfolioSummary.fromHoldings(stocks)

        // Then
        assertEquals(0.0, summary.currentValue, 0.01)
        assertEquals(0.0, summary.totalInvestment, 0.01)
        assertEquals(0.0, summary.totalPnL, 0.01)
        assertEquals(0.0, summary.todaysPnL, 0.01)
    }
}


