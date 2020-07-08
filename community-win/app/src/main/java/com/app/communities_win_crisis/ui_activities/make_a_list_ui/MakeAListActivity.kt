package com.app.communities_win_crisis.ui_activities.make_a_list_ui

import android.os.Bundle
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.viewpager.widget.ViewPager
import com.app.communities_win_crisis.R
import com.app.communities_win_crisis.network_interfacing.data_models.CategoryItemList
import com.app.communities_win_crisis.network_interfacing.data_models.CategoryListItem
import com.app.communities_win_crisis.ui_activities.make_a_list_ui.main.CategoryListAdapter
import com.app.communities_win_crisis.ui_activities.make_a_list_ui.main.MakeAListSectionsPagerAdapter
import com.app.communities_win_crisis.utils.BaseActivity
import com.google.android.material.tabs.TabLayout

class MakeAListActivity : BaseActivity() ,
    CategoryListAdapter.CreateListHandler {
    private var makeAListPresenter: MakeAListPresenter? = null
    private var sectionsPagerAdapter: MakeAListSectionsPagerAdapter? = null
    private var groceryList: CategoryItemList? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_make_a_list)
        window.statusBarColor = ContextCompat.getColor(this, R.color.colorGroceryDark)
        makeAListPresenter=MakeAListPresenter(this)
        makeAListPresenter!!.requestAvailableCategories()
    }

    fun loadLayoutsUI(categoryItemList: CategoryItemList?) {
        runOnUiThread {
            sectionsPagerAdapter = MakeAListSectionsPagerAdapter(this,
                supportFragmentManager, categoryItemList, groceryList)
            val viewPager: ViewPager = findViewById(R.id.view_pager)
            viewPager.offscreenPageLimit = 2
            viewPager.adapter = sectionsPagerAdapter
            viewPager.addOnPageChangeListener(object: ViewPager.OnPageChangeListener{
                override fun onPageScrollStateChanged(state: Int) {
                }

                override fun onPageScrolled(
                    position: Int,
                    positionOffset: Float,
                    positionOffsetPixels: Int
                ) {}

                override fun onPageSelected(position: Int) {
                    if(position==2){
                        sectionsPagerAdapter!!.updateUserList(groceryList)
                        viewPager.adapter.notifyDataSetChanged()
                    }
                }
            })
            val tabs: TabLayout = findViewById(R.id.tabs)
            tabs.setupWithViewPager(viewPager)

        }
    }

    override fun onCategoryItemUpdated(item: CategoryListItem) {
        makeAListPresenter?.updateUserGroceryList(item)
    }

    fun showErrorMessage(message: String?) {
        runOnUiThread {
            Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
        }
    }

    fun updateTheList(groceryList: CategoryItemList?) {
        this.groceryList = groceryList
    }
}