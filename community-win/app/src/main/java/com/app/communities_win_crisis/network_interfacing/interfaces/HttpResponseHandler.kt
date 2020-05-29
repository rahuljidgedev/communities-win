package com.app.communities_win_crisis.network_interfacing.interfaces

interface HttpResponseHandler {
    fun onSucceed(responseString: String?, contact: String?, requestName: String?)
    fun onSucceed(responseString: String?, requestName: String?)
    fun onFailure(message: String?)
}