package com.app.communities_win_crisis.ui_activities.make_a_list_ui

import android.view.View
import com.app.communities_win_crisis.R
import com.app.communities_win_crisis.network_interfacing.data_models.CategoryItemList
import com.app.communities_win_crisis.network_interfacing.data_models.CategoryListItem
import com.app.communities_win_crisis.network_interfacing.data_models.UserUploadListItem
import com.app.communities_win_crisis.network_interfacing.interfaces.HttpResponseHandler
import com.app.communities_win_crisis.network_interfacing.utils.GetVendorProductList
import com.app.communities_win_crisis.network_interfacing.utils.HttpConstants
import com.app.communities_win_crisis.network_interfacing.utils.UploadUserListByName
import com.google.gson.Gson
import java.util.*

class MakeAListPresenter(var context: MakeAListActivity): HttpResponseHandler {
    private var groceryList : CategoryItemList? = CategoryItemList()

    fun requestAvailableCategories() {
        context.setProgressVisibility(View.VISIBLE, context.getString(R.string.getting_available_products))
        GetVendorProductList().execute(HttpConstants.SERVICE_REQUEST_VENDOR_BASE_URL+
                HttpConstants.VENDOR_PRODUCTS_LIST, this)
    }

    fun requestUploadUserList() {
        context.setProgressVisibility(View.VISIBLE, context.getString(R.string.upload_the_user_list))
        val map: HashMap<String, Any?> = HashMap(5)
        val date = Calendar.getInstance()
        map[HttpConstants.REQ_BODY_ORDER_NAME] = context.userContact.toString()+"_"+ date.time
        map[HttpConstants.REQ_BODY_CREATED_ON] = date.time
        date.set(Calendar.DATE, 1)
        map[HttpConstants.REQ_BODY_DELIVERY_BY] = date.time
        map[HttpConstants.REQ_BODY_USER] = context.userContact.toString()
        map[HttpConstants.REQ_BODY_ITEMS_LIST] = convertGroceryList()

        UploadUserListByName().execute(HttpConstants.SERVICE_REQUEST_USER_BASE_URL+
                HttpConstants.SHOPPING_LIST_UPDATE, map, this)
    }

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


    override fun onSucceed(responseString: String?, contact: String?, requestName: String?) {}

    override fun onSucceed(responseString: String?, requestName: String?) {
        when(requestName){
            HttpConstants.SERVICE_REQUEST_VENDOR_BASE_URL+
                    HttpConstants.VENDOR_PRODUCTS_LIST ->{
                val categoryItemList = Gson().fromJson(responseString, CategoryItemList::class.java)
                context.loadLayoutsUI(categoryItemList)
                context.setProgressVisibility(View.GONE, context.getString(R.string.please_wait))
            }
            HttpConstants.SERVICE_REQUEST_USER_BASE_URL+
                    HttpConstants.SHOPPING_LIST_UPDATE ->{
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
}