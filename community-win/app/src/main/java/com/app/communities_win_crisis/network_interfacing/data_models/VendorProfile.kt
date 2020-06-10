package com.app.communities_win_crisis.network_interfacing.data_models


import com.google.gson.annotations.SerializedName

data class VendorProfile(
    @SerializedName("VendorCity")
    val vendorCity: String,
    @SerializedName("VendorFeverScreening")
    val vendorFeverScreening: Boolean,
    @SerializedName("vendor_name")
    val vendorName: String,
    @SerializedName("VendorPin")
    val vendorPin: String,
    @SerializedName("VendorProducts")
    val vendorProducts: List<VendorProduct>,
    @SerializedName("VendorSantizier")
    val vendorSantizier: Boolean,
    @SerializedName("VendorSocialDistanced")
    val vendorSocialDistanced: Boolean,
    @SerializedName("VendorStampCheck")
    val vendorStampCheck: Boolean,
    @SerializedName("VendorContactLessPay")
    val vendorContactLessPay: Boolean,
    @SerializedName("VendorState")
    val vendorState: String,
    @SerializedName("Vendorcountry")
    val vendorcountry: String,
    @SerializedName("Vendorlatitude")
    val vendorlatitude: Any,
    @SerializedName("Vendorlongitude")
    val vendorlongitude: Any
)