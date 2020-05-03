package com.app.app_demo.ui.main

import androidx.recyclerview.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import com.app.app_demo.R
import com.app.app_demo.models.ContactInfo
import com.app.app_demo.network_interfacing.data_models.ActiveContactTable


import com.app.app_demo.ui.main.SafetyContactListFragment.OnListFragmentInteractionListener
import com.app.app_demo.ui.main.models.DummyContent.DummyItem
import com.app.app_demo.utils.AppConstants.Companion.VIEW_CONNECTION_LIST
import com.squareup.picasso.Picasso

import kotlinx.android.synthetic.main.fragment_safety_contact_list.view.*
import java.time.LocalDate

/**
 * [RecyclerView.Adapter] that can display a [DummyItem] and makes a call to the
 * specified [OnListFragmentInteractionListener].
 * TODO: Replace the implementation with code for your data type.
 */
class MyItemRecyclerViewAdapter(
    private val mValues: List<ContactInfo>?,
    private val mListener: OnListFragmentInteractionListener?,
    private val columnNumber: Int
) : RecyclerView.Adapter<MyItemRecyclerViewAdapter.ViewHolder>() {

    private val mOnClickListener: View.OnClickListener

    init {
        mOnClickListener = View.OnClickListener { v ->
            val item = v.tag as ContactInfo
            // Notify the active callbacks interface (the activity, if the fragment is attached to
            // one) that an item has been selected.
            mListener?.onListFragmentInteraction(item)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.fragment_safety_contact_list, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = mValues?.get(position)
        holder.mContactName.text = item?.name
        if (columnNumber == VIEW_CONNECTION_LIST){
            /*Glide
                .with(this)
                .load(item?.photoUrl)
                .centerCrop()
                .placeholder(R.drawable.ic_user)
                .into(holder.mUserImage)*/
            Picasso.get()
                .load(item?.photoUrl)
                .error(R.drawable.ic_user)
                .placeholder(R.drawable.ic_user)
                .into(holder.mUserImage)
            holder.mUserImage.setImageResource(R.drawable.ic_user)
            holder.mLastUpdated.visibility = View.VISIBLE
            holder.ivShareWhatsapp.visibility = View.INVISIBLE
            holder.ivShareMessage.visibility = View.INVISIBLE
            holder.mLastUpdated.text = "Since ${LocalDate.parse(item?.lastAct?.substring(0,10))}"
        }else{
            holder.mLastUpdated.visibility = View.INVISIBLE
            holder.ivShareWhatsapp.visibility = View.VISIBLE
            holder.ivShareMessage.visibility = View.VISIBLE
            holder.mContactName.maxEms = 5
        }

        with(holder.mView) {
            tag = item
            setOnClickListener(mOnClickListener)
        }
    }

    override fun getItemCount(): Int {
        return mValues?.size ?: 0
    }

    inner class ViewHolder(val mView: View) : RecyclerView.ViewHolder(mView) {
        val mUserImage: ImageView = mView.iv_contact_image
        val mContactName: TextView = mView.tv_conatct_name
        val mLastUpdated: TextView = mView.tv_last_updated
        val ivShareWhatsapp: ImageView = mView.iv_whats_up
        val ivShareMessage: ImageView = mView.iv_messages

        override fun toString(): String {
            return super.toString() + " '" + mContactName.text + "'"
        }
    }
}