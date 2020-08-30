package com.app.communities_win_crisis.ui_activities.make_a_list_ui.main

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.recyclerview.widget.RecyclerView
import com.app.communities_win_crisis.R
import com.app.communities_win_crisis.network_interfacing.data_models.CategoryListItem
import com.app.communities_win_crisis.network_interfacing.utils.HttpConstants
import com.app.communities_win_crisis.utils.AppConstants
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.category_list_item.view.*

class CategoryListAdapter(
    private var context: Context,
    private var categoryItemList: List<CategoryListItem>?,
    private val listener: CreateListHandler?
) : RecyclerView.Adapter<CategoryListAdapter.MyCategoryItemHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyCategoryItemHolder {
        val view = LayoutInflater.from(context)
            .inflate(R.layout.category_list_item, parent, false)
        return MyCategoryItemHolder(view)
    }

    override fun getItemCount(): Int {
        return if(categoryItemList != null &&categoryItemList?.size!! != 0) categoryItemList?.size!! else 0
    }

    override fun onBindViewHolder(holder: MyCategoryItemHolder, position: Int) {
        holder.mProductName.text = categoryItemList?.get(position)?.productName

        val imagePath = when {
            categoryItemList?.get(position)?.imageUrl!!.isNotEmpty() -> categoryItemList?.get(position)?.imageUrl
            categoryItemList?.get(position)?.category.equals("Fruits") -> {
                HttpConstants.LINK_FRUIT_NO_IMAGE
            }
            else -> {
                HttpConstants.LINK_VEGETABLES_NO_IMAGE
            }
        }
        val failureImage = if(categoryItemList?.get(position)?.category == AppConstants.GROCERY_VEGETABLES)
            R.drawable.veg_not_available
        else
            R.drawable.fruit_not_available
        Picasso.get()
            .load(imagePath)
            .error(failureImage)
            .placeholder(failureImage)
            .into(holder.mProductImage)

        ArrayAdapter.createFromResource(
            context,
            R.array.weight_units,
            android.R.layout.simple_spinner_item
        ).also {adapter ->
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
            holder.mSpinnerUnits.adapter = adapter
        }
        holder.mSpinnerUnits.onItemSelectedListener = object: AdapterView.OnItemSelectedListener{
            override fun onItemSelected(parent:AdapterView<*>, view: View, position: Int, id: Long){
                // Display the selected item text on text view
                holder.mProductQty.text = "0"
            }

            override fun onNothingSelected(parent: AdapterView<*>){}
        }
        holder.mQtyIncrement.setOnClickListener {
            holder.mProductQty.text = changeValueByUnit("+"+(holder.mSpinnerUnits
                .selectedItem.toString()), holder.mProductQty.text.toString())

            val categoryListItem = categoryItemList?.get(position)
            categoryListItem?.quantity = holder.mProductQty.text.toString().toFloatOrNull()!!
            categoryListItem?.unit = holder.mSpinnerUnits.selectedItem.toString()
            if (categoryListItem != null) {
                listener?.onCategoryItemUpdated(categoryListItem)
            }
        }

        holder.mQtyDecrement.setOnClickListener {
            if (holder.mProductQty.text.toString().toFloat() <= 0) {
                holder.mProductQty.text = "0"
                return@setOnClickListener
            }
            holder.mProductQty.text = changeValueByUnit("-"+(holder.mSpinnerUnits
                .selectedItem.toString()), holder.mProductQty.text.toString())

            val categoryListItem = categoryItemList?.get(position)
            categoryListItem?.quantity = holder.mProductQty.text.toString().toFloatOrNull()!!
            categoryListItem?.unit = holder.mSpinnerUnits.selectedItem.toString()
            if (categoryListItem != null) {
                listener?.onCategoryItemUpdated(categoryListItem)
            }
        }
    }

    private fun changeValueByUnit(operation: String, value: String): String {
        when (operation) {
            "+unit","-unit" ->{
                return value
            }
            "+kg", "+count" ->{
                return value.toFloatOrNull()?.plus(1).toString()
            }
            "+gm" ->{
                return value.toFloatOrNull()?.plus(50).toString()
            }
            "+dozen" ->{
                return value.toFloatOrNull()?.plus(0.5f).toString()
            }
            "-kg","-count" ->{
                return value.toFloatOrNull()?.minus(1).toString()
            }
            "-gm" ->{
                return value.toFloatOrNull()?.minus(50).toString()
            }
            "-dozen" ->{
                return value.toFloatOrNull()?.minus(0.5f).toString()
            }
        }
        return value
    }

    inner class MyCategoryItemHolder(mView: View): RecyclerView.ViewHolder(mView) {
        val mProductImage: ImageView = mView.iv_icon
        val mProductName: TextView = mView.tv_category_name
        val mQtyIncrement:ImageButton = mView.btn_increase_qty
        val mQtyDecrement:ImageButton = mView.btn_decrease_qty
        val mProductQty: TextView = mView.tv_category_qty
        val mSpinnerUnits: Spinner = mView.spinner_unit
    }

    interface CreateListHandler {
        fun onCategoryItemUpdated(
            item: CategoryListItem
        )
    }
}