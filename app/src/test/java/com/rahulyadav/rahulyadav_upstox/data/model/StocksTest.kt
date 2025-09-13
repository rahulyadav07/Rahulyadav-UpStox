package com.rahulyadav.rahulyadav_upstox.data.model

import org.junit.Assert.*
import org.junit.Test

class StocksTest {

    @Test
    fun `getCurrentValue calculates correctly`() {
        // Given
        val stocks = Stocks("TEST", 100, 50.0, 45.0, 55.0)

        // When
        val currentValue = stocks.getCurrentValue()

        // Then
        assertEquals(5000.0, currentValue, 0.01)
    }

    @Test
    fun `getTotalInvestment calculates correctly`() {
        // Given
        val stocks = Stocks("TEST", 100, 50.0, 45.0, 55.0)

        // When
        val totalInvestment = stocks.getTotalInvestment()

        // Then
        assertEquals(4500.0, totalInvestment, 0.01)
    }

    @Test
    fun `getPnL calculates correctly for profit`() {
        // Given
        val stocks = Stocks("TEST", 100, 50.0, 45.0, 55.0)

        // When
        val pnl = stocks.getPnL()

        // Then
        assertEquals(500.0, pnl, 0.01)
    }

    @Test
    fun `getPnL calculates correctly for loss`() {
        // Given
        val stocks = Stocks("TEST", 100, 40.0, 45.0, 55.0)

        // When
        val pnl = stocks.getPnL()

        // Then
        assertEquals(-500.0, pnl, 0.01)
    }

    @Test
    fun `getTodaysPnL calculates correctly for profit`() {
        // Given
        val stocks = Stocks("TEST", 100, 50.0, 45.0, 55.0)

        // When
        val todaysPnl = stocks.getTodaysPnL()

        // Then
        assertEquals(500.0, todaysPnl, 0.01)
    }

    @Test
    fun `getTodaysPnL calculates correctly for loss`() {
        // Given
        val stocks = Stocks("TEST", 100, 60.0, 45.0, 55.0)

        // When
        val todaysPnl = stocks.getTodaysPnL()

        // Then
        assertEquals(-500.0, todaysPnl, 0.01)
    }
}


