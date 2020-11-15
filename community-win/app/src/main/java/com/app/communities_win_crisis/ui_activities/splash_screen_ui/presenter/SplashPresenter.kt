package com.app.communities_win_crisis.ui_activities.splash_screen_ui.presenter

import android.os.Build
import com.app.communities_win_crisis.network_interfacing.data_models.UserToken
import com.app.communities_win_crisis.network_interfacing.interfaces.HttpResponseHandler
import com.app.communities_win_crisis.network_interfacing.utils.GetTokenRequest
import com.app.communities_win_crisis.network_interfacing.utils.HttpConstants
import com.app.communities_win_crisis.network_interfacing.utils.UpdateTokenRequest
import com.app.communities_win_crisis.ui_activities.splash_screen_ui.SplashActivity
import com.google.gson.Gson

class SplashPresenter(var context: SplashActivity): HttpResponseHandler {

    fun checkAppTokenAvailable(){
        val map: HashMap<String,String> = HashMap(1)
        map[HttpConstants.REQ_BODY_NAME_CEL] = context.userContact!!
        if (context.userContact?.isNotEmpty()!!){
            GetTokenRequest().execute(HttpConstants.SERVICE_REQUEST_TOKEN, map, this)
        }
    }

    override fun onSucceed(responseString: String?, contact: String?, requestName: String?) {
        val userToken = Gson().fromJson(responseString, UserToken::class.java)
        /*If token is expired then request for token update*/
        if (userToken.table[0].expiryVal == HttpConstants.TOKEN_EXPIRED){
            val map: HashMap<String,String> = HashMap(3)
            map[HttpConstants.REQ_BODY_NAME_CEL] = context.userContact!!
            map[HttpConstants.REQ_BODY_NAME_APP] = HttpConstants.REQ_APP
            map[HttpConstants.REQ_BODY_NAME_DEVICE_DETAILS] = Build.MODEL
            UpdateTokenRequest().execute(HttpConstants.SERVICE_REQUEST_TOKEN_UPDATE, map, this)
        }else{
            context.setUserToken(userToken.table[0].tknNum)
            //context.setUserContactUpdated(userToken.table2[0].count)
            context.updateUI()
        }
    }

    override fun onSucceed(responseString: String?, requestName: String?) {}

    override fun onFailure(message: String?) {
        context.showErrorMessage(message)
    }
}