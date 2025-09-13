package com.rahulyadav.rahulyadav_upstox.data.model

import com.google.gson.annotations.SerializedName


data class Stocks(
    @SerializedName("symbol")
    val symbol: String,

    @SerializedName("quantity")
    val quantity: Int,

    @SerializedName("ltp")
    val ltp: Double, // Last Traded Price

    @SerializedName("avgPrice")
    val avgPrice: Double, // Average Price

    @SerializedName("close")
    val close: Double
) {

    fun getCurrentValue(): Double = ltp * quantity


    fun getTotalInvestment(): Double = avgPrice * quantity


    fun getPnL(): Double = getCurrentValue() - getTotalInvestment()


    fun getPnLPercentage(): Double {
        val inv = getTotalInvestment()
        return if (inv == 0.0) 0.0 else (getPnL() / inv) * 100.0
    }


    fun getTodaysPnL(): Double = (ltp - close) * quantity

}

