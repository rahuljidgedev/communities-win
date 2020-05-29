package com.app.communities_win_crisis.ui.main

import android.net.Uri
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AlertDialog
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.RecyclerView
import com.app.communities_win_crisis.R
import com.app.communities_win_crisis.network_interfacing.data_models.ProductListItem
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.product_grid_item.view.*

class ProductGridRecyclerAdapter(
    private val productsList: ArrayList<ProductListItem>,
    private val listener: GridItemClickedListener?,
    private val dialog: AlertDialog
) : RecyclerView.Adapter<ProductGridRecyclerAdapter.ViewHolder>() {


    override fun getItemCount(): Int {
        return  productsList.size
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.product_grid_item, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.mProductName.text = productsList[position].productName
        Picasso.get()
            .load(Uri.parse(productsList[position].imageUrl))
            .error(R.drawable.ic_user)
            .placeholder(R.drawable.ic_user)
            .into(holder.mProductImage)
        holder.mParentCardView.setOnClickListener {
                listener?.onGridItemSelected(productsList[position], dialog)
        }
    }

    inner class ViewHolder(mView: View) : RecyclerView.ViewHolder(mView) {
        val mProductImage: ImageView = mView.product_image
        val mProductName: TextView = mView.tv_product_name
        val mParentCardView: CardView = mView.parent_card_view
    }

    interface GridItemClickedListener {
        fun onGridItemSelected(
            item: ProductListItem,
            dialog: AlertDialog
        )
    }

}