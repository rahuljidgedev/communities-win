package com.app.communities_win_crisis.ui_activities.home_screen.models

import com.google.gson.annotations.SerializedName

data class VendorListing(
    @SerializedName("Table")
    val Vendor: List<Vendor>
)