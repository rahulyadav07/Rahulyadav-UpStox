package com.rahulyadav.rahulyadav_upstox.data.model

import com.google.gson.annotations.SerializedName

/**
 * Data class representing the API response structure
 */
data class UserHoldingResponse(
    @SerializedName("data")
    val data: UserHoldingData
)

/**
 * Data class representing the data wrapper in the API response
 */
data class UserHoldingData(
    @SerializedName("userHolding")
    val userHolding: List<Stocks>
)


