package com.app.communities_win_crisis.ui_activities.home_screen

import android.content.Intent
import android.content.pm.PackageManager
import android.os.Bundle
import android.view.View
import androidx.core.content.ContextCompat
import com.app.communities_win_crisis.R
import com.app.communities_win_crisis.ui_activities.VendorActivity
import com.app.communities_win_crisis.ui_activities.make_a_list_ui.MakeAListActivity
import com.app.communities_win_crisis.utils.BaseActivity
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.material.appbar.AppBarLayout
import com.google.android.material.bottomnavigation.BottomNavigationView

class AppHomeActivity : BaseActivity(), OnMapReadyCallback {

    private lateinit var bottomNavigation: BottomNavigationView
    private lateinit var mapUtils: MapUtils
    private lateinit var mPresenter: AppHomePresenter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_app_home)
        mapUtils = MapUtils(this)
        mPresenter = AppHomePresenter(this, mapUtils)
        setupBottomNavigation()

        val mapFragment = supportFragmentManager
            .findFragmentById(R.id.mapFragment) as SupportMapFragment
        mapFragment.getMapAsync(this)
    }

    private fun setupBottomNavigation() {
        bottomNavigation = findViewById(R.id.bottom_navigation)
        bottomNavigation.setOnNavigationItemSelectedListener { item ->
            when(item.itemId){
                R.id.menu_friends -> {
                    loadBuySection()
                    return@setOnNavigationItemSelectedListener true
                }
                R.id.menu_grocery -> {
                    loadSellSection()
                    return@setOnNavigationItemSelectedListener true
                }
                R.id.menu_others -> {
                    loadProfileSection()
                    return@setOnNavigationItemSelectedListener true
                }
            }
            false
        }
    }

    private fun loadBuySection() {
        setVisibilityOfView(R.id.buy_views, View.VISIBLE)
        setVisibilityOfView(R.id.sell_views, View.GONE)
        bottomNavigation.menu.getItem(0).isChecked = true
        findViewById<AppBarLayout>(R.id.app_bar).setBackgroundColor(
            ContextCompat.getColor(this, R.color.colorFriendsPrimary))
        bottomNavigation.setBackgroundColor(
            ContextCompat.getColor(this, R.color.colorFriendsPrimary))
        window.statusBarColor = ContextCompat.getColor(this, R.color.colorFriendsDark)
    }

    private fun loadSellSection() {
        setVisibilityOfView(R.id.buy_views, View.GONE)
        setVisibilityOfView(R.id.sell_views, View.VISIBLE)
        setVisibilityOfView(R.id.rl_unregistered_vendor, View.VISIBLE)
        setVisibilityOfView(R.id.rl_registered_vendor, View.GONE)
        bottomNavigation.menu.getItem(1).isChecked = true
        findViewById<AppBarLayout>(R.id.app_bar).setBackgroundColor(
            ContextCompat.getColor(this, R.color.colorGroceryPrimary))
        bottomNavigation.setBackgroundColor(
            ContextCompat.getColor(this,
            R.color.colorGroceryPrimary))
        window.statusBarColor = ContextCompat.getColor(this, R.color.colorGroceryDark)
    }

    private fun loadProfileSection() {
        setVisibilityOfView(R.id.buy_views, View.GONE)
        setVisibilityOfView(R.id.sell_views, View.VISIBLE)
        setVisibilityOfView(R.id.rl_unregistered_vendor, View.GONE)
        setVisibilityOfView(R.id.rl_registered_vendor, View.VISIBLE)
        bottomNavigation.menu.getItem(2).isChecked = true
        findViewById<AppBarLayout>(R.id.app_bar).setBackgroundColor(
            ContextCompat.getColor(this, R.color.colorGroceryPrimary))
        bottomNavigation.setBackgroundColor(
            ContextCompat.getColor(this,
            R.color.colorGroceryPrimary))
        window.statusBarColor = ContextCompat.getColor(this, R.color.colorGroceryDark)
    }

    override fun onMapReady(googleMap: GoogleMap) {
        mapUtils.setupMap(googleMap)
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (grantResults[0] == PackageManager.PERMISSION_GRANTED){
            mapUtils.startLocationUpdate()
        }else{
            mPresenter.showPinCodeDialog()
        }
    }

    fun clickedMakeAList(view: View) {
        startActivity(Intent(this, MakeAListActivity::class.java))
    }
    fun clickedBrowse(view: View) {}
    fun clickedOrder(view: View) {}

    private fun setVisibilityOfView(view: Int, visibility: Int) {
        findViewById<View>(view).visibility = visibility
    }

    fun openVendorScreen(view: View) {
        startActivity(Intent(this, VendorActivity::class.java))
    }

    fun clickedDemandCard(view: View) {}
    fun clickedOrdersCard(view: View) {}
    fun clickedPrecautionsCard(view: View) {}
    fun clickedTimingCard(view: View) {}
    fun clickedEditProfile(view: View) {}
}