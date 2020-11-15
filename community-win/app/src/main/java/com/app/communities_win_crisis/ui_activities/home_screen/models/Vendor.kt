package com.app.communities_win_crisis.ui_activities.home_screen.models

import com.google.gson.annotations.SerializedName

data class Vendor(
    @SerializedName("VendorCity")
    val City: String,
    @SerializedName("Vendorcountry")
    val Country: String,
    @SerializedName("VendorContactLessPay")
    val IsContactLessPay: Any,
    @SerializedName("VendorFeverScreening")
    val IsFeverScreen: Any,
    @SerializedName("VendorSantizier")
    val IsSanitizerUsed: Any,
    @SerializedName("VendorSocialDistanced")
    val IsSocialDistance: Any,
    @SerializedName("VendorStampCheck")
    val IsStampCheck: Any,
    @SerializedName("Vendorlatitude")
    val Latitude: Double,
    @SerializedName("Vendorlongitude")
    val Longitude: Double,
    @SerializedName("Phone")
    val Phone: Long,
    @SerializedName("VendorPin")
    val Pin: String,
    @SerializedName("VendorState")
    val State: String,
    @SerializedName("vendor_name")
    val VendorName: String
)