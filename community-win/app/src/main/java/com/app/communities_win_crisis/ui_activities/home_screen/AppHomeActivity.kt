package com.app.communities_win_crisis.ui_activities.home_screen

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.view.View
import android.widget.ImageView
import android.widget.Spinner
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.core.content.ContextCompat
import com.app.communities_win_crisis.R
import com.app.communities_win_crisis.network_interfacing.data_models.ProductListItem
import com.app.communities_win_crisis.network_interfacing.data_models.VendorProduct
import com.app.communities_win_crisis.network_interfacing.data_models.VendorProfile
import com.app.communities_win_crisis.ui_activities.home_page_ui.main.ProductGridRecyclerAdapter
import com.app.communities_win_crisis.ui_activities.home_screen.models.VendorListing
import com.app.communities_win_crisis.ui_activities.make_a_list_ui.MakeAListActivity
import com.app.communities_win_crisis.utils.AppConstants
import com.app.communities_win_crisis.utils.BaseActivity
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import com.google.android.material.appbar.AppBarLayout
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.squareup.picasso.Picasso
import java.util.*

class AppHomeActivity : BaseActivity(), OnMapReadyCallback, LocationUtils.LocationUpdater,
    ProductGridRecyclerAdapter.GridItemClickedListener {

    companion object {
        const val REQUEST_LOCATION_PERMISSION = 1
    }

    private lateinit var bottomNavigation: BottomNavigationView
    private lateinit var vendorProfile: VendorProfile
    private lateinit var mVendorDialogUtils: VendorDialogUtils
    private lateinit var mMap: GoogleMap
    lateinit var mPresenter: AppHomePresenter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_app_home)
        findViewById<AppBarLayout>(R.id.app_bar).setBackgroundColor(
            ContextCompat.getColor(this, R.color.colorFriendsPrimary))
        window.statusBarColor = ContextCompat.getColor(this, R.color.colorFriendsDark)
        mPresenter = AppHomePresenter(this)
        mVendorDialogUtils = VendorDialogUtils(this)
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
        mPresenter.getVendorProfileIfExist()
        setVisibilityOfView(R.id.buy_views, View.GONE)
        setVisibilityOfView(R.id.sell_views, View.VISIBLE)
        if(userContact!!.isEmpty()) {
            setVisibilityOfView(R.id.rl_unregistered_vendor, View.VISIBLE)
            setVisibilityOfView(R.id.rl_registered_vendor, View.GONE)
        }else {
            setVisibilityOfView(R.id.rl_unregistered_vendor, View.GONE)
            setVisibilityOfView(R.id.rl_registered_vendor, View.VISIBLE)
        }
        bottomNavigation.menu.getItem(1).isChecked = true
        findViewById<AppBarLayout>(R.id.app_bar).setBackgroundColor(
            ContextCompat.getColor(this, R.color.colorGroceryPrimary))
        bottomNavigation.setBackgroundColor(
            ContextCompat.getColor(this,
            R.color.colorGroceryPrimary))
        window.statusBarColor = ContextCompat.getColor(this, R.color.colorGroceryDark)
    }

    private fun loadProfileSection() {
        //startActivity(Intent(this, VendorActivity::class.java))
        /*setVisibilityOfView(R.id.buy_views, View.GONE)
        setVisibilityOfView(R.id.sell_views, View.VISIBLE)
        setVisibilityOfView(R.id.rl_unregistered_vendor, View.GONE)
        setVisibilityOfView(R.id.rl_registered_vendor, View.VISIBLE)
        bottomNavigation.menu.getItem(2).isChecked = true
        findViewById<AppBarLayout>(R.id.app_bar).setBackgroundColor(
            ContextCompat.getColor(this, R.color.colorGroceryPrimary))
        bottomNavigation.setBackgroundColor(
            ContextCompat.getColor(this,
            R.color.colorGroceryPrimary))
        window.statusBarColor = ContextCompat.getColor(this, R.color.colorGroceryDark)*/
    }

    override fun onMapReady(googleMap: GoogleMap) {
        mMap = googleMap
        if (userLatitude != 0.0 && userLongitude != 0.0){
            setMapPosition(LatLng(userLatitude!!, userLongitude!!))
        }else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M && checkSelfPermission(
                Manifest.permission.ACCESS_FINE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED) {
            mPresenter.showLocationRequestDialog()
        } else {
            LocationUtils(this).startLocationUpdate()
        }
    }

    override fun onLocationReceived(location: LatLng) {
        runOnUiThread {
            setUserLatitude(location.latitude)
            setUserLongitude(location.longitude)
            setMapPosition(location)
        }
        //setMapPosition(location)
    }

    fun setMapPosition(pos: LatLng) {
        mMap.clear()
        mMap.addMarker(MarkerOptions().position(pos)).title = getString(R.string.your_location)
        mMap.moveCamera(CameraUpdateFactory.newLatLng(pos))
        mMap.animateCamera(CameraUpdateFactory.zoomTo(16.0f))
        mPresenter.getNearByVendors(pos)
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (grantResults[0] == PackageManager.PERMISSION_GRANTED){
            LocationUtils(this).startLocationUpdate()
        }else{
            mPresenter.showLocationRequestDialog()
        }
    }

    fun clickedMakeAList(view: View) {
        startActivity(Intent(this, MakeAListActivity::class.java))
    }

    fun clickedBrowse(view: View) {}
    fun clickedOrder(view: View) {}


    /*-------------------------Vendor Profile functionality----------------------------*/

    fun loginTheVendor(view: View) {
        mPresenter.showLoginDialog()
    }

    fun showAndUpdateVendorProfile() {
        runOnUiThread {
            loadSellSection()
        }
    }

    fun clickedEditProfile(view: View) {
        try {
            mVendorDialogUtils.openVendorRegistrationDialog(vendorProfile)
        }catch (e: java.lang.Exception){
            mVendorDialogUtils.openVendorRegistrationDialog(VendorProfile("",false,"","", List<VendorProduct>(1){VendorProduct(0.0,0.0,"","","")},false,false,false,false,"","","",""))
        }
    }

    fun clickedDemandCard(view: View) {}
    fun clickedOrdersCard(view: View) {}

    fun clickedPrecautionsCard(view: View) {
        try {
            mVendorDialogUtils.openVendorPrecautionsDialog(vendorProfile)
        }catch (e: java.lang.Exception){
            mVendorDialogUtils.openVendorPrecautionsDialog(VendorProfile("",false,"","", List<VendorProduct>(1){VendorProduct(0.0,0.0,"","","")},false,false,false,false,"","","",""))
        }
    }

    fun clickedTimingCard(view: View) {}

    fun clickedAddVegetablesCard(view: View) {
        mPresenter.requestProductList(AppConstants.GROCERY_VEGETABLES)
    }
    fun clickedAddFruitsCard(view: View) {
        mPresenter.requestProductList(AppConstants.GROCERY_FRUITS)
    }

    fun updateVendorProfile(vendorProfile: VendorProfile) {
        if(userContact!!.isNotEmpty()){
            this.vendorProfile = vendorProfile
            runOnUiThread {
                findViewById<TextView>(R.id.tv_vendor_name).text = vendorProfile.vendorName
                var address = ""
                if(!vendorProfile.vendorCity.isNullOrEmpty())
                    address = vendorProfile.vendorCity+", "
                if(!vendorProfile.vendorState.isNullOrEmpty())
                    address += vendorProfile.vendorState+", "
                if(!vendorProfile.vendorCountry.isNullOrEmpty())
                    address += vendorProfile.vendorCountry+", "
                if(!vendorProfile.vendorPin.isNullOrEmpty())
                    address += vendorProfile.vendorPin
                findViewById<TextView>(R.id.tv_vendor_address).text = address

                if(!vendorProfile.vendorProducts.isNullOrEmpty()) {
                    var size = (vendorProfile.vendorProducts.filter {
                        it.categoryName == AppConstants.GROCERY_VEGETABLES
                    }).size
                    findViewById<TextView>(R.id.tv_vegetables).text =
                        if (size == 0) "0" else size.toString()
                    size = (vendorProfile.vendorProducts.filter {
                        it.categoryName == AppConstants.GROCERY_FRUITS
                    }).size
                    findViewById<TextView>(R.id.tv_fruits).text =
                        if (size == 0) "0" else size.toString()
                }
            }
        }
    }

    fun updateVendorPrecautions(map: HashMap<String, Any>) {
        mPresenter.updateVendorPrecautions(map)
    }

    fun openAddProductsDialog(productsList: ArrayList<ProductListItem>) {
        mVendorDialogUtils.openVendorProductListDialog(vendorProfile, productsList)
    }

    override fun onGridProductSelected(item: ProductListItem, dialog: AlertDialog) {
        dialog.findViewById<TextView>(R.id.tv_product_name)!!.text = item.productName
        val failureImage = if(item.category == AppConstants.GROCERY_VEGETABLES)
            R.drawable.veg_not_available
        else
            R.drawable.fruit_not_available
        Picasso.get()
            .load(Uri.parse(item.imageUrl))
            .error(failureImage)
            .placeholder(failureImage)
            .into(dialog.findViewById<ImageView>(R.id.iv_product_image))
    }

    override fun onGridExistingProductSelected(
        item: VendorProduct,
        itemUrl: String,
        dialog: AlertDialog
    ) {
        dialog.findViewById<TextView>(R.id.tv_product_name)!!.text = item.productName
        dialog.findViewById<TextView>(R.id.et_selling_price)!!.text = item.price.toString()
        dialog.findViewById<TextView>(R.id.et_itm_min_ord_qty)!!.text = item.minOrderQuantity.toString()
        val pos: Int = resources.getStringArray(R.array.weight_units).toMutableList().indexOf(item.units)
        dialog.findViewById<Spinner>(R.id.spinner_unit)!!.setSelection(pos)
        val failureImage = if(item.categoryName == AppConstants.GROCERY_VEGETABLES)
            R.drawable.veg_not_available
        else
            R.drawable.fruit_not_available
        try{
            Picasso.get()
                .load(Uri.parse(itemUrl))
                .error(failureImage)
                .placeholder(failureImage)
                .into(dialog.findViewById<ImageView>(R.id.iv_product_image))
        }catch(e: Exception){}
    }

    /*--------------------------------------------------------------*/


    fun showRequestStatus(message:String){
        runOnUiThread {
            Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
        }
    }

    private fun setVisibilityOfView(view: Int, visibility: Int) {
        findViewById<View>(view).visibility = visibility
    }

    fun listVendorsOnTheMap(vendorProfile: VendorListing?) {
        runOnUiThread {
            vendorProfile!!.Vendor.forEach {
                mMap
                    .addMarker(MarkerOptions()
                        .position(LatLng(it.Latitude, it.Longitude))
                        /*.icon(BitmapDescriptorFactory.fromResource(R.drawable.ic_grocery))*/)
                    .title = it.VendorName
            }
        }
    }
}