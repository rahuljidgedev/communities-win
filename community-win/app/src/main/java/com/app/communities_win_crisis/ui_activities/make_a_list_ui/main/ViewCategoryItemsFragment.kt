package com.app.communities_win_crisis.ui_activities.make_a_list_ui.main

import android.content.Context
import android.os.Bundle
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TableLayout
import android.widget.TableRow
import android.widget.TextView
import androidx.fragment.app.Fragment
import com.app.communities_win_crisis.R
import com.app.communities_win_crisis.network_interfacing.data_models.CategoryItemList
import com.app.communities_win_crisis.network_interfacing.data_models.CategoryListItem
import com.google.gson.Gson
import com.google.gson.GsonBuilder


/**
 * A placeholder fragment containing a simple view.
 */
class ViewCategoryItemsFragment :  Fragment(),
    CategoryListAdapter.CreateListHandler {

    private var listener: CategoryListAdapter.CreateListHandler? = null
    private var groceryList: CategoryItemList? = null
    private var rootView: View? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        groceryList = GsonBuilder().create().fromJson(arguments.getString(CAT_ITEM_DATA), CategoryItemList::class.java)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val rootView = inflater.inflate(R.layout.fragment_view_list, container, false)
        this.rootView = rootView

        val tl = rootView?.findViewById(R.id.tl_list) as TableLayout
        if (groceryList== null || groceryList?.isEmpty()!!)
            return null

        val lp = TableRow.LayoutParams(
            TableRow.LayoutParams.MATCH_PARENT,
            TableRow.LayoutParams.WRAP_CONTENT
        )
        val strArr:MutableList<Int> = arrayListOf(R.string.items,R.string.qty,R.string.unit)
        lp.setMargins(16,4,16,4)
        val rowHeading = TableRow(context)
        rowHeading.gravity = Gravity.CENTER_HORIZONTAL
        for (i in 0..2){
            val tv = TextView(context,null,0,R.style.TextAppearance_AppCompat_Body1)
            tv.text = getString(strArr[i])
            tv.layoutParams = lp
            rowHeading.addView(tv)
        }
        tl.addView(rowHeading,lp)
        groceryList?.forEach {
            val dataRow = TableRow(context)
            dataRow.gravity = Gravity.CENTER_HORIZONTAL
            val itemName = TextView(context,null,0,R.style.TextAppearance_AppCompat_Caption)
            itemName.text = it.productName
            itemName.layoutParams = lp
            dataRow.addView(itemName)
            val qty = TextView(context,null,0,R.style.TextAppearance_AppCompat_Caption)
            qty.text = it.quantity.toString()
            qty.layoutParams = lp
            dataRow.addView(qty)
            val unit = TextView(context,null,0,R.style.TextAppearance_AppCompat_Caption)
            unit.text = it.unit
            unit.layoutParams = lp
            dataRow.addView(unit)
            tl.addView(dataRow,lp)
        }
        return rootView
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        if (context is CategoryListAdapter.CreateListHandler) {
            listener = context
        } else {
            //throw RuntimeException(context.toString() + " must implement onCategoryItemUpdated")
        }
    }

    override fun onDetach() {
        super.onDetach()
        listener = null
    }

    companion object {
        /**
         * The fragment argument representing the section number for this
         * fragment.
         */
        private const val ARG_SECTION_NUMBER = "section_number"
        private const val CAT_ITEM_DATA = "categoryItemData"

        /**
         * Returns a new instance of this fragment for the given section
         * number.
         */
        @JvmStatic
        fun newInstance(
            sectionNumber: Int,
            groceryList: CategoryItemList?
        ): ViewCategoryItemsFragment {
            return ViewCategoryItemsFragment().apply {
                arguments = Bundle().apply {
                    putInt(ARG_SECTION_NUMBER, sectionNumber)
                    putString(CAT_ITEM_DATA, Gson().toJson(groceryList))
                }
            }
        }
    }

    override fun onCategoryItemUpdated(item: CategoryListItem) {
        TODO("Not yet implemented")
    }
}