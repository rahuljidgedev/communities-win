package com.app.communities_win_crisis.network_interfacing.data_models


import com.google.gson.annotations.SerializedName

data class ProductListItem(
    @SerializedName("Category")
    val category: String,
    @SerializedName("ImageUrl")
    val imageUrl: String,
    @SerializedName("ProductName")
    val productName: String
)