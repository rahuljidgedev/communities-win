package com.app.communities_win_crisis.ui.main

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.app.communities_win_crisis.R
import com.app.communities_win_crisis.models.ContactInfo
import com.app.communities_win_crisis.models.UserSession
import com.app.communities_win_crisis.utils.AppConstants.Companion.EMPTY_TOKEN
import com.app.communities_win_crisis.utils.AppConstants.Companion.FRIENDS_VIEW_CONNECTION_LIST
import com.app.communities_win_crisis.utils.AppConstants.Companion.FRIENDS_VIEW_INVITE_LIST
import com.app.communities_win_crisis.utils.AppConstants.Companion.USER_TYPE_CONNECTION
import com.app.communities_win_crisis.utils.AppConstants.Companion.USER_TYPE_INVITE
import com.google.gson.Gson
import com.google.gson.GsonBuilder

/**
 * A fragment representing a list of Items.
 * Activities containing this fragment MUST implement the
 * [SafetyContactListFragment.OnListFragmentInteractionListener] interface.
 */
class SafetyContactListFragment : Fragment() {

    private var columnCount = 0

    private var listener: OnListFragmentInteractionListener? = null

    private var contactInfo: List<ContactInfo>? = null

    private var userSession: UserSession? = null

    private var listPopulated: Boolean = false

    private var fragmentType: Int = -1

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        var userListData = ""
        var userData = ""
        arguments?.let {
            columnCount = it.getInt(ARG_COLUMN_COUNT)
            userListData = it.getString(ARG_USER_LIST_DATA).toString()
            userData = it.getString(ARG_USER_DATA).toString()
        }
        try {
            contactInfo = GsonBuilder().create().fromJson(userListData, Array<ContactInfo>::class.java).toList()
        }catch (e: Exception){}
        try {
            userSession = GsonBuilder().create().fromJson(userData, UserSession::class.java)
        }catch (e: Exception){}
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
            if (columnCount == FRIENDS_VIEW_CONNECTION_LIST){
                fragmentType = FRIENDS_VIEW_CONNECTION_LIST
                connectionList = contactInfo?.filter {
                    it.userType == USER_TYPE_CONNECTION
                }
                text = text.replace("_", connectionList?.size.toString())
                    .replace("-", contactInfo?.size.toString())
                if (connectionList != null)
                    listPopulated = true
            }else{
                fragmentType = FRIENDS_VIEW_INVITE_LIST
                connectionList = contactInfo?.filter {
                    it.userType == USER_TYPE_INVITE
                }
                val connectionList2: List<ContactInfo>? = contactInfo?.filter {
                    it.userType == USER_TYPE_CONNECTION
                }
                text = text.replace("_", connectionList2?.size.toString())
                    .replace("-", contactInfo?.size.toString())

            }
            if (text.contains("null")){
                text = text.replace("null", EMPTY_TOKEN.toString())
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

    fun isListPopulated(): Boolean {
        return listPopulated
    }

    fun updateContactInfo(contactInfo: List<ContactInfo>){
        this.contactInfo = contactInfo
    }

    fun getFragmentType(): Int{
        return fragmentType
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
        const val ARG_USER_DATA = "user-info"

        // TODO: Customize parameter initialization
        @JvmStatic
        fun newInstance(
            columnCount: Int,
            contactInfo: List<ContactInfo>?,
            userSession: UserSession
        ) =
            SafetyContactListFragment().apply {
                arguments = Bundle().apply {
                    putInt(ARG_COLUMN_COUNT, columnCount)
                    putString(ARG_USER_LIST_DATA, Gson().toJson(contactInfo))
                    putString(ARG_USER_DATA, Gson().toJson(userSession))
                }
            }
    }
}
