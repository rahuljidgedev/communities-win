package com.app.communities_win_crisis.network_interfacing.data_models


import com.google.gson.annotations.SerializedName

data class UserToken(
    @SerializedName("table")
    val table: List<UserTokenTable>,

    @SerializedName("table1")
    val table2: List<UserContactUpdatedTable>
)