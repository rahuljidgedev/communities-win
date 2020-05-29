package com.app.communities_win_crisis.network_interfacing.data_models


import com.google.gson.annotations.SerializedName

data class VendorProduct(
    @SerializedName("MinOrderQuantity")
    val minOrderQuantity: Double,
    @SerializedName("Price")
    val price: Double,
    @SerializedName("ProductName")
    val productName: String,
    @SerializedName("Units")
    val units: String
)