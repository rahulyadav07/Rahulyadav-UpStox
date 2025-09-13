package com.rahulyadav.rahulyadav_upstox.utils

import com.rahulyadav.rahulyadav_upstox.R
import java.text.DecimalFormat

/**
 * Centralized formatting utility following Single Responsibility Principle
 * Handles all display formatting operations
 */
object DisplayFormatter {

    private val decimalFormat = DecimalFormat("#,##0.00")

    fun formatCurrency(value: Double): String {
        // show sign for positive values to match colored text in UI if you want: "+₹ 1,234.00"
        val sign = if (value > 0) "+" else if (value < 0) "-" else ""
        // use absolute value for formatting, keep sign separate
        return "$sign₹ ${decimalFormat.format(kotlin.math.abs(value))}"
    }

    fun formatPercentage(value: Double): String {
        val sign = if (value > 0) "+" else if (value < 0) "-" else ""
        return "$sign${decimalFormat.format(kotlin.math.abs(value))}%"
    }

    fun formatQuantity(quantity: Int): String = quantity.toString()

    /**
     * Color resource id: return different resource for positive/zero/negative if needed
     */
    fun getPnLColorResource(value: Double): Int {
        return if ( value > 0.0)
           R.color.green
           else R.color.red


    }

    /**
     * Combined formatted PnL (currency + percentage)
     */
    fun formatPnLWithPercentage(pnlValue: Double, percentage: Double): String {
        val currencyText = formatCurrency(pnlValue)
        val percentageText = formatPercentage(percentage)
        return "$currencyText ($percentageText)"
    }
}

