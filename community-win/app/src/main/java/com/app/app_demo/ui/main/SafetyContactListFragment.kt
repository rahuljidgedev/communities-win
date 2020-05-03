package com.app.app_demo.ui.main

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.app.app_demo.R
import com.app.app_demo.models.ContactInfo
import com.app.app_demo.utils.AppConstants.Companion.USER_TYPE_CONNECTION
import com.app.app_demo.utils.AppConstants.Companion.USER_TYPE_INVITE
import com.app.app_demo.utils.AppConstants.Companion.VIEW_CONNECTION_LIST
import com.app.app_demo.utils.AppConstants.Companion.VIEW_INVITE_LIST
import com.google.gson.Gson
import com.google.gson.GsonBuilder

/**
 * A fragment representing a list of Items.
 * Activities containing this fragment MUST implement the
 * [SafetyContactListFragment.OnListFragmentInteractionListener] interface.
 */
class SafetyContactListFragment : Fragment() {

    // TODO: Customize parameters
    private var columnCount = 0

    private var listener: OnListFragmentInteractionListener? = null

    private var contactInfo: List<ContactInfo>? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        var userData: String = ""
        arguments?.let {
            columnCount = it.getInt(ARG_COLUMN_COUNT)
            userData = it.getString(ARG_USER_LIST_DATA).toString()
        }

        contactInfo = GsonBuilder().create().fromJson(userData, Array<ContactInfo>::class.java).toList()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val parentView = inflater.inflate(R.layout.fragment_safety_contact_list_list, container, false)
        val textView: TextView = parentView.findViewById(R.id.tv_contacts_safe)
        val view = parentView.findViewById(R.id.list) as RecyclerView
        // Set the adapter
        with(view) {
            layoutManager = LinearLayoutManager(context)
            var text = parentView.context.getString(R.string.txt_safe_users)
            val connectionList: List<ContactInfo>?
            if (columnCount == VIEW_CONNECTION_LIST){
                connectionList = contactInfo?.filter {
                        it.userType == USER_TYPE_CONNECTION
                    }
                text = text.replace("_", connectionList?.size.toString())
                    .replace("-", contactInfo?.size.toString())
            }else{
                connectionList = contactInfo?.filter {
                        it.userType == USER_TYPE_INVITE
                    }
                val connectionList2: List<ContactInfo>? = contactInfo?.filter {
                        it.userType == USER_TYPE_CONNECTION
                    }
                text = text.replace("_", connectionList2?.size.toString())
                    .replace("-", contactInfo?.size.toString())
                if (connectionList2.isNullOrEmpty()){
                    text = text.replace("_", "0")
                        .replace("-", contactInfo?.size.toString())
                }
            }
            textView.text = text
            adapter = MyItemRecyclerViewAdapter(connectionList, listener, columnCount)
        }
        return parentView
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        if (context is OnListFragmentInteractionListener) {
            listener = context
        } else {
            //throw RuntimeException(context.toString() + " must implement OnListFragmentInteractionListener")
        }
    }

    override fun onDetach() {
        super.onDetach()
        listener = null
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     *
     *
     * See the Android Training lesson
     * [Communicating with Other Fragments](http://developer.android.com/training/basics/fragments/communicating.html)
     * for more information.
     */
    interface OnListFragmentInteractionListener {
        // TODO: Update argument type and name
        fun onListFragmentInteraction(item: ContactInfo)
    }

    companion object {

        // TODO: Customize parameter argument names
        const val ARG_COLUMN_COUNT = "column-count"
        const val ARG_USER_LIST_DATA = "user-list"

        // TODO: Customize parameter initialization
        @JvmStatic
        fun newInstance(
            columnCount: Int,
            contactInfo: List<ContactInfo>?
        ) =
            SafetyContactListFragment().apply {
                arguments = Bundle().apply {
                    putInt(ARG_COLUMN_COUNT, columnCount)
                    putString(ARG_USER_LIST_DATA, Gson().toJson(contactInfo))
                }
            }
    }
}
