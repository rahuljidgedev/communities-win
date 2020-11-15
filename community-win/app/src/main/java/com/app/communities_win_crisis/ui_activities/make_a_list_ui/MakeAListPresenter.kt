package com.app.communities_win_crisis.ui_activities.make_a_list_ui

import android.view.View
import com.app.communities_win_crisis.R
import com.app.communities_win_crisis.network_interfacing.data_models.CategoryItemList
import com.app.communities_win_crisis.network_interfacing.data_models.CategoryListItem
import com.app.communities_win_crisis.network_interfacing.data_models.UserToken
import com.app.communities_win_crisis.network_interfacing.data_models.UserUploadListItem
import com.app.communities_win_crisis.network_interfacing.interfaces.HttpResponseHandler
import com.app.communities_win_crisis.network_interfacing.utils.GetVendorProductList
import com.app.communities_win_crisis.network_interfacing.utils.HttpConstants
import com.app.communities_win_crisis.network_interfacing.utils.UploadUserListByName
import com.app.communities_win_crisis.utils.LoginUtil
import com.google.android.gms.maps.model.LatLng
import com.google.gson.Gson
import java.text.SimpleDateFormat
import java.util.*

class MakeAListPresenter(var context: MakeAListActivity): HttpResponseHandler {
    private var groceryList : CategoryItemList? = CategoryItemList()

    private fun convertGroceryList(): List<UserUploadListItem> {
        val list: ArrayList<UserUploadListItem> = arrayListOf()
        groceryList!!.forEach {
            list.add(UserUploadListItem(it.productName, it.category, it.quantity, it.unit))
        }
        return list
    }

    fun updateUserGroceryList(item: CategoryListItem) {
        val pos = groceryList?.indexOfFirst {
            it.productName == item.productName
        }
        try {
            if (pos != null) {
                groceryList?.removeAt(pos)
            }
        }catch (e: ArrayIndexOutOfBoundsException){}
        groceryList?.add(item)
        context.updateTheList(groceryList)
    }

    fun showLoginDialog() {
        val userLatLng = LatLng(context.userLatitude!!, context.userLongitude!!)
        LoginUtil(context).signInTheUser(this, userLatLng)
        /*val builder: AlertDialog = AlertDialog.Builder(context).create()
        val parentView = context.layoutInflater.inflate(R.layout.dialog_login, null)
        val etMobileNumber = parentView.findViewById<EditText>(R.id.et_user_contact)
        val ccp: CountryCodePicker = parentView.findViewById(R.id.ccp)
        ccp.registerCarrierNumberEditText(etMobileNumber)
        builder.setTitle(context.getString(R.string.login))
        builder.setView(parentView)
        builder.setButton(
            DialogInterface.BUTTON_POSITIVE, context.getString(R.string.login),
            DialogInterface.OnClickListener { dialog, _ ->
                val userContact = etMobileNumber.text.toString().trim()
                if (userContact.isNotEmpty()){
                    requestTokenTokenUpdate(ccp.fullNumber)
                    dialog.dismiss()
                }
            }
        )
        builder.setButton(
            DialogInterface.BUTTON_NEGATIVE, context.getString(R.string.cancel),
            DialogInterface.OnClickListener { dialog, _ ->
                dialog.dismiss()
            }
        )
        builder.setCancelable(false)
        builder.setCanceledOnTouchOutside(false)
        builder.show()*/
    }

    /*--------------------HTTP Requests-------------------------*/
    /*private fun requestTokenTokenUpdate(contact: String) {
        context.setProgressVisibility(View.VISIBLE, context.getString(R.string.registering_you))
        val map: HashMap<String,String> = HashMap(3)
        val mobile: String = contact
        map[HttpConstants.REQ_BODY_NAME_CEL] = mobile
        map[HttpConstants.REQ_BODY_NAME_APP] = HttpConstants.REQ_APP
        map[HttpConstants.REQ_BODY_NAME_DEVICE_DETAILS] = Build.MODEL
        UpdateTokenRequest().execute(HttpConstants.SERVICE_REQUEST_TOKEN_UPDATE, map, this)
    }*/

    fun requestAvailableCategories() {
        context.setProgressVisibility(
            View.VISIBLE,
            context.getString(R.string.getting_available_products)
        )
        GetVendorProductList().execute(
            HttpConstants.SERVICE_REQUEST_VENDOR_BASE_URL +
                    HttpConstants.VENDOR_PRODUCTS_LIST, this
        )
    }

    fun requestUploadUserList() {
        context.setProgressVisibility(
            View.VISIBLE,
            context.getString(R.string.upload_the_user_list)
        )
        val map: HashMap<String, Any?> = HashMap(5)
        val date = Calendar.getInstance()
        map[HttpConstants.REQ_BODY_ORDER_NAME] = context.userContact.toString()+"_"+ date.time

        val simpleDateFormat = SimpleDateFormat("dd-mm-yyyy HH:mmaa")
        map[HttpConstants.REQ_BODY_CREATED_ON] = simpleDateFormat.format(date.time)
        date.add(Calendar.DATE, 1)
        map[HttpConstants.REQ_BODY_DELIVERY_BY] = simpleDateFormat.format(date.time)
        map[HttpConstants.REQ_BODY_USER] = context.userContact.toString()
        map[HttpConstants.REQ_BODY_ITEMS_LIST] = convertGroceryList()

        UploadUserListByName().execute(
            HttpConstants.SERVICE_REQUEST_USER_BASE_URL +
                    HttpConstants.SHOPPING_LIST_UPDATE, map, this
        )
    }
    /*----------------------------------------------------------*/

    /*--------------------HTTP Responses------------------------*/
    override fun onSucceed(responseString: String?, contact: String?, requestName: String?) {
        when(requestName) {
            HttpConstants.SERVICE_REQUEST_TOKEN_UPDATE -> {
                val userToken = Gson().fromJson(responseString, UserToken::class.java)
                context.setUserToken(userToken.table[0].tknNum)
                context.setUserContact(contact!!)
                context.setProgressVisibility(View.GONE, context.getString(R.string.please_wait))
                requestUploadUserList()
            }
        }
    }

    override fun onSucceed(responseString: String?, requestName: String?) {
        when(requestName){
            HttpConstants.SERVICE_REQUEST_VENDOR_BASE_URL +
                    HttpConstants.VENDOR_PRODUCTS_LIST -> {
                val categoryItemList = Gson().fromJson(responseString, CategoryItemList::class.java)
                context.loadLayoutsUI(categoryItemList)
                context.setProgressVisibility(View.GONE, context.getString(R.string.please_wait))
            }
            HttpConstants.SERVICE_REQUEST_USER_BASE_URL +
                    HttpConstants.SHOPPING_LIST_UPDATE -> {
                context.setProgressVisibility(View.GONE, context.getString(R.string.please_wait))
                context.showMessageToUser(context.getString(R.string.upload_the_list_response_success))
                context.finish()
            }
        }
    }

    override fun onFailure(message: String?) {
        context.setProgressVisibility(View.GONE, context.getString(R.string.please_wait))
        context.showMessageToUser(message)
    }
    /*----------------------------------------------------------*/
}