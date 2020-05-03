package com.app.app_demo.network_interfacing.interfaces

import okhttp3.Response

interface HttpResponseHandler {
    fun onSucceed(responseString: String?, contact: String?, requestName: String?)
    fun onFailure(message: String?)
}