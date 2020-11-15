package com.app.communities_win_crisis.utils

import android.app.Activity
import android.content.DialogInterface
import android.os.Build
import android.view.View
import android.widget.EditText
import androidx.appcompat.app.AlertDialog
import com.app.communities_win_crisis.R
import com.app.communities_win_crisis.network_interfacing.utils.HttpConstants
import com.app.communities_win_crisis.network_interfacing.utils.UpdateTokenRequest
import com.app.communities_win_crisis.ui_activities.home_screen.AppHomeActivity
import com.app.communities_win_crisis.ui_activities.make_a_list_ui.MakeAListActivity
import com.google.android.gms.maps.model.LatLng
import com.hbb20.CountryCodePicker
import java.util.*

class LoginUtil(context: Activity) {
    var mContext = context
    fun signInTheUser(callback: Any, userLatLng: LatLng){
        val  builder: AlertDialog = AlertDialog.Builder(mContext).create()
        val parentView = mContext.layoutInflater.inflate(R.layout.dialog_login, null)
        val etMobileNumber = parentView.findViewById<EditText>(R.id.et_user_contact)
        val ccp: CountryCodePicker = parentView.findViewById(R.id.ccp)
        ccp.registerCarrierNumberEditText(etMobileNumber)
        builder.setTitle(mContext.getString(R.string.login))
        builder.setView(parentView)
        builder.setButton(
            DialogInterface.BUTTON_POSITIVE, mContext.getString(R.string.login),
            DialogInterface.OnClickListener { dialog, _ ->
                val userContact = etMobileNumber.text.toString().trim()
                if (userContact.isNotEmpty()){
                    if(mContext is MakeAListActivity)
                        (mContext as MakeAListActivity).setProgressVisibility(View.VISIBLE,
                            mContext.getString(R.string.registering_you))
                    else if(mContext is AppHomeActivity)
                        (mContext as AppHomeActivity).setProgressVisibility(View.VISIBLE,
                            mContext.getString(R.string.registering_you))
                    val map: HashMap<String, Any> = HashMap(3)
                    map[HttpConstants.REQ_BODY_NAME_CEL] = ccp.fullNumber
                    map[HttpConstants.REQ_BODY_NAME_APP] = HttpConstants.REQ_APP
                    map[HttpConstants.REQ_BODY_NAME_DEVICE_DETAILS] = Build.MODEL
                    if(userLatLng.latitude != 0.0 && userLatLng.longitude != 0.0) {
                        map[HttpConstants.REQ_BODY_USER_LAT] = userLatLng.latitude
                        map[HttpConstants.REQ_BODY_USER_LNG] = userLatLng.longitude
                    }
                    UpdateTokenRequest().execute(HttpConstants.SERVICE_REQUEST_TOKEN_UPDATE, map, callback)
                    dialog.dismiss()
                }
            }
        )
        builder.setButton(
            DialogInterface.BUTTON_NEGATIVE, mContext.getString(R.string.cancel),
            DialogInterface.OnClickListener { dialog, _ ->
                dialog.dismiss()
            }
        )
        builder.setCancelable(false)
        builder.setCanceledOnTouchOutside(false)
        builder.show()
    }
}