package com.app.communities_win_crisis.ui_activities.make_a_list_ui.main

import android.content.Context
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter
import androidx.viewpager.widget.PagerAdapter
import com.app.communities_win_crisis.R
import com.app.communities_win_crisis.network_interfacing.data_models.CategoryItemList
import com.app.communities_win_crisis.network_interfacing.data_models.CategoryListItem
import com.app.communities_win_crisis.utils.AppConstants

private val TAB_TITLES = arrayOf(
    R.string.make_a_list_tab_text_1,
    R.string.make_a_list_tab_text_2,
    R.string.make_a_list_tab_text_3
)

/**
 * A [FragmentPagerAdapter] that returns a fragment corresponding to
 * one of the sections/tabs/pages.
 */
class MakeAListSectionsPagerAdapter (
    private val context: Context,
    fm: FragmentManager,
    private val categoryItemList: CategoryItemList?,
    private var userGroceryList: CategoryItemList?
) :
    FragmentPagerAdapter(fm) ,
    CategoryListAdapter.CreateListHandler {

    override fun getItem(position: Int): Fragment {
        return when (position) {
            0 -> {
                val filteredList=  categoryItemList?.filter {
                    it.category == AppConstants.GROCERY_VEGETABLES
                }
                SelectCategoryItemsFragment.newInstance(position,
                    filteredList)
            }
            1 -> {
                val filteredList = categoryItemList?.filter {
                    it.category == AppConstants.GROCERY_FRUITS
                }
                SelectCategoryItemsFragment.newInstance(position,
                    filteredList)
            }
            2 -> {
                ViewCategoryItemsFragment.newInstance(position, userGroceryList)
            }
            else -> {
                SelectCategoryItemsFragment.newInstance(position, null)
            }
        }
    }

    override fun getPageTitle(position: Int): CharSequence? {
        return context.resources.getString(TAB_TITLES[position])
    }

    override fun getItemPosition(`object`: Any): Int {
        return PagerAdapter.POSITION_NONE
    }

    override fun getCount(): Int {
        return 3
    }

    override fun onCategoryItemUpdated(item: CategoryListItem) {
        TODO("Not yet implemented")
    }

    fun updateUserList(groceryList: CategoryItemList?) {
        userGroceryList = groceryList
        notifyDataSetChanged()
    }
}