package com.app.community_win_crisis.network_interfacing.data_models


import com.google.gson.annotations.SerializedName

data class ActiveContactTable(
    @SerializedName("cel")
    val cel: String,
    @SerializedName("prvAct")
    var prvAct: String
)