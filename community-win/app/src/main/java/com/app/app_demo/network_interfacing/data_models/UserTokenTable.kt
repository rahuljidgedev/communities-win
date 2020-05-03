package com.app.app_demo.network_interfacing.data_models


import com.google.gson.annotations.SerializedName

data class UserTokenTable(
    @SerializedName("dys")
    val dys: Int,
    @SerializedName("expiryVal")
    val expiryVal: String,
    @SerializedName("tknNum")
    val tknNum: Int,
    @SerializedName("usrDtb")
    val usrDtb: Any
)