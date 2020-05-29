package com.app.communities_win_crisis.network_interfacing.data_models


import com.google.gson.annotations.SerializedName

data class PushedContact(
    @SerializedName("table")
    val pushedContactTable: List<PushedContactTable>
)