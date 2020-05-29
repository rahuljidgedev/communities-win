package com.app.communities_win_crisis.network_interfacing.data_models

import com.google.gson.annotations.SerializedName

data class UserContactUpdatedTable(
    @SerializedName("cts")
    val count: Int
)
