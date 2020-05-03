package com.app.app_demo.network_interfacing.data_models


import com.google.gson.annotations.SerializedName

data class ActiveContactTable(
    @SerializedName("cel")
    val cel: String,
    @SerializedName("prvAct")
    var prvAct: String
)