package com.app.communities_win_crisis.network_interfacing.utils

class HttpConstants {

    companion object {

        /**Service Requests**/
        const val SERVICE_REQUEST_TOKEN: String = "http://biharservices.com/api/Usr/Tkn"

        const val SERVICE_REQUEST_TOKEN_UPDATE: String = "http://biharservices.com/api/UsrTknUpd/Tkn_Upd"

        const val SERVICE_REQUEST_USER_CONTACT_LIST: String = "http://biharservices.com/api/UsrContctLst/Get"

        const val SERVICE_REQUEST_ANONYMOUS_CONTACT_UPLOAD: String = "http://biharservices.com/api/UsrContct/Bks"

        /**Vendor Service Requests**/
        const val SERVICE_REQUEST_VENDOR_BASE_URL: String = "http://vendor.comwin.in/api/VendorDetails/"

        const val REGISTER_ADD_UPDATE_VENDOR: String = "VendorRegistrationAddUpdate"

        const val VENDOR_CATEGORY: String = "VendorCategory"

        const val VENDOR_PRODUCTS_LIST: String = "ProductsList"

        const val VENDOR_PRODUCTS_PRICES: String = "VendorProductPrices"

        const val VENDOR_CORONA_PRECAUTIONS: String = "UpdateVendorCoronaPrecautions"

        const val GET_VENDOR: String = "GetVendor"




        /**Request Headers**/
        const val REQ_BODY_NAME_CEL: String = "Cel"
        const val REQ_BODY_NAME_APP: String = "App"
        const val REQ_BODY_NAME_DEVICE_DETAILS: String = "DeviceDet"
        const val REQ_BODY_TYP: String = "Typ"
        const val REQ_BODY_TOKEN: String = "Tkn"
        const val REQ_BODY_CONTACT_ALL: String = "CntactAll"

        /**Vendor request Headers**/
        const val REQ_BODY_PHONE: String = "Phone"
        const val REQ_BODY_PHONE_NUMBER: String = "phoneNumber"
        const val REQ_BODY_VENDOR_NAME: String = "VendorName"
        const val REQ_BODY_VENDOR_COUNTRY: String = "Country"
        const val REQ_BODY_VENDOR_STATE: String = "State"
        const val REQ_BODY_VENDOR_CITY: String = "City"
        const val REQ_BODY_VENDOR_PIN: String = "Pin"
        const val REQ_BODY_VENDOR_LAT: String = "Latitude"
        const val REQ_BODY_VENDOR_LNG: String = "Longitude"
        const val REQ_BODY_VENDOR_CATEGORIES: String = "Categories"

        const val REQ_BODY_PRODUCT_CATEGORY: String = "CategoryName"
        const val REQ_BODY_PRODUCT_CATEGORY_NAME: String = "categoryname"

        const val REQ_BODY_VENDOR_ENABLE: String = "Enable"

        const val REQ_BODY_VENDOR_APP_KEY: String = "AppKey"

        const val REQ_BODY_PRODUCT_NAME: String = "ProductName"
        const val REQ_BODY_PRICE: String = "Price"
        const val REQ_BODY_MIN_ORDER_QTY: String = "MinOrderQuantity"
        const val REQ_BODY_UNIT: String = "Units"

        const val REQ_BODY_VENDOR_FEVER_SCREEN: String = "IsFeverScreen"
        const val REQ_BODY_VENDOR_SANITIZER_USED: String = "IsSanitizerUsed"
        const val REQ_BODY_VENDOR_STAMP_CHECK: String = "IsStampCheck"
        const val REQ_BODY_VENDOR_SOCIAL_DISTANCE: String = "IsSocialDistance"
        const val REQ_BODY_VENDOR_CONTACT_LESS_PAY: String = "IsContactLessPay"
        const val REQ_HEADER_API_KEY: String = "x-api-key"





        /***Response values*/
        const val TOKEN_EXPIRED: String = "Expired"
        const val TOKEN_ACTIVE: String = "Active"
        const val REQ_APP: String = "Rahul-Android"
        const val REQ_BODY_VALUE_TYP: String = "ALL"
    }
}