package com.app.communities_win_crisis.ui_activities.make_a_list_ui.main

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.app.communities_win_crisis.R
import com.app.communities_win_crisis.network_interfacing.data_models.CategoryItemList
import com.app.communities_win_crisis.network_interfacing.data_models.CategoryListItem
import com.google.gson.Gson
import com.google.gson.GsonBuilder

/**
 * A placeholder fragment containing a simple view.
 */
class SelectCategoryItemsFragment :  Fragment(),
    CategoryListAdapter.CreateListHandler {

    private var listener: CategoryListAdapter.CreateListHandler? = null

    private var filteredList: List<CategoryListItem>? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        filteredList = GsonBuilder().create().fromJson(arguments.getString(CAT_ITEM_DATA), CategoryItemList::class.java)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val rootView = inflater.inflate(R.layout.fragment_make_a_list, container, false)
        val recycler = rootView.findViewById(R.id.recycler_view) as RecyclerView
        with(recycler){
            layoutManager = LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
            adapter = CategoryListAdapter(context!!, filteredList, listener)
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
            filteredList: List<CategoryListItem>?
        ): SelectCategoryItemsFragment {
            return SelectCategoryItemsFragment().apply {
                arguments = Bundle().apply {
                    putInt(ARG_SECTION_NUMBER, sectionNumber)
                    putString(CAT_ITEM_DATA, Gson().toJson(filteredList))
                }
            }
        }
    }

    override fun onCategoryItemUpdated(item: CategoryListItem) {
        TODO("Not yet implemented")
    }
}