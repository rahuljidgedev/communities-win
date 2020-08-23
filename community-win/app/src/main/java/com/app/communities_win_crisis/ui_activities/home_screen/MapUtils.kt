package com.app.communities_win_crisis.ui_activities.home_screen

import android.Manifest
import android.content.pm.PackageManager
import android.os.Build
import android.os.Looper
import androidx.core.app.ActivityCompat
import com.app.communities_win_crisis.R
import com.google.android.gms.location.*
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions

class MapUtils(var context: AppHomeActivity) {
    private lateinit var mMap: GoogleMap

    companion object {
     const val REQUEST_LOCATION_PERMISSION = 1
    }

    fun setupMap(googleMap: GoogleMap) {
        mMap = googleMap
        if (context.userLatitude != 0.0 && context.userLongitude != 0.0){
            setMapPosition(LatLng(context.userLatitude!!, context.userLongitude!!))
        }else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M && context.checkSelfPermission(
                    Manifest.permission.ACCESS_FINE_LOCATION
                ) != PackageManager.PERMISSION_GRANTED) {
                context.requestPermissions(
                    arrayOf(Manifest.permission.ACCESS_FINE_LOCATION),
                    REQUEST_LOCATION_PERMISSION
                )
        } else {
            startLocationUpdate()
        }
    }

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

        val settingsClient = LocationServices.getSettingsClient(context)
        settingsClient!!.checkLocationSettings(locationSettingRequest)

        val locationCallback = object: LocationCallback(){
            override fun onLocationResult(p0: LocationResult?) {
                super.onLocationResult(p0)
                val location = LatLng(p0!!.lastLocation.latitude, p0.lastLocation.longitude)
                context.setUserLatitude(p0.lastLocation.latitude)
                context.setUserLongitude(p0.lastLocation.longitude)
                setMapPosition(location)
            }
        }

        if (ActivityCompat.checkSelfPermission(context, Manifest.permission.ACCESS_FINE_LOCATION)
            != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(context,
                Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED
        ) {
            return
        }
        LocationServices.getFusedLocationProviderClient(context)
            .requestLocationUpdates(locationRequest, locationCallback, Looper.myLooper())
    }

    fun setMapPosition(pos: LatLng) {
        mMap.clear()
        mMap.addMarker(MarkerOptions().position(pos)).title = context.getString(R.string.your_location)
        mMap.moveCamera(CameraUpdateFactory.newLatLng(pos))
        mMap.animateCamera(CameraUpdateFactory.zoomTo(16.0f))
    }
}