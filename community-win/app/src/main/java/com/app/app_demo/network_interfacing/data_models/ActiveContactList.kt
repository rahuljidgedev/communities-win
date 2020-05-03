package com.app.app_demo.network_interfacing.data_models


import com.google.gson.annotations.SerializedName

data class ActiveContactList(
    @SerializedName("table")
    val activeTable: List<ActiveContactTable>
)