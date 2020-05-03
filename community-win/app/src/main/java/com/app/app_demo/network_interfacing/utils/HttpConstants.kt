package com.app.app_demo.network_interfacing.utils

class HttpConstants {

    companion object {

        /**Service Requests**/
        const val SERVICE_REQUEST_TOKEN: String = "http://biharservices.com/api/Usr/Tkn"

        const val SERVICE_REQUEST_TOKEN_UPDATE: String = "http://biharservices.com/api/UsrTknUpd/Tkn_Upd"

        const val SERVICE_REQUEST_USER_CONTACT_LIST: String = "http://biharservices.com/api/UsrContctLst/Get"

        const val SERVICE_REQUEST_ANONYMOUS_CONTACT_UPLOAD: String = "http://biharservices.com/api/UsrContct/Bks"

        /**Request Headers**/
        const val REQ_BODY_NAME_CEL: String = "Cel"
        const val REQ_BODY_NAME_APP: String = "App"
        const val REQ_BODY_NAME_DEVICE_DETAILS: String = "DeviceDet"
        const val REQ_BODY_TYP: String = "Typ"
        const val REQ_BODY_TOKEN: String = "Tkn"
        const val REQ_BODY_CONTACT_ALL: String = "CntactAll"

        /***Response values*/
        const val TOKEN_EXPIRED: String = "Expired"
        const val TOKEN_ACTIVE: String = "Active"
        const val REQ_APP: String = "Rahul-Android"
        const val REQ_BODY_VALUE_TYP: String = "ALL"
    }
}