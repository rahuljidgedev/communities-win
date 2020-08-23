package com.app.communities_win_crisis.ui_activities.make_a_list_ui

import android.os.Bundle
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.viewpager.widget.ViewPager
import com.app.communities_win_crisis.R
import com.app.communities_win_crisis.network_interfacing.data_models.CategoryItemList
import com.app.communities_win_crisis.network_interfacing.data_models.CategoryListItem
import com.app.communities_win_crisis.ui_activities.make_a_list_ui.main.CategoryListAdapter
import com.app.communities_win_crisis.ui_activities.make_a_list_ui.main.MakeAListSectionsPagerAdapter
import com.app.communities_win_crisis.ui_activities.make_a_list_ui.main.ViewCategoryItemsFragment
import com.app.communities_win_crisis.utils.BaseActivity
import com.google.android.material.tabs.TabLayout


class MakeAListActivity : BaseActivity() ,
    CategoryListAdapter.CreateListHandler,
    ViewCategoryItemsFragment.UserListUpload {
    private var makeAListPresenter: MakeAListPresenter? = null
    private var sectionsPagerAdapter: MakeAListSectionsPagerAdapter? = null
    companion object {
        lateinit var groceryListObservable: MutableLiveData<CategoryItemList>
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_make_a_list)
        window.statusBarColor = ContextCompat.getColor(this, R.color.colorFriendsDark)
        makeAListPresenter=MakeAListPresenter(this)
        makeAListPresenter!!.requestAvailableCategories()
        groceryListObservable = MutableLiveData()
        getUserUpdatedList().observe(this, androidx.lifecycle.Observer {
            val frag: Fragment = supportFragmentManager.fragments[2]
            if(frag.isResumed)
                supportFragmentManager
                    .beginTransaction()
                    .detach(frag)
                    .attach(frag)
                    .commit()
        })
    }

    private fun getUserUpdatedList():LiveData<CategoryItemList>{
        return groceryListObservable
    }

    fun loadLayoutsUI(categoryItemList: CategoryItemList?) {
        runOnUiThread {
            sectionsPagerAdapter = MakeAListSectionsPagerAdapter(this,
                supportFragmentManager, categoryItemList)
            val viewPager: ViewPager = findViewById(R.id.view_pager)
            viewPager.offscreenPageLimit = 2
            viewPager.adapter = sectionsPagerAdapter
            val tabs: TabLayout = findViewById(R.id.tabs)
            tabs.setupWithViewPager(viewPager)
        }
    }

    override fun onCategoryItemUpdated(item: CategoryListItem) {
        makeAListPresenter?.updateUserGroceryList(item)
    }

    fun showMessageToUser(message: String?) {
        runOnUiThread {
            Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
        }
    }

    fun updateTheList(groceryList: CategoryItemList?) {
        groceryListObservable.value =groceryList
    }

    override fun onUploadButtonClicked() {
        if(userContact.isNullOrEmpty()){
            makeAListPresenter!!.showLoginDialog()
        }else {
            makeAListPresenter!!.requestUploadUserList()
        }
    }
}