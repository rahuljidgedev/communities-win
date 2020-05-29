package com.app.communities_win_crisis.ui_activities

import android.os.Bundle
import android.text.Html
import android.view.View
import android.widget.*
import androidx.appcompat.app.AlertDialog
import androidx.recyclerview.widget.RecyclerView
import com.app.communities_win_crisis.R
import com.app.communities_win_crisis.network_interfacing.data_models.ProductListItem
import com.app.communities_win_crisis.network_interfacing.data_models.VendorProfile
import com.app.communities_win_crisis.network_interfacing.utils.HttpConstants.Companion.REQ_BODY_MIN_ORDER_QTY
import com.app.communities_win_crisis.network_interfacing.utils.HttpConstants.Companion.REQ_BODY_PHONE
import com.app.communities_win_crisis.network_interfacing.utils.HttpConstants.Companion.REQ_BODY_PRICE
import com.app.communities_win_crisis.network_interfacing.utils.HttpConstants.Companion.REQ_BODY_PRODUCT_CATEGORY
import com.app.communities_win_crisis.network_interfacing.utils.HttpConstants.Companion.REQ_BODY_PRODUCT_NAME
import com.app.communities_win_crisis.network_interfacing.utils.HttpConstants.Companion.REQ_BODY_UNIT
import com.app.communities_win_crisis.network_interfacing.utils.HttpConstants.Companion.REQ_BODY_VENDOR_FEVER_SCREEN
import com.app.communities_win_crisis.network_interfacing.utils.HttpConstants.Companion.REQ_BODY_VENDOR_NAME
import com.app.communities_win_crisis.network_interfacing.utils.HttpConstants.Companion.REQ_BODY_VENDOR_PIN
import com.app.communities_win_crisis.network_interfacing.utils.HttpConstants.Companion.REQ_BODY_VENDOR_SANITIZER_USED
import com.app.communities_win_crisis.network_interfacing.utils.HttpConstants.Companion.REQ_BODY_VENDOR_SOCIAL_DISTANCE
import com.app.communities_win_crisis.network_interfacing.utils.HttpConstants.Companion.REQ_BODY_VENDOR_STAMP_CHECK
import com.app.communities_win_crisis.ui.main.ProductGridRecyclerAdapter
import com.app.communities_win_crisis.ui.main.presentor.VendorPresenter
import com.app.communities_win_crisis.utils.BaseActivity
import com.squareup.picasso.Picasso

class VendorActivity : BaseActivity(), ProductGridRecyclerAdapter.GridItemClickedListener {
    private var vPresenter: VendorPresenter? = null
    private var vendorProfile: VendorProfile? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_vendorprofil)
        vPresenter = VendorPresenter(this)
        supportActionBar?.title = Html.fromHtml("<font color='#FFFFFF'>"+
                getString(R.string.vendor_details_update)+"</font>")
        vPresenter!!.getVendorProfileIfExist()
    }

    fun openVendorBusinessNameDialog(view: View){
        val dialogBuilder = AlertDialog.Builder(this)
        dialogBuilder.setView(layoutInflater.inflate(R.layout.dialog_vendor_name, null))
        val alertDialog = dialogBuilder.create()
        alertDialog.show()
        if(vendorProfile!=null) {
            alertDialog.findViewById<EditText>(R.id.et_vendor_name)!!.setText(vendorProfile?.vendorName)
            alertDialog.findViewById<EditText>(R.id.et_vendor_pinCode)!!.setText(vendorProfile?.vendorPin)
            if(vendorProfile?.vendorProducts.toString().contains("Fruits"))
                alertDialog.findViewById<ToggleButton>(R.id.ib_businessCategoryFruits)!!.isChecked =true
            if(vendorProfile?.vendorProducts.toString().contains("Vegetables"))
                alertDialog.findViewById<ToggleButton>(R.id.ib_businessCategoryVegetables)!!.isChecked =true
        }
        alertDialog.findViewById<Button>(R.id.btn_sav_vendor_basics)!!.setOnClickListener {
            if(alertDialog.findViewById<EditText>(R.id.et_vendor_name)!!.text.toString().isEmpty()){
                Toast.makeText(this, getString(R.string.enter_vendor_name),
                    Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }
            if(!alertDialog.findViewById<ToggleButton>(R.id.ib_businessCategoryFruits)!!.isChecked &&
                !alertDialog.findViewById<ToggleButton>(R.id.ib_businessCategoryVegetables)!!.isChecked){
                Toast.makeText(this, getString(R.string.select_atleast_one_category),
                    Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            if(alertDialog.findViewById<EditText>(R.id.et_vendor_pinCode)!!.text.toString().isEmpty()){
                Toast.makeText(this, getString(R.string.enter_vendor_pincode),
                    Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            val map: HashMap<String, Any> = HashMap(3)
            map[REQ_BODY_PHONE] = userContact.toString()
            map[REQ_BODY_VENDOR_NAME]=alertDialog.findViewById<EditText>(R.id.et_vendor_name)!!.text.toString()
            map[REQ_BODY_VENDOR_PIN]=alertDialog.findViewById<EditText>(R.id.et_vendor_pinCode)!!.text.toString()
            alertDialog.dismiss()
            vPresenter!!.updateVendorDetails(map)
        }
    }

    fun openCityPinDialog(view: View){
        val dialogBuilder = AlertDialog.Builder(this)
        //TODO: Add correct layout here
        dialogBuilder.setView(this.layoutInflater.inflate(R.layout.dialog_vendor_name, null))
        val alertDialog = dialogBuilder.create()
        alertDialog.show()
    }

    fun openDemandsDialog(view: View){
        val dialogBuilder = AlertDialog.Builder(this)
        //TODO: Add correct layout here
        dialogBuilder.setView(this.layoutInflater.inflate(R.layout.dialog_vendor_name, null))
        val alertDialog = dialogBuilder.create()
        alertDialog.show()
    }

    fun openOrdersDialog(view: View){
        val dialogBuilder = AlertDialog.Builder(this)
        //TODO: Add correct layout here
        dialogBuilder.setView(this.layoutInflater.inflate(R.layout.dialog_vendor_name, null))
        val alertDialog = dialogBuilder.create()
        alertDialog.show()
    }

    fun openAddFruitsDialog(view: View){
        vPresenter!!.requestProductList("Fruits")
    }

    fun openAddVegetablesDialog(view: View){
        vPresenter!!.requestProductList("Vegetables")
    }

    fun showAddProductsDialog(
        productsList: ArrayList<ProductListItem>,
        productListType: String?
    ) {
        runOnUiThread {
            val dialogBuilder = AlertDialog.Builder(this)
            dialogBuilder.setView(layoutInflater.inflate(R.layout.dialog_vendor_product_add, null))
            val alertDialog = dialogBuilder.create()
            alertDialog.show()
            val recyclerView: RecyclerView = alertDialog.findViewById(R.id.recycler_view)!!
            recyclerView.adapter = ProductGridRecyclerAdapter(productsList, this, alertDialog)
            val spinner: Spinner? = alertDialog.findViewById(R.id.spinner_unit)
            ArrayAdapter.createFromResource(
                this,
                R.array.weight_units,
                android.R.layout.simple_spinner_item
            ).also {adapter ->
                adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
                spinner!!.adapter = adapter
            }
            alertDialog.findViewById<Button>(R.id.btn_save_product_details)?.setOnClickListener {
                if(alertDialog.findViewById<EditText>(R.id.et_selling_price)!!.text.toString().isEmpty()){
                    Toast.makeText(this, getString(R.string.enter_item_selling_price),
                        Toast.LENGTH_SHORT).show()
                    return@setOnClickListener
                }
                if(alertDialog.findViewById<EditText>(R.id.et_itm_min_ord_qty)!!.text.toString().isEmpty()){
                    Toast.makeText(this, getString(R.string.enter_item_min_qty),
                        Toast.LENGTH_SHORT).show()
                    return@setOnClickListener
                }
                if(spinner!!.selectedItem.toString() == "unit"){
                    Toast.makeText(this, getString(R.string.select_correct_unit),
                        Toast.LENGTH_SHORT).show()
                    return@setOnClickListener
                }
                val map: HashMap<String, Any> = HashMap(6)
                map[REQ_BODY_PHONE] = userContact.toString()
                map[REQ_BODY_PRODUCT_NAME]=alertDialog.findViewById<TextView>(R.id.tv_product_name)!!.text.toString()
                map[REQ_BODY_PRODUCT_CATEGORY]= productListType.toString()
                map[REQ_BODY_PRICE]=alertDialog.findViewById<EditText>(R.id.et_selling_price)!!.text.toString()
                map[REQ_BODY_MIN_ORDER_QTY]=alertDialog.findViewById<EditText>(R.id.et_itm_min_ord_qty)!!.text.toString()
                map[REQ_BODY_UNIT]= spinner.selectedItem.toString()
                vPresenter?.updateVendorProductPrices(map)
                alertDialog.dismiss()
            }
        }
    }

    fun openVendorPrecautionsDialog(view: View){
        val dialogBuilder = AlertDialog.Builder(this)
        dialogBuilder.setView(layoutInflater.inflate(R.layout.dialog_vendor_precautions_add, null))
        val alertDialog = dialogBuilder.create()
        alertDialog.show()
        if (vendorProfile!=null){
            alertDialog.findViewById<TextView>(R.id.tv_vendor_name)!!.text = vendorProfile?.vendorName
            if(vendorProfile?.vendorSocialDistanced!!)
                alertDialog.findViewById<CheckBox>(R.id.cb_social_distancing)!!.isChecked = true
            if(vendorProfile?.vendorFeverScreening!!)
                alertDialog.findViewById<CheckBox>(R.id.cb_fever_screening)!!.isChecked = true
            if(vendorProfile?.vendorStampCheck!!)
                alertDialog.findViewById<CheckBox>(R.id.cb_quarantine_check)!!.isChecked = true
            if(vendorProfile?.vendorSantizier!!)
                alertDialog.findViewById<CheckBox>(R.id.cb_hand_sanitizer)!!.isChecked = true
            /*if(vendorProfile?.cb_contact_less_pay!!)
                alertDialog.findViewById<CheckBox>(R.id.cb_hand_sanitizer)!!.isChecked = true*/
        }

        alertDialog.findViewById<Button>(R.id.btn_save_vendor_precautions)!!.setOnClickListener {
            if(!(alertDialog.findViewById<CheckBox>(R.id.cb_social_distancing)!!.isChecked) &&
            !(alertDialog.findViewById<CheckBox>(R.id.cb_fever_screening)!!.isChecked) &&
            !(alertDialog.findViewById<CheckBox>(R.id.cb_quarantine_check)!!.isChecked) &&
            !(alertDialog.findViewById<CheckBox>(R.id.cb_hand_sanitizer)!!.isChecked) &&
                !(alertDialog.findViewById<CheckBox>(R.id.cb_contact_less_pay)!!.isChecked)){
                Toast.makeText(this, getString(R.string.select_atleast_one_feature),
                    Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            val map: HashMap<String, Any> = HashMap(4)
            map[REQ_BODY_VENDOR_SOCIAL_DISTANCE] = (alertDialog.findViewById<CheckBox>
                (R.id.cb_social_distancing))!!.isChecked
            map[REQ_BODY_VENDOR_FEVER_SCREEN] = (alertDialog.findViewById<CheckBox>
                (R.id.cb_fever_screening))!!.isChecked
            map[REQ_BODY_VENDOR_STAMP_CHECK] = (alertDialog.findViewById<CheckBox>
                (R.id.cb_quarantine_check))!!.isChecked
            map[REQ_BODY_VENDOR_SANITIZER_USED] = (alertDialog.findViewById<CheckBox>
                (R.id.cb_hand_sanitizer))!!.isChecked
            /*map[REQ_BODY_VENDOR_STAMP_CHECK] = (alertDialog.findViewById<CheckBox>
                (R.id.cb_contact_less_pay))!!.isChecked*/
            map[REQ_BODY_PHONE] = userContact.toString()
            vPresenter!!.updateVendorPrecautions(map)
            alertDialog.dismiss()
        }
    }

    fun openVendorTimingDialog(view: View){
        val dialogBuilder = AlertDialog.Builder(this)
        dialogBuilder.setView(this.layoutInflater.inflate(R.layout.dialog_vendor_timing, null))
        val alertDialog = dialogBuilder.create()
        alertDialog.show()
    }

    fun showRequestStatus(message:String){
        runOnUiThread {
            Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
        }
    }

    fun updateVendorProfile(vendorProfile: VendorProfile) {
        this.vendorProfile = vendorProfile
    }

    override fun onGridItemSelected(item: ProductListItem, dialog: AlertDialog) {
        dialog.findViewById<TextView>(R.id.tv_product_name)!!.text = item.productName
        var iconId: Int =  R.drawable.vegetables
        if (item.category == "Fruits")
            iconId = R.drawable.fruits
        Picasso.get()
            .load(iconId)
            .error(R.drawable.ic_user)
            .placeholder(R.drawable.ic_user)
            .into(dialog.findViewById<ImageView>(R.id.iv_product_image))
    }
}

