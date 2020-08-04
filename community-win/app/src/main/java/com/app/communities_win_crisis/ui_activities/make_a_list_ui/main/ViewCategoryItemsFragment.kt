package com.app.communities_win_crisis.ui_activities.make_a_list_ui.main

import android.content.Context
import android.os.Bundle
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import com.app.communities_win_crisis.R
import com.app.communities_win_crisis.ui_activities.make_a_list_ui.MakeAListActivity.Companion.groceryListObservable
import com.google.android.material.floatingactionbutton.FloatingActionButton


/**
 * A placeholder fragment containing a simple view.
 */
class ViewCategoryItemsFragment :  Fragment() {
    private var listener: UserListUpload? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val rootView = inflater.inflate(R.layout.fragment_view_list, container, false)
        val parent = rootView?.findViewById(R.id.table_ll) as LinearLayout

        if (groceryListObservable.value == null || groceryListObservable.value!!.size == 0)
            return null
        parent.removeAllViews()
        val lp = LinearLayout.LayoutParams(
            LinearLayout.LayoutParams.MATCH_PARENT,
            LinearLayout.LayoutParams.WRAP_CONTENT
        )
        val tvLp = LinearLayout.LayoutParams(
            150,
            LinearLayout.LayoutParams.WRAP_CONTENT, 3f
        )
        tvLp.setMargins(8,16,8,16)
        val tvLp2 = LinearLayout.LayoutParams(
            30,
            LinearLayout.LayoutParams.WRAP_CONTENT, 1f
        )
        tvLp2.setMargins(8,16,8,16)
        val strArr:MutableList<Int> = arrayListOf(R.string.sr_no, R.string.items,R.string.qty,R.string.unit)
        val headingLinearLayout = LinearLayout(context)
        headingLinearLayout.gravity = Gravity.CENTER_HORIZONTAL
        headingLinearLayout.orientation = LinearLayout.HORIZONTAL
        headingLinearLayout.layoutParams = lp
        for (i in 0..3){
            val tv = TextView(context,null,0,R.style.TextAppearance_AppCompat_Body1)
            tv.text = getString(strArr[i])
            if (i==1) {
                tv.layoutParams = tvLp
            }else {
                tv.layoutParams = tvLp2
                tv.gravity = Gravity.CENTER_HORIZONTAL
            }
            headingLinearLayout.addView(tv)
        }
        parent.addView(headingLinearLayout)
        var count = 0
        groceryListObservable.value?.forEach {
            val dataRow = LinearLayout(context)
            dataRow.gravity = Gravity.CENTER_HORIZONTAL
            dataRow.orientation = LinearLayout.HORIZONTAL
            dataRow.layoutParams = lp
            if(count %2==0)
                dataRow.setBackgroundColor(ContextCompat.getColor(context!!, R.color.off_white))

            val srNo = TextView(context,null,0,R.style.TextAppearance_AppCompat_Caption)
            srNo.text = (++count).toString()
            srNo.gravity = Gravity.CENTER_HORIZONTAL
            srNo.layoutParams = tvLp2
            dataRow.addView(srNo)
            val itemName = TextView(context,null,0,R.style.TextAppearance_AppCompat_Caption)
            itemName.text = it.productName
            itemName.layoutParams = tvLp
            dataRow.addView(itemName)
            val qty = TextView(context,null,0,R.style.TextAppearance_AppCompat_Caption)
            qty.text = it.quantity.toString()
            qty.gravity = Gravity.CENTER_HORIZONTAL
            qty.layoutParams = tvLp2
            dataRow.addView(qty)
            val unit = TextView(context,null,0,R.style.TextAppearance_AppCompat_Caption)
            unit.text = it.unit
            unit.gravity = Gravity.CENTER_HORIZONTAL
            unit.layoutParams = tvLp2
            dataRow.addView(unit)
            parent.addView(dataRow)
        }
        rootView.findViewById<FloatingActionButton>(R.id.fab_upload_list).setOnClickListener {
            listener!!.onUploadButtonClicked()
        }
        return rootView
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        if (context is UserListUpload) {
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
        @JvmStatic
        fun newInstance(): ViewCategoryItemsFragment {
            return ViewCategoryItemsFragment()
        }
    }

    interface UserListUpload {
        fun onUploadButtonClicked()
    }
}