package com.app.communities_win_crisis.ui_activities.home_page_ui

import android.os.Build
import android.view.View
import com.app.communities_win_crisis.R
import com.app.communities_win_crisis.models.ContactInfo
import com.app.communities_win_crisis.network_interfacing.data_models.ActiveContactList
import com.app.communities_win_crisis.network_interfacing.data_models.UserToken
import com.app.communities_win_crisis.network_interfacing.interfaces.HttpResponseHandler
import com.app.communities_win_crisis.network_interfacing.utils.GetActiveConnectionRequest
import com.app.communities_win_crisis.network_interfacing.utils.HttpConstants.Companion.REQ_APP
import com.app.communities_win_crisis.network_interfacing.utils.HttpConstants.Companion.REQ_BODY_CONTACT_ALL
import com.app.communities_win_crisis.network_interfacing.utils.HttpConstants.Companion.REQ_BODY_NAME_APP
import com.app.communities_win_crisis.network_interfacing.utils.HttpConstants.Companion.REQ_BODY_NAME_CEL
import com.app.communities_win_crisis.network_interfacing.utils.HttpConstants.Companion.REQ_BODY_NAME_DEVICE_DETAILS
import com.app.communities_win_crisis.network_interfacing.utils.HttpConstants.Companion.REQ_BODY_TOKEN
import com.app.communities_win_crisis.network_interfacing.utils.HttpConstants.Companion.REQ_BODY_TYP
import com.app.communities_win_crisis.network_interfacing.utils.HttpConstants.Companion.REQ_BODY_VALUE_TYP
import com.app.communities_win_crisis.network_interfacing.utils.HttpConstants.Companion.SERVICE_REQUEST_ANONYMOUS_CONTACT_UPLOAD
import com.app.communities_win_crisis.network_interfacing.utils.HttpConstants.Companion.SERVICE_REQUEST_TOKEN_UPDATE
import com.app.communities_win_crisis.network_interfacing.utils.HttpConstants.Companion.SERVICE_REQUEST_USER_CONTACT_LIST
import com.app.communities_win_crisis.network_interfacing.utils.UpdateTokenRequest
import com.app.communities_win_crisis.network_interfacing.utils.UploadAnonymousContactRequest
import com.app.communities_win_crisis.utils.AppConstants
import com.app.communities_win_crisis.utils.AppConstants.Companion.USER_TYPE_CONNECTION
import com.app.communities_win_crisis.utils.ContactUtils
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import com.google.gson.Gson

class HomePresenter (var context: HomePageActivity): HttpResponseHandler {
    /*HTTP Request*/
    fun requestTokenTokenUpdate(contact: String) {
        context.setProgressVisibility(View.VISIBLE, context.getString(R.string.registering_you))
        val map: HashMap<String,String> = HashMap(3)
        val mobile: String = contact
        map[REQ_BODY_NAME_CEL] = mobile
        map[REQ_BODY_NAME_APP] = REQ_APP
        map[REQ_BODY_NAME_DEVICE_DETAILS] = Build.MODEL
        UpdateTokenRequest().execute(SERVICE_REQUEST_TOKEN_UPDATE, map, this)
    }

    fun requestContactUpload(){
        context.setProgressVisibility(View.VISIBLE, context.getString(R.string.please_wait))
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
        context.setProgressVisibility(View.VISIBLE, context.getString(R.string.please_wait))
        val map: HashMap<String, String> = HashMap(3)
        map[REQ_BODY_NAME_CEL] = context.userContact!!
        map[REQ_BODY_TOKEN] = context.userToken!!.toString()
        map[REQ_BODY_TYP] = REQ_BODY_VALUE_TYP
        GetActiveConnectionRequest().execute(SERVICE_REQUEST_USER_CONTACT_LIST, map, this)
    }
    /*-----------*/


    /*HTTP Request Responses*/
    override fun onSucceed(responseString: String?, contact: String?, requestName: String?) {
        when (requestName) {
            SERVICE_REQUEST_TOKEN_UPDATE -> {
                val userToken = Gson().fromJson(responseString, UserToken::class.java)
                context.setUserToken(userToken.table[0].tknNum)
                context.setUserContact(contact!!)
                context.setProgressVisibility(View.GONE, context.getString(R.string.please_wait))
                context.loadRegistrationUpdateUI()
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
                context.setProgressVisibility(View.VISIBLE, context.getString(R.string.please_wait))
            }
        }
    }

    override fun onSucceed(responseString: String?, requestName: String?) {}

    override fun onFailure(message: String?) {
        context.setProgressVisibility(View.VISIBLE, context.getString(R.string.please_wait))
        context.showErrorMessage(message)
    }
    /*-----------*/


    /*Grocery Map load*/
    fun loadMapFragment() {
        val mapFragment = context.supportFragmentManager.
                            findFragmentById(R.id.map) as SupportMapFragment?
        mapFragment.getMapAsync(callback)
    }

    /*Grocery Map load callback*/
    private val callback = OnMapReadyCallback { googleMap ->
        /**
         * Manipulates the map once available.
         * This callback is triggered when the map is ready to be used.
         * This is where we can add markers or lines, add listeners or move the camera.
         * In this case, we just add a marker near Sydney, Australia.
         * If Google Play services is not installed on the device, the user will be prompted to
         * install it inside the SupportMapFragment. This method will only be triggered once the
         * user has installed Google Play services and returned to the app.
         */
        /**
         * Manipulates the map once available.
         * This callback is triggered when the map is ready to be used.
         * This is where we can add markers or lines, add listeners or move the camera.
         * In this case, we just add a marker near Sydney, Australia.
         * If Google Play services is not installed on the device, the user will be prompted to
         * install it inside the SupportMapFragment. This method will only be triggered once the
         * user has installed Google Play services and returned to the app.
         */
        val sydney = LatLng(-34.0, 151.0)
        googleMap.addMarker(MarkerOptions().position(sydney).title("Marker in Sydney"))
        googleMap.moveCamera(CameraUpdateFactory.newLatLng(sydney))
    }
}