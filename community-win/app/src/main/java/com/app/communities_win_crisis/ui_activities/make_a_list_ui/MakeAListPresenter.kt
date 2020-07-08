package com.app.communities_win_crisis.ui_activities.make_a_list_ui

import android.view.View
import com.app.communities_win_crisis.R
import com.app.communities_win_crisis.network_interfacing.data_models.CategoryItemList
import com.app.communities_win_crisis.network_interfacing.data_models.CategoryListItem
import com.app.communities_win_crisis.network_interfacing.interfaces.HttpResponseHandler
import com.app.communities_win_crisis.network_interfacing.utils.GetVendorProductList
import com.app.communities_win_crisis.network_interfacing.utils.HttpConstants
import com.google.gson.Gson

class MakeAListPresenter(var context: MakeAListActivity): HttpResponseHandler {
    private var groceryList : CategoryItemList? = CategoryItemList()

    fun requestAvailableCategories() {
        context.setProgressVisibility(View.VISIBLE, context.getString(R.string.getting_available_products))
        GetVendorProductList().execute(HttpConstants.SERVICE_REQUEST_VENDOR_BASE_URL+
                HttpConstants.VENDOR_PRODUCTS_LIST, this)
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
        val categoryItemList = Gson().fromJson(responseString, CategoryItemList::class.java)
        context.loadLayoutsUI(categoryItemList)
        context.setProgressVisibility(View.GONE, context.getString(R.string.please_wait))
    }

    override fun onFailure(message: String?) {
        context.setProgressVisibility(View.GONE, context.getString(R.string.please_wait))
        context.showErrorMessage(message)
    }
}