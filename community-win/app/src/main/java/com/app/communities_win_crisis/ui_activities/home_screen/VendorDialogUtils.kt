package com.app.communities_win_crisis.ui_activities.home_screen

import android.Manifest
import android.annotation.SuppressLint
import android.content.pm.PackageManager
import android.os.Build
import android.widget.*
import androidx.appcompat.app.AlertDialog
import androidx.recyclerview.widget.RecyclerView
import com.app.communities_win_crisis.R
import com.app.communities_win_crisis.network_interfacing.data_models.ProductListItem
import com.app.communities_win_crisis.network_interfacing.data_models.VendorProfile
import com.app.communities_win_crisis.network_interfacing.utils.HttpConstants
import com.app.communities_win_crisis.ui_activities.home_page_ui.main.ProductGridRecyclerAdapter
import com.google.android.gms.maps.model.LatLng
import java.util.*
import kotlin.collections.HashMap
import kotlin.collections.set


class VendorDialogUtils(var context: AppHomeActivity) {

    fun openVendorRegistrationDialog(vendorProfile: VendorProfile) {
        val address = LocationUtils(context).getAddressFromLocation(LatLng(context.userLatitude!!,
            context.userLongitude!!
        ))
        val dialogBuilder = AlertDialog.Builder(context)
        dialogBuilder.setView(context.layoutInflater.inflate(R.layout.dialog_vendor_name, null))
        val alertDialog = dialogBuilder.create()
        alertDialog.show()
        alertDialog.findViewById<TextView>(R.id.tv_vendor_address)!!.text = address[0].getAddressLine(0)
        if(!vendorProfile.vendorName.isNullOrEmpty())
            alertDialog.findViewById<EditText>(R.id.et_vendor_name)!!.setText(vendorProfile.vendorName)
        /*if(!vendorProfile?.vendorProducts.isNullOrEmpty()){
            var categories = ""
            vendorProfile?.vendorProducts?.forEach {
                categories += it.categoryName
            }
            if(categories.contains("Fruits"))
                alertDialog.findViewById<ToggleButton>(R.id.ib_businessCategoryFruits)!!.isChecked =true
            if(categories.contains("Vegetables"))
                alertDialog.findViewById<ToggleButton>(R.id.ib_businessCategoryVegetables)!!.isChecked =true
        }*/

        alertDialog.findViewById<Button>(R.id.btn_get_location)!!.setOnClickListener {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M && context.checkSelfPermission(
                    Manifest.permission.ACCESS_FINE_LOCATION
                ) != PackageManager.PERMISSION_GRANTED) {
                LocationUtils(context).startLocationUpdate()
            }else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                context.requestPermissions(
                    arrayOf(Manifest.permission.ACCESS_FINE_LOCATION),
                    AppHomeActivity.REQUEST_LOCATION_PERMISSION
                )
            }else if (Build.VERSION.SDK_INT < Build.VERSION_CODES.M) {
                LocationUtils(context).startLocationUpdate()
            }
            alertDialog.dismiss()
        }
        alertDialog.findViewById<Button>(R.id.btn_sav_vendor_basics)!!.setOnClickListener {
            if(alertDialog.findViewById<EditText>(R.id.et_vendor_name)!!.text.toString().isEmpty()){
                Toast.makeText(
                    context, context.getString(R.string.enter_vendor_name),
                    Toast.LENGTH_SHORT
                ).show()
                return@setOnClickListener
            }
            /*if(!alertDialog.findViewById<ToggleButton>(R.id.ib_businessCategoryFruits)!!.isChecked &&
                !alertDialog.findViewById<ToggleButton>(R.id.ib_businessCategoryVegetables)!!.isChecked){
                Toast.makeText(
                    context, context.getString(R.string.select_atleast_one_category),
                    Toast.LENGTH_SHORT
                ).show()
                return@setOnClickListener
            }*/

            /*var categories = ""
            if(alertDialog.findViewById<ToggleButton>(R.id.ib_businessCategoryFruits)!!.isChecked)
                categories += "Fruits,"
            if(alertDialog.findViewById<ToggleButton>(R.id.ib_businessCategoryVegetables)!!.isChecked)
                categories += "Vegetables"*/

            val map: HashMap<String, Any> = HashMap(4)
            map[HttpConstants.REQ_BODY_PHONE] = context.userContact.toString()
            map[HttpConstants.REQ_BODY_VENDOR_NAME]=alertDialog.findViewById<EditText>(R.id.et_vendor_name)!!.text.toString()
            //map[HttpConstants.REQ_BODY_VENDOR_CATEGORIES]= categories
            map[HttpConstants.REQ_BODY_VENDOR_CITY]=address[0].locality
            map[HttpConstants.REQ_BODY_VENDOR_STATE]=address[0].adminArea
            map[HttpConstants.REQ_BODY_VENDOR_COUNTRY]=address[0].countryName
            map[HttpConstants.REQ_BODY_VENDOR_PIN]=address[0].postalCode
            map[HttpConstants.REQ_BODY_VENDOR_LAT]=context.userLatitude!!
            map[HttpConstants.REQ_BODY_VENDOR_LNG]=context.userLongitude!!
            alertDialog.dismiss()
            context.mPresenter.updateVendorDetails(map)
        }
    }

    @SuppressLint("CutPasteId")
    fun openVendorPrecautionsDialog(vendorProfile: VendorProfile) {
        val dialogBuilder = AlertDialog.Builder(context)
        dialogBuilder.setView(context.layoutInflater.inflate(R.layout.dialog_vendor_precautions_add, null))
        val alertDialog = dialogBuilder.create()
        alertDialog.show()
        if(!vendorProfile.vendorName.isNullOrEmpty())
            alertDialog.findViewById<TextView>(R.id.tv_vendor_name)!!.text = vendorProfile.vendorName
        alertDialog.findViewById<CheckBox>(R.id.cb_social_distancing)!!.isChecked = vendorProfile.vendorSocialDistanced
        alertDialog.findViewById<CheckBox>(R.id.cb_fever_screening)!!.isChecked = vendorProfile.vendorFeverScreening
        alertDialog.findViewById<CheckBox>(R.id.cb_quarantine_check)!!.isChecked = vendorProfile.vendorStampCheck
        alertDialog.findViewById<CheckBox>(R.id.cb_hand_sanitizer)!!.isChecked = vendorProfile.vendorSanitizer
        alertDialog.findViewById<CheckBox>(R.id.cb_contact_less_pay)!!.isChecked = vendorProfile.vendorContactLessPay

        alertDialog.findViewById<Button>(R.id.btn_save_vendor_precautions)!!.setOnClickListener {
            if(!(alertDialog.findViewById<CheckBox>(R.id.cb_social_distancing)!!.isChecked) &&
                !(alertDialog.findViewById<CheckBox>(R.id.cb_fever_screening)!!.isChecked) &&
                !(alertDialog.findViewById<CheckBox>(R.id.cb_quarantine_check)!!.isChecked) &&
                !(alertDialog.findViewById<CheckBox>(R.id.cb_hand_sanitizer)!!.isChecked) &&
                !(alertDialog.findViewById<CheckBox>(R.id.cb_contact_less_pay)!!.isChecked)){
                Toast.makeText(context, context.getString(R.string.select_atleast_one_feature),
                    Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            val map: HashMap<String, Any> = HashMap(4)
            map[HttpConstants.REQ_BODY_VENDOR_SOCIAL_DISTANCE] = (alertDialog.findViewById<CheckBox>
                (R.id.cb_social_distancing))!!.isChecked
            map[HttpConstants.REQ_BODY_VENDOR_FEVER_SCREEN] = (alertDialog.findViewById<CheckBox>
                (R.id.cb_fever_screening))!!.isChecked
            map[HttpConstants.REQ_BODY_VENDOR_STAMP_CHECK] = (alertDialog.findViewById<CheckBox>
                (R.id.cb_quarantine_check))!!.isChecked
            map[HttpConstants.REQ_BODY_VENDOR_SANITIZER_USED] = (alertDialog.findViewById<CheckBox>
                (R.id.cb_hand_sanitizer))!!.isChecked
            map[HttpConstants.REQ_BODY_VENDOR_CONTACT_LESS_PAY] = (alertDialog.findViewById<CheckBox>
                (R.id.cb_contact_less_pay))!!.isChecked
            map[HttpConstants.REQ_BODY_PHONE] = context.userContact.toString()
            context.updateVendorPrecautions(map)
            alertDialog.dismiss()
        }
    }

    fun openVendorProductListDialog(vendorProfile: VendorProfile, productsList: ArrayList<ProductListItem>) {
        context.runOnUiThread {
            val dialogBuilder = AlertDialog.Builder(context)
            dialogBuilder.setView(
                context.layoutInflater.inflate(
                    R.layout.dialog_vendor_product_add,
                    null
                )
            )
            val alertDialog = dialogBuilder.create()
            alertDialog.show()
            val recyclerView: RecyclerView = alertDialog.findViewById(R.id.recycler_view)!!
            recyclerView.adapter =
                ProductGridRecyclerAdapter(
                    productsList,
                    vendorProfile.vendorProducts, context, alertDialog
                )
            val spinner: Spinner? = alertDialog.findViewById(R.id.spinner_unit)
            ArrayAdapter.createFromResource(
                context,
                R.array.weight_units,
                android.R.layout.simple_spinner_item
            ).also { adapter ->
                adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
                spinner!!.adapter = adapter
            }
            alertDialog.findViewById<Button>(R.id.btn_save_product_details)!!.setOnClickListener {
                if(alertDialog.findViewById<EditText>(R.id.et_selling_price)!!.text.toString().isEmpty()){
                    Toast.makeText(
                        context, context.getString(R.string.enter_item_selling_price),
                        Toast.LENGTH_SHORT
                    ).show()
                    return@setOnClickListener
                }
                if(alertDialog.findViewById<EditText>(R.id.et_itm_min_ord_qty)!!.text.toString().isEmpty()){
                    Toast.makeText(
                        context, context.getString(R.string.enter_item_min_qty),
                        Toast.LENGTH_SHORT
                    ).show()
                    return@setOnClickListener
                }
                if(spinner!!.selectedItem.toString() == "unit"){
                    Toast.makeText(
                        context, context.getString(R.string.select_correct_unit),
                        Toast.LENGTH_SHORT
                    ).show()
                    return@setOnClickListener
                }
                val map: HashMap<String, Any> = HashMap(6)
                map[HttpConstants.REQ_BODY_PHONE] = context.userContact.toString()
                map[HttpConstants.REQ_BODY_PRODUCT_NAME]=alertDialog.findViewById<TextView>(R.id.tv_product_name)!!.text.toString()
                map[HttpConstants.REQ_BODY_PRODUCT_CATEGORY]= productsList[0].category
                map[HttpConstants.REQ_BODY_PRICE]=alertDialog.findViewById<EditText>(R.id.et_selling_price)!!.text.toString()
                map[HttpConstants.REQ_BODY_MIN_ORDER_QTY]=alertDialog.findViewById<EditText>(R.id.et_itm_min_ord_qty)!!.text.toString()
                map[HttpConstants.REQ_BODY_UNIT]= spinner.selectedItem.toString()
                context.mPresenter.updateVendorProductPrices(map)
                alertDialog.dismiss()
            }
        }
    }
}