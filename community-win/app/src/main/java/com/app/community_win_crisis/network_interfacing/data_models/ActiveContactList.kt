package com.app.community_win_crisis.network_interfacing.data_models


import com.google.gson.annotations.SerializedName

data class ActiveContactList(
    @SerializedName("table")
    val activeTable: List<ActiveContactTable>
)