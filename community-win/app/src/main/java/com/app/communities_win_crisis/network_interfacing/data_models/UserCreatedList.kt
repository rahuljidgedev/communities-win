package com.app.communities_win_crisis.network_interfacing.data_models


import com.google.gson.annotations.SerializedName

data class UserCreatedList(
    @SerializedName("orderName")
    val orderName: String,
    @SerializedName("phoneNumber")
    val phoneNumber: String,
    @SerializedName("createdOn")
    val createdOn: String,
    @SerializedName("deliveryBy")
    val deliveryBy: String,
    @SerializedName("itemList")
    var itemList: List<UserUploadListItem>?
)