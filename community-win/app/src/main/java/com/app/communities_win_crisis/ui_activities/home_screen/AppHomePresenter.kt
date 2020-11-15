package com.app.communities_win_crisis.ui_activities.home_screen

import android.Manifest
import android.content.DialogInterface
import android.content.pm.PackageManager
import android.location.Address
import android.location.Geocoder
import android.os.Build
import android.view.View
import android.widget.EditText
import androidx.appcompat.app.AlertDialog
import com.app.communities_win_crisis.R
import com.app.communities_win_crisis.network_interfacing.data_models.ProductList
import com.app.communities_win_crisis.network_interfacing.data_models.ProductListItem
import com.app.communities_win_crisis.network_interfacing.data_models.UserToken
import com.app.communities_win_crisis.network_interfacing.data_models.VendorProfile
import com.app.communities_win_crisis.network_interfacing.interfaces.HttpResponseHandler
import com.app.communities_win_crisis.network_interfacing.utils.*
import com.app.communities_win_crisis.ui_activities.home_screen.AppHomeActivity.Companion.REQUEST_LOCATION_PERMISSION
import com.app.communities_win_crisis.ui_activities.home_screen.models.VendorListing
import com.app.communities_win_crisis.utils.AppConstants
import com.app.communities_win_crisis.utils.LoginUtil
import com.google.android.gms.maps.model.LatLng
import com.google.gson.Gson
import java.util.*

class AppHomePresenter(var context: AppHomeActivity) :
    HttpResponseHandler {
    var productListType = ""

    fun showLoginDialog() {
        val vendorLatLng = LatLng(context.userLatitude!!,context.userLongitude!!)
        LoginUtil(context).signInTheUser(this, vendorLatLng)
    }

    fun showLocationRequestDialog() {
        val  builder: AlertDialog = AlertDialog.Builder(context).create()
        builder.setTitle(context.getString(R.string.location_permission_title))
        builder.setMessage(context.getString(R.string.location_rationale_message))
        builder.setButton(DialogInterface.BUTTON_POSITIVE, context.getString(R.string.enable_permission),
            DialogInterface.OnClickListener { dialog, _ ->
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M && context.checkSelfPermission(
                        Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                    context.requestPermissions(arrayOf(Manifest.permission.ACCESS_FINE_LOCATION),
                        REQUEST_LOCATION_PERMISSION)
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
            if (pinCode.isNullOrEmpty()){
                return@OnClickListener
            }
            val address: MutableList<Address>? = geoCoder.getFromLocationName(pinCode, 5)
            if (address!= null) {
                val location: Address = address[0]
                val pos = LatLng(location.latitude, location.longitude)
                context.setUserLatitude(location.latitude)
                context.setUserLongitude(location.longitude)
                context.setMapPosition(pos)
                dialog.dismiss()
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


    /*-------------------Http Requests------------------------*/
    fun updateVendorDetails(map: HashMap<String, Any>) {
        context.setProgressVisibility(View.VISIBLE, context.getString(R.string.updating_business_details))
        VendorRegistrationUpdate().execute(
            HttpConstants.SERVICE_REQUEST_VENDOR_BASE_URL +
                HttpConstants.REGISTER_ADD_UPDATE_VENDOR, map, this)
    }

    fun updateVendorPrecautions(map: HashMap<String, Any>) {
        context.setProgressVisibility(View.VISIBLE, context.getString(R.string.please_wait))
        GetVendorCoronaPrecautionsUpdate().execute(HttpConstants.SERVICE_REQUEST_VENDOR_BASE_URL +
                HttpConstants.VENDOR_CORONA_PRECAUTIONS, map, this)
    }

    fun requestProductList(productListType: String) {
        context.setProgressVisibility(View.VISIBLE, context.getString(R.string.getting_available_products))
        this.productListType = productListType
        GetVendorProductList().execute(HttpConstants.SERVICE_REQUEST_VENDOR_BASE_URL +
                HttpConstants.VENDOR_PRODUCTS_LIST, this)
    }

    fun updateVendorProductPrices(map: HashMap<String, Any>) {
        context.setProgressVisibility(View.VISIBLE, context.getString(R.string.please_wait))
        VendorProductPrices().execute(HttpConstants.VENDOR_PRODUCTS_PRICES, map, this)
    }

    fun getVendorProfileIfExist() {
        context.setProgressVisibility(View.VISIBLE, context.getString(R.string.getting_vendor_details))
        val map: HashMap<String, Any> = HashMap(3)
        map[HttpConstants.REQ_BODY_PHONE_NUMBER] = context.userContact.toString()
        GetVendor().execute(HttpConstants.SERVICE_REQUEST_VENDOR_BASE_URL +
                HttpConstants.GET_VENDOR, map, this)

    }

    fun getNearByVendors(location: LatLng) {
        context.setProgressVisibility(View.VISIBLE, context.getString(R.string.loading_vendor_near_to_you))
        val vendorLatLng = LatLng(context.userLatitude!!,context.userLongitude!!)
        val map: HashMap<String, Any> = HashMap(3)
        map[HttpConstants.REQ_BODY_VENDOR_LAT] = context.userLatitude!!
        map[HttpConstants.REQ_BODY_VENDOR_LNG] = context.userLongitude!!
        map["Pin"] = 0
        GetVendorByLocation().execute(HttpConstants.SERVICE_REQUEST_VENDOR_BASE_URL +
                HttpConstants.GET_VENDOR_BY_LAT_LNG, map, this)
    }
    /*--------------------------------------------------------*/

    /*--------------------Http Responses------------------------------------*/
    override fun onSucceed(responseString: String?, contact: String?, requestName: String?) {
        when(requestName) {
            HttpConstants.SERVICE_REQUEST_TOKEN_UPDATE -> {
                val userToken = Gson().fromJson(responseString, UserToken::class.java)
                context.setUserToken(userToken.table[0].tknNum)
                context.setUserContact(contact!!)
                context.setProgressVisibility(View.GONE, context.getString(R.string.please_wait))
                context.showAndUpdateVendorProfile()
            }
        }
    }

    override fun onSucceed(responseString: String?, requestName: String?) {
        context.setProgressVisibility(View.GONE, "")
        when(requestName){
            HttpConstants.SERVICE_REQUEST_VENDOR_BASE_URL+HttpConstants.REGISTER_ADD_UPDATE_VENDOR->{
                context.showRequestStatus(context.getString(R.string.vendor_registered))
                getVendorProfileIfExist()
            }

            HttpConstants.SERVICE_REQUEST_VENDOR_BASE_URL+HttpConstants.VENDOR_CORONA_PRECAUTIONS ->{
                context.showRequestStatus(context.getString(R.string.update_vendor_safety_feature))
                getVendorProfileIfExist()
            }

            HttpConstants.SERVICE_REQUEST_VENDOR_BASE_URL+HttpConstants.VENDOR_PRODUCTS_LIST -> {
                val productList = Gson().fromJson(responseString, ProductList::class.java)
                if(this.productListType == AppConstants.GROCERY_VEGETABLES)
                    context.openAddProductsDialog(productList.filter {
                        it.category == AppConstants.GROCERY_VEGETABLES
                    } as ArrayList<ProductListItem>)
                else
                    context.openAddProductsDialog(productList.filter {
                        it.category == AppConstants.GROCERY_FRUITS
                    } as ArrayList<ProductListItem>)
            }

            HttpConstants.VENDOR_PRODUCTS_PRICES ->{
                context.showRequestStatus(context.getString(R.string.update_product_price))
                getVendorProfileIfExist()
            }

            HttpConstants.SERVICE_REQUEST_VENDOR_BASE_URL+HttpConstants.GET_VENDOR -> {
                val vendorProfile = Gson().fromJson(responseString, VendorProfile::class.java)
                if(vendorProfile != null){
                    context.updateVendorProfile(vendorProfile)
                }else{
                    context.showRequestStatus(context.getString(R.string.no_vendor_data_available))
                }
            }

            HttpConstants.SERVICE_REQUEST_VENDOR_BASE_URL+HttpConstants.GET_VENDOR_BY_LAT_LNG -> {
                val vendorProfile = Gson().fromJson(responseString, VendorListing::class.java)
                if(vendorProfile != null){
                    context.listVendorsOnTheMap(vendorProfile)
                }else{
                    context.showRequestStatus(context.getString(R.string.no_vendor_data_available))
                }
            }
        }
    }

    override fun onFailure(message: String?) {
        context.showRequestStatus(message.toString())
    }
    /*--------------------------------------------------------*/
}