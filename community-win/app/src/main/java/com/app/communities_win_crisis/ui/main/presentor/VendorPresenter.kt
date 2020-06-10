package com.app.communities_win_crisis.ui.main.presentor

import android.view.View
import com.app.communities_win_crisis.R
import com.app.communities_win_crisis.network_interfacing.data_models.ProductList
import com.app.communities_win_crisis.network_interfacing.data_models.VendorProfile
import com.app.communities_win_crisis.network_interfacing.interfaces.HttpResponseHandler
import com.app.communities_win_crisis.network_interfacing.utils.*
import com.app.communities_win_crisis.network_interfacing.utils.HttpConstants.Companion.REQ_BODY_PHONE_NUMBER
import com.app.communities_win_crisis.network_interfacing.utils.HttpConstants.Companion.REQ_BODY_PRODUCT_CATEGORY_NAME
import com.app.communities_win_crisis.ui_activities.VendorActivity
import com.google.gson.Gson
import java.util.*

class VendorPresenter (context: VendorActivity): HttpResponseHandler {
    var context: VendorActivity = context


    fun getVendorProfileIfExist() {
        context.setProgressVisibility(View.VISIBLE, context.getString(R.string.getting_vendor_details))
        val map: HashMap<String, Any> = HashMap(3)
        map[REQ_BODY_PHONE_NUMBER] = context.userContact.toString()
        GetVendor().execute(HttpConstants.SERVICE_REQUEST_VENDOR_BASE_URL +
                HttpConstants.GET_VENDOR, map, this)
    }

    fun updateVendorDetails(map: HashMap<String, Any>) {
        context.setProgressVisibility(View.VISIBLE, context.getString(R.string.updating_business_details))
        VendorRegistrationUpdate().execute(HttpConstants.SERVICE_REQUEST_VENDOR_BASE_URL +
                HttpConstants.REGISTER_ADD_UPDATE_VENDOR, map, this)
    }

    fun requestProductList(productListType: String) {
        context.setProgressVisibility(View.VISIBLE, context.getString(R.string.getting_available_products))
        val map: HashMap<String, Any> = HashMap(1)
        map[REQ_BODY_PRODUCT_CATEGORY_NAME] = productListType
        GetVendorProductList().execute(HttpConstants.SERVICE_REQUEST_VENDOR_BASE_URL +
                HttpConstants.VENDOR_PRODUCTS_LIST, map
            , this)
    }

    fun updateVendorProductPrices(map: HashMap<String, Any>) {
        context.setProgressVisibility(View.VISIBLE, context.getString(R.string.please_wait))
        VendorProductPrices().execute(HttpConstants.SERVICE_REQUEST_VENDOR_BASE_URL +
                HttpConstants.VENDOR_PRODUCTS_PRICES, map, this)
    }






    fun updateVendorPrecautions(map: HashMap<String, Any>) {
        context.setProgressVisibility(View.VISIBLE, context.getString(R.string.please_wait))
        GetVendorCoronaPrecautionsUpdate().execute(HttpConstants.SERVICE_REQUEST_VENDOR_BASE_URL +
             HttpConstants.VENDOR_CORONA_PRECAUTIONS, map, this)
    }

    override fun onSucceed(responseString: String?, contact: String?, requestName: String?) {}

    override fun onSucceed(responseString: String?, requestName: String?) {
        context.setProgressVisibility(View.GONE, "")
        when(requestName){
            HttpConstants.SERVICE_REQUEST_VENDOR_BASE_URL+HttpConstants.REGISTER_ADD_UPDATE_VENDOR->{
                context.showRequestStatus(context.getString(R.string.vendor_registered))
            }
            HttpConstants.SERVICE_REQUEST_VENDOR_BASE_URL+HttpConstants.VENDOR_PRODUCTS_PRICES ->{
                context.showRequestStatus(context.getString(R.string.update_product_price))
            }
            HttpConstants.SERVICE_REQUEST_VENDOR_BASE_URL +
                    HttpConstants.VENDOR_CORONA_PRECAUTIONS ->{
                context.showRequestStatus(context.getString(R.string.update_vendor_safety_feature))
            }
            HttpConstants.SERVICE_REQUEST_VENDOR_BASE_URL+HttpConstants.VENDOR_PRODUCTS_LIST -> {
                val productList = Gson().fromJson(responseString, ProductList::class.java)
                context.showAddProductsDialog(productList)
            }
            HttpConstants.SERVICE_REQUEST_VENDOR_BASE_URL+HttpConstants.GET_VENDOR -> {
                val vendorProfile = Gson().fromJson(responseString, VendorProfile::class.java)
                if(vendorProfile != null){
                    context.updateVendorProfile(vendorProfile)
                }else{
                    context.showRequestStatus(context.getString(R.string.no_vendor_data_available))
                }
            }
        }
    }

    override fun onFailure(message: String?) {
        context.showRequestStatus(message.toString())
    }
}