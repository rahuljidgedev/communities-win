package com.app.communities_win_crisis.ui_activities

import android.Manifest
import android.content.pm.PackageManager
import android.location.Address
import android.location.Geocoder
import android.os.Build
import android.os.Bundle
import android.os.Looper
import android.widget.Button
import android.widget.EditText
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import com.app.communities_win_crisis.R
import com.google.android.gms.location.*
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import java.util.*

class EssentialAround : AppCompatActivity(), OnMapReadyCallback {

    companion object {
        private const val REQUEST_LOCATION_PERMISSION = 1
    }

    private lateinit var mMap: GoogleMap

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_essential_around)
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        val mapFragment = supportFragmentManager
            .findFragmentById(R.id.map) as SupportMapFragment
        mapFragment.getMapAsync(this)
    }

    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    override fun onMapReady(googleMap: GoogleMap) {
        mMap = googleMap

        // Add a marker in India and move the camera
        val india = LatLng(20.5, 78.9)
        mMap.addMarker(MarkerOptions().position(india).title("India"))
        mMap.moveCamera(CameraUpdateFactory.newLatLng(india))
        mMap.animateCamera( CameraUpdateFactory.zoomTo(4.0f))
        mMap.uiSettings.isMapToolbarEnabled = false

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M && checkSelfPermission(
                Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            requestPermissions(arrayOf(Manifest.permission.ACCESS_FINE_LOCATION),
                REQUEST_LOCATION_PERMISSION
            )
            //callback onRequestPermissionsResult
        } else {
            startLocationUpdate()
        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (grantResults[0] != PackageManager.PERMISSION_GRANTED){
            showPinCodeDialog()
        }
    }

    private fun showPinCodeDialog() {
        val  builder: AlertDialog = AlertDialog.Builder(this).create()
        val parentView = layoutInflater.inflate(R.layout.dialog_get_pincode, null)
        builder.setView(parentView)
        val btn: Button = parentView.findViewById(R.id.btn_save_pincode)
        btn.setOnClickListener {
            val et = parentView.findViewById<EditText>(R.id.et_vendor_pinCode)
            val geoCoder = Geocoder(this, Locale("en","IN"))

            val pinCode = et.text.toString().trim()
            if (pinCode.isNotEmpty()){
                val address: MutableList<Address>? = geoCoder.getFromLocationName(pinCode, 5)
                if (address!= null){
                    val location: Address = address[0]
                    val pos = LatLng(location.latitude, location.longitude)
                    setMapPosition(pos)
                    builder.dismiss()
                }
            }
        }
        builder.show()
    }

    private fun setMapPosition(pos: LatLng) {
        mMap.clear()
        mMap.addMarker(MarkerOptions().position(pos)).title = "current location"
        mMap.moveCamera(CameraUpdateFactory.newLatLng(pos))
        mMap.animateCamera(CameraUpdateFactory.zoomTo(16.0f))
    }

    private fun startLocationUpdate(){
        val locationRequest = LocationRequest.create()
        locationRequest!!.run {
            priority = LocationRequest.PRIORITY_HIGH_ACCURACY
            interval = 10000
            setFastestInterval(2000)
        }

        val builder= LocationSettingsRequest.Builder()
        builder.addLocationRequest(locationRequest)
        val locationSettingRequest = builder.build()

        val settingsClient = LocationServices.getSettingsClient(this)
        settingsClient!!.checkLocationSettings(locationSettingRequest)

        val locationCallback = object: LocationCallback(){
            override fun onLocationResult(p0: LocationResult?) {
                super.onLocationResult(p0)
                val location = LatLng(p0!!.lastLocation.latitude, p0.lastLocation.longitude)
                setMapPosition(location)
            }
        }

        LocationServices.getFusedLocationProviderClient(this).requestLocationUpdates(locationRequest, locationCallback, Looper.myLooper())

    }

    fun openBrowseStoreScreen(){

    }

    fun openAddNeedsScreen() {
        //startActivity(Intent(this, AddNeeds::class.java))
    }
}