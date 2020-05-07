package com.app.app_demo.ui_activities

import android.Manifest
import android.app.Dialog
import android.content.DialogInterface
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.viewpager.widget.ViewPager
import com.app.app_demo.R
import com.app.app_demo.models.ContactInfo
import com.app.app_demo.ui.main.PlaceholderFragment
import com.app.app_demo.ui.main.SafetyContactListFragment
import com.app.app_demo.ui.main.SectionsPagerAdapter
import com.app.app_demo.ui.main.presentor.HomePresenter
import com.app.app_demo.utils.AppConstants
import com.app.app_demo.utils.AppConstants.Companion.FRIENDS_VIEW_REGISTER
import com.app.app_demo.utils.BaseActivity
import com.google.android.material.appbar.AppBarLayout
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.android.material.snackbar.Snackbar
import com.google.android.material.tabs.TabLayout


class HomePageActivity: BaseActivity(),
    SafetyContactListFragment.OnListFragmentInteractionListener,
    PlaceholderFragment.OnRegisterButtonTappedListener{
    private var bottomNavigation: BottomNavigationView? = null
    private var homePresenter: HomePresenter? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home_page)

        homePresenter= HomePresenter(this)
        val fab: FloatingActionButton = findViewById(R.id.fab)
        bottomNavigation = findViewById(R.id.bottom_navigation)
        loadUItoLayout(AppConstants.Companion.TABS.FRIENDS, FRIENDS_VIEW_REGISTER, null)

        bottomNavigation?.setOnNavigationItemSelectedListener {
                item ->
            when(item.itemId){
                R.id.menu_friends -> {
                    loadFriendsTab()
                    return@setOnNavigationItemSelectedListener true
                }
                R.id.menu_grocery -> {
                    loadGroceryTab()
                    return@setOnNavigationItemSelectedListener true
                }
                R.id.menu_others -> {
                    loadOthersTab()
                    return@setOnNavigationItemSelectedListener true
                }
            }
            false
        }

        fab.setOnClickListener { view ->
            Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                .setAction("Action", null).show()
        }
    }

    private fun loadFriendsTab() {
        bottomNavigation?.menu?.getItem(0)?.isChecked = true
        findViewById<AppBarLayout>(R.id.app_bar).setBackgroundColor(
            resources.getColor(R.color.colorFriendsPrimary))
        bottomNavigation?.setBackgroundColor(resources.getColor(R.color.colorFriendsPrimary))
        window.statusBarColor = resources.getColor(R.color.colorFriendsDark)
        //findViewById<FloatingActionButton>(R.id.fab).visibility = View.GONE
        findViewById<TabLayout>(R.id.tabs).visibility = View.VISIBLE
        findViewById<ViewPager>(R.id.view_pager).visibility = View.VISIBLE
    }

    private fun loadGroceryTab() {
        bottomNavigation?.menu?.getItem(1)?.isChecked = true
        findViewById<AppBarLayout>(R.id.app_bar).setBackgroundColor(
            resources.getColor(R.color.colorGroceryPrimary))
        bottomNavigation?.setBackgroundColor(resources.getColor(R.color.colorGroceryPrimary))
        window.statusBarColor = resources.getColor(R.color.colorGroceryDark)
        //findViewById<FloatingActionButton>(R.id.fab).visibility = View.VISIBLE
        findViewById<TabLayout>(R.id.tabs).visibility = View.GONE
        findViewById<ViewPager>(R.id.view_pager).visibility = View.GONE
    }

    private fun loadOthersTab() {
        bottomNavigation?.menu?.getItem(2)?.isChecked = true
        findViewById<AppBarLayout>(R.id.app_bar).setBackgroundColor(
            resources.getColor(R.color.colorOthersPrimary))
        bottomNavigation?.setBackgroundColor(resources.getColor(R.color.colorOthersPrimary))
        window.statusBarColor = resources.getColor(R.color.colorOthersDark)
        //findViewById<FloatingActionButton>(R.id.fab).visibility = View.GONE
        findViewById<TabLayout>(R.id.tabs).visibility = View.VISIBLE
        findViewById<ViewPager>(R.id.view_pager).visibility = View.VISIBLE
    }

    private fun showPermissionDialogToUploadContact() {
        val builder: AlertDialog.Builder = AlertDialog.Builder(this)
        builder.setTitle(getString(R.string.user_message))
        builder.setCancelable(true)
        builder.setPositiveButton(getString(R.string.yes), DialogInterface.OnClickListener {
                dialog, which ->
            if (!isContactReadPermissionAvailable()) {
                requestPermissionToReadContact()
            } else {
                homePresenter?.requestContactUpload()
            }
            dialog.dismiss()
        })
        builder.setNegativeButton(getString(R.string.no), DialogInterface.OnClickListener {
                dialog, which ->
                dialog.dismiss()
        })
        val dialog: Dialog = builder.create()
        dialog.show()
    }

    internal fun loadUItoLayout(tabType: AppConstants.Companion.TABS, position: Int, contactInfo: List<ContactInfo>?) {
        runOnUiThread {
            val sectionsPagerAdapter = SectionsPagerAdapter(
                this,
                tabType,
                supportFragmentManager,
                contactInfo)
            sectionsPagerAdapter.notifyDataSetChanged()
            val viewPager: ViewPager = findViewById(R.id.view_pager)
            viewPager.visibility = View.VISIBLE
            viewPager.adapter = sectionsPagerAdapter
            val tabs: TabLayout = findViewById(R.id.tabs)
            tabs.setupWithViewPager(viewPager)
            viewPager.currentItem = position
            when (tabType){
                AppConstants.Companion.TABS.FRIENDS ->{
                    loadFriendsTab()
                }
                AppConstants.Companion.TABS.GROCERY ->{
                    loadGroceryTab()
                }
                AppConstants.Companion.TABS.OTHERS ->{
                    loadOthersTab()
                }
            }
        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (grantResults[0] == PackageManager.PERMISSION_GRANTED){
            //findViewById<FrameLayout>(R.id.busy_progress).visibility = View.VISIBLE
            homePresenter?.requestContactUpload()
        }else{
            loadUItoLayout(AppConstants.Companion.TABS.FRIENDS, FRIENDS_VIEW_REGISTER, null)
        }
    }

    override fun onListFragmentInteraction(item: ContactInfo) {
        Toast.makeText(this, "Tapped list item.", Toast.LENGTH_SHORT).show()
    }

    override fun onLoadRequestConnectionUpdate() {
        if(userContactUpdated!! == 0){
            showPermissionDialogToUploadContact()
        }else{
            homePresenter!!.requestActiveContact()
        }

        /***
         * 1) Request for contact Read Permission
         * 2) Read all contact make them in string form
         ***/
        /*if (isContactReadPermissionAvailable()){
            homePresenter?.requestContactUpload()
        }else{
            requestPermissionToReadContact()
        }*/
    }

    override fun onRegisterButtonTapped(contact: String) {
        homePresenter?.requestTokenTokenUpdate(contact)
    }

    private fun isContactReadPermissionAvailable(): Boolean {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M && checkSelfPermission(
                Manifest.permission.READ_CONTACTS) == PackageManager.PERMISSION_GRANTED)
            return true
        else if(Build.VERSION.SDK_INT < Build.VERSION_CODES.M)
            return true
        return false
    }

    private fun requestPermissionToReadContact() {
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            requestPermissions(
                arrayOf(Manifest.permission.READ_CONTACTS),
                AppConstants.PERMISSIONS_REQUEST_READ_CONTACTS
            )
        }
    }

    fun showErrorMessage(message: String?) {
        runOnUiThread {
            Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
        }
    }

    fun loadRegistrationUpdateUI() {
        loadUItoLayout(AppConstants.Companion.TABS.FRIENDS, FRIENDS_VIEW_REGISTER, null)
    }
}