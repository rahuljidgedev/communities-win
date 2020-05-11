package com.app.community_win_crisis.ui.main.presentor

import android.os.Build
import android.view.View
import com.app.community_win_crisis.models.ContactInfo
import com.app.community_win_crisis.network_interfacing.data_models.ActiveContactList
import com.app.community_win_crisis.network_interfacing.data_models.UserToken
import com.app.community_win_crisis.network_interfacing.interfaces.HttpResponseHandler
import com.app.community_win_crisis.network_interfacing.utils.GetActiveConnectionRequest
import com.app.community_win_crisis.network_interfacing.utils.HttpConstants.Companion.REQ_APP
import com.app.community_win_crisis.network_interfacing.utils.HttpConstants.Companion.REQ_BODY_CONTACT_ALL
import com.app.community_win_crisis.network_interfacing.utils.HttpConstants.Companion.REQ_BODY_NAME_APP
import com.app.community_win_crisis.network_interfacing.utils.HttpConstants.Companion.REQ_BODY_NAME_CEL
import com.app.community_win_crisis.network_interfacing.utils.HttpConstants.Companion.REQ_BODY_NAME_DEVICE_DETAILS
import com.app.community_win_crisis.network_interfacing.utils.HttpConstants.Companion.REQ_BODY_TOKEN
import com.app.community_win_crisis.network_interfacing.utils.HttpConstants.Companion.REQ_BODY_TYP
import com.app.community_win_crisis.network_interfacing.utils.HttpConstants.Companion.REQ_BODY_VALUE_TYP
import com.app.community_win_crisis.network_interfacing.utils.HttpConstants.Companion.SERVICE_REQUEST_ANONYMOUS_CONTACT_UPLOAD
import com.app.community_win_crisis.network_interfacing.utils.HttpConstants.Companion.SERVICE_REQUEST_TOKEN_UPDATE
import com.app.community_win_crisis.network_interfacing.utils.HttpConstants.Companion.SERVICE_REQUEST_USER_CONTACT_LIST
import com.app.community_win_crisis.network_interfacing.utils.UpdateTokenRequest
import com.app.community_win_crisis.network_interfacing.utils.UploadAnonymousContactRequest
import com.app.community_win_crisis.ui_activities.HomePageActivity
import com.app.community_win_crisis.utils.AppConstants
import com.app.community_win_crisis.utils.AppConstants.Companion.USER_TYPE_CONNECTION
import com.app.community_win_crisis.utils.ContactUtils
import com.google.gson.Gson

class HomePresenter (context: HomePageActivity): HttpResponseHandler {
    var context: HomePageActivity = context

    fun requestTokenTokenUpdate(contact: String) {
        context.setProgressVisibility(View.VISIBLE)
        val map: HashMap<String,String> = HashMap(3)
        val mobile: String = contact
        map[REQ_BODY_NAME_CEL] = mobile
        map[REQ_BODY_NAME_APP] = REQ_APP
        map[REQ_BODY_NAME_DEVICE_DETAILS] = Build.MODEL
        UpdateTokenRequest().execute(SERVICE_REQUEST_TOKEN_UPDATE, map, this)
    }

    fun requestContactUpload(){
        context.setProgressVisibility(View.VISIBLE)
        val map: HashMap<String,String> = HashMap(3)
        map[REQ_BODY_NAME_CEL] = context.userContact!!
        map[REQ_BODY_NAME_APP] = REQ_APP
        map[REQ_BODY_CONTACT_ALL] = ""
        val contacts: MutableList<ContactInfo> = ContactUtils.getContacts(context)
        UploadAnonymousContactRequest(
            contacts,
            SERVICE_REQUEST_ANONYMOUS_CONTACT_UPLOAD,
            map,
            this)
            .execute()
    }

    fun requestActiveContact() {
        context.setProgressVisibility(View.VISIBLE)
        val map: HashMap<String, String> = HashMap(3)
        map[REQ_BODY_NAME_CEL] = context.userContact!!
        map[REQ_BODY_TOKEN] = context.userToken!!.toString()
        map[REQ_BODY_TYP] = REQ_BODY_VALUE_TYP
        GetActiveConnectionRequest().execute(SERVICE_REQUEST_USER_CONTACT_LIST, map, this)
    }



    override fun onSucceed(responseString: String?, contact: String?, requestName: String?) {
        when (requestName) {
            SERVICE_REQUEST_TOKEN_UPDATE -> {
                val userToken = Gson().fromJson(responseString, UserToken::class.java)
                context.setUserToken(userToken.table[0].tknNum)
                context.setUserContact(contact!!)
                context.loadRegistrationUpdateUI()
                context.setProgressVisibility(View.GONE)
            }
            SERVICE_REQUEST_USER_CONTACT_LIST -> {
                    val contactActiveList = Gson().fromJson(responseString, ActiveContactList::class.java)
                    val contactInfo: List<ContactInfo> = ContactUtils.getContacts(context)

                    if(contactActiveList != null) {
                        for (x in contactInfo.indices) {
                            for (y in contactActiveList.activeTable.indices) {
                                if (contactInfo[x].number.toString()
                                        .contains(contactActiveList.activeTable[y].toString())
                                ) {
                                    contactInfo[x].lastAct = contactActiveList.activeTable[y].prvAct
                                    contactInfo[x].userType = USER_TYPE_CONNECTION
                                    break
                                }
                            }
                        }
                        context.loadConnectionSafeConnectionList(contactInfo, AppConstants.FRIENDS_VIEW_CONNECTION_LIST)
                    }
                    context.loadConnectionSafeConnectionList(contactInfo, AppConstants.FRIENDS_VIEW_INVITE_LIST)
                context.setProgressVisibility(View.GONE)
            }
        }
    }

    override fun onFailure(message: String?) {
        context.setProgressVisibility(View.GONE)
        context.showErrorMessage(message)
    }
}