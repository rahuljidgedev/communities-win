package com.app.communities_win_crisis.ui_activities.home_screen

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import android.location.Address
import android.location.Geocoder
import android.os.Looper
import androidx.core.app.ActivityCompat
import com.google.android.gms.location.*
import com.google.android.gms.maps.model.LatLng
import java.util.*

class LocationUtils(context: Context) {
    var mContext = context
    var lHandler:LocationUpdater? = context as LocationUpdater

    fun startLocationUpdate(){
        val locationRequest = LocationRequest.create()
        locationRequest!!.run {
            priority = LocationRequest.PRIORITY_HIGH_ACCURACY
            interval = 10000
            setFastestInterval(2000)
        }

        val builder= LocationSettingsRequest.Builder()
        builder.addLocationRequest(locationRequest)
        val locationSettingRequest = builder.build()

        val settingsClient = LocationServices.getSettingsClient(mContext)
        settingsClient!!.checkLocationSettings(locationSettingRequest)

        val locationCallback = object: LocationCallback(){
            override fun onLocationResult(p0: LocationResult?) {
                super.onLocationResult(p0)
                val location = LatLng(p0!!.lastLocation.latitude, p0.lastLocation.longitude)
                lHandler!!.onLocationReceived(location)
                lHandler = null
            }
        }

        if (ActivityCompat.checkSelfPermission(mContext, Manifest.permission.ACCESS_FINE_LOCATION)
            == PackageManager.PERMISSION_DENIED && ActivityCompat.checkSelfPermission(mContext,
                Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_DENIED
        ) {
            return
        }
        LocationServices.getFusedLocationProviderClient(mContext)
            .requestLocationUpdates(locationRequest, locationCallback, Looper.myLooper())
    }

    fun getAddressFromLocation(location: LatLng): List<Address> {
        val geoCoder = Geocoder(mContext, Locale.getDefault())
        return geoCoder.getFromLocation(location.latitude, location.longitude,1)
    }

    interface LocationUpdater{
        fun onLocationReceived(
            location: LatLng
        )
    }
}