package com.app.communities_win_crisis.network_interfacing.data_models


import com.google.gson.annotations.SerializedName

data class UserUploadListItem(
    @SerializedName("itemName")
    val itemName: String,
    @SerializedName("category")
    val category: String,
    @SerializedName("quantity")
    val quantity: Float,
    @SerializedName("unit")
    val unit: String
)