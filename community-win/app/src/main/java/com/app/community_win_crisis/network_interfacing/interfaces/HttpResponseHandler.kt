package com.app.community_win_crisis.network_interfacing.interfaces

interface HttpResponseHandler {
    fun onSucceed(responseString: String?, contact: String?, requestName: String?)
    fun onFailure(message: String?)
}