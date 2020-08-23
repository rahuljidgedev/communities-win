package com.app.communities_win_crisis.ui_activities.home_screen

import android.Manifest
import android.content.DialogInterface
import android.content.pm.PackageManager
import android.location.Address
import android.location.Geocoder
import android.os.Build
import android.widget.EditText
import androidx.appcompat.app.AlertDialog
import com.app.communities_win_crisis.R
import com.google.android.gms.maps.model.LatLng
import java.util.*

class AppHomePresenter(var context: AppHomeActivity, private var mapUtils: MapUtils) {

    fun showPinCodeDialog() {
        val  builder: AlertDialog = AlertDialog.Builder(context).create()
        builder.setTitle(context.getString(R.string.location_permission_title))
        builder.setMessage(context.getString(R.string.location_rationale_message))
        builder.setButton(DialogInterface.BUTTON_POSITIVE, context.getString(R.string.enable_permission),
            DialogInterface.OnClickListener { dialog, _ ->
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M && context.checkSelfPermission(
                        Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                    context.requestPermissions(arrayOf(Manifest.permission.ACCESS_FINE_LOCATION),
                        MapUtils.REQUEST_LOCATION_PERMISSION)
                    dialog.dismiss()
                }
            })
        builder.setButton(DialogInterface.BUTTON_NEUTRAL, context.getString(R.string.pin_code),
            DialogInterface.OnClickListener { dialog, _ ->
                    openPinCodeDialog()
                    dialog.dismiss()
                }
        )
        builder.setCancelable(false)
        builder.setCanceledOnTouchOutside(false)
        builder.show()
    }

    private fun openPinCodeDialog() {
        val  builder: AlertDialog = AlertDialog.Builder(context).create()
        val parentView = context.layoutInflater.inflate(R.layout.dialog_get_pincode, null)
        builder.setView(parentView)
        builder.setButton(DialogInterface.BUTTON_POSITIVE, context.getString(R.string.pin_code),
            DialogInterface.OnClickListener { dialog, _ ->
            val et = parentView.findViewById<EditText>(R.id.et_vendor_pinCode)
            val geoCoder = Geocoder(context, Locale("en","IN"))

            val pinCode = et.text.toString().trim()
            if (pinCode.isNotEmpty()){
                val address: MutableList<Address>? = geoCoder.getFromLocationName(pinCode, 5)
                if (address!= null){
                    val location: Address = address[0]
                    val pos = LatLng(location.latitude, location.longitude)
                    mapUtils.setMapPosition(pos)
                    dialog.dismiss()
                }
            }
        })
        builder.setButton(DialogInterface.BUTTON_NEGATIVE, context.getString(R.string.exit_app),
        DialogInterface.OnClickListener { dialog, _ ->
            dialog.dismiss()
            context.finishAffinity()
        }
        )
        builder.setCancelable(false)
        builder.setCanceledOnTouchOutside(false)
        builder.show()
    }
}