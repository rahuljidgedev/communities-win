package com.app.community_win_crisis.network_interfacing.utils

import android.os.AsyncTask
import com.app.community_win_crisis.models.ContactInfo
import com.app.community_win_crisis.network_interfacing.data_models.PushedContact
import com.app.community_win_crisis.network_interfacing.interfaces.HttpResponseHandler
import com.app.community_win_crisis.ui.main.presentor.HomePresenter
import com.app.community_win_crisis.ui.main.presentor.SplashPresenter
import com.google.gson.Gson

@Suppress("UNCHECKED_CAST")
class GetTokenRequest : AsyncTask<Any, Any, Any>() {

    override fun doInBackground(vararg params: Any?) {
        HttpRequestsUtils.httpGetTokenStatusRequest(params[0] as String,
            params[1] as HashMap<String, String>, params[2] as SplashPresenter)
        return
    }
}

@Suppress("UNCHECKED_CAST")
class UpdateTokenRequest : AsyncTask<Any, Any, Any>() {

    override fun doInBackground(vararg params: Any?) {
        HttpRequestsUtils.httpTokenUpdateRequest(params[0] as String,
            params[1] as HashMap<String, String>, params[2] as Any)
        return
    }
}

@Suppress("UNCHECKED_CAST")
class UploadAnonymousContactRequest(var contacts: MutableList<ContactInfo>,
                                    var serviceUrl: String,
                                    var map: HashMap<String, String>,
                                    var homePresenter: HomePresenter
) : AsyncTask<Any, Any, Any>(),
    HttpResponseHandler {

    override fun doInBackground(vararg params: Any?) {
        val csvContact: String = prepareContacts()
        if (csvContact.isEmpty())
            return
        map[HttpConstants.REQ_BODY_CONTACT_ALL] = csvContact.removeRange(csvContact.length-1, csvContact.length)

        HttpRequestsUtils.httpUserAnonymousContactUploadRequest(serviceUrl, map, this)
        return
    }

    private fun prepareContacts(): String {
        var nextContacts = ""

        if (contacts.size > 50){
            for (x in 0..49){
                nextContacts += "${contacts[x].number},"
            }
        }else if(contacts.size < 50 && contacts.isNotEmpty()){
            for (contact in contacts){
                nextContacts += "${contact.number},"
            }
        }
        return nextContacts.replace("[","").replace("]","")
    }

    override fun onSucceed(responseString: String?, contact: String?, requestName: String?) {
        val pushedContact = Gson().fromJson(responseString,
            PushedContact::class.java)

        /*Removed uploaded contacts from list*/
        var x = -1
        while(++x < pushedContact.pushedContactTable[0].rwc)
            contacts.removeAt(0)

        if (contacts.isNotEmpty()){
            //prepareContacts()
            doInBackground()
        }else{
            homePresenter.requestActiveContact()
        }
    }

    override fun onFailure(message: String?) {
        homePresenter.onFailure(message)
    }
}

@Suppress("UNCHECKED_CAST")
class GetActiveConnectionRequest : AsyncTask<Any, Any, Any>() {

    override fun doInBackground(vararg params: Any?) {
        HttpRequestsUtils.httpUserActiveConnectionListRequest(params[0] as String,
            params[1] as HashMap<String, String>, params[2] as Any)
        return
    }
}