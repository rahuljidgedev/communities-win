package com.app.communities_win_crisis.ui_activities

import android.Manifest
import android.app.Dialog
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.core.content.ContextCompat
import androidx.viewpager.widget.ViewPager
import com.app.communities_win_crisis.R
import com.app.communities_win_crisis.models.ContactInfo
import com.app.communities_win_crisis.ui.main.PlaceholderFragment
import com.app.communities_win_crisis.ui.main.SafetyContactListFragment
import com.app.communities_win_crisis.ui.main.SectionsPagerAdapter
import com.app.communities_win_crisis.ui.main.presentor.HomePresenter
import com.app.communities_win_crisis.utils.AppConstants
import com.app.communities_win_crisis.utils.AppConstants.Companion.FRIENDS_VIEW_CONNECTION_LIST
import com.app.communities_win_crisis.utils.AppConstants.Companion.FRIENDS_VIEW_REGISTER
import com.app.communities_win_crisis.utils.BaseActivity
import com.google.android.material.appbar.AppBarLayout
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.android.material.snackbar.Snackbar
import com.google.android.material.tabs.TabLayout


@Suppress("UNCHECKED_CAST")
class HomePageActivity: BaseActivity(),
    SafetyContactListFragment.OnListFragmentInteractionListener,
    PlaceholderFragment.OnRegisterButtonTappedListener{
    private var bottomNavigation: BottomNavigationView? = null
    private var homePresenter: HomePresenter? = null
    private var sectionsPagerAdapter: SectionsPagerAdapter? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home_page)

        homePresenter= HomePresenter(this)
        val fab: FloatingActionButton = findViewById(R.id.fab)
        bottomNavigation = findViewById(R.id.bottom_navigation)
        loadUItoLayout()

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
            ContextCompat.getColor(this, R.color.colorFriendsPrimary))
        bottomNavigation?.setBackgroundColor(
            ContextCompat.getColor(this, R.color.colorFriendsPrimary))
        window.statusBarColor = ContextCompat.getColor(this, R.color.colorFriendsDark)
        findViewById<FloatingActionButton>(R.id.fab).visibility = View.GONE
        findViewById<TabLayout>(R.id.tabs).visibility = View.VISIBLE
        findViewById<ViewPager>(R.id.view_pager).visibility = View.VISIBLE
    }

    private fun loadGroceryTab() {
        bottomNavigation?.menu?.getItem(1)?.isChecked = true
        findViewById<AppBarLayout>(R.id.app_bar).setBackgroundColor(
            ContextCompat.getColor(this, R.color.colorGroceryPrimary))
        bottomNavigation?.setBackgroundColor(ContextCompat.getColor(this,
            R.color.colorGroceryPrimary))
        window.statusBarColor = ContextCompat.getColor(this, R.color.colorGroceryDark)
        findViewById<FloatingActionButton>(R.id.fab).visibility = View.VISIBLE
        findViewById<TabLayout>(R.id.tabs).visibility = View.GONE
        findViewById<ViewPager>(R.id.view_pager).visibility = View.GONE
    }

    private fun loadOthersTab() {
        bottomNavigation?.menu?.getItem(2)?.isChecked = true
        findViewById<AppBarLayout>(R.id.app_bar).setBackgroundColor(
            ContextCompat.getColor(this, R.color.colorOthersPrimary))
        bottomNavigation?.setBackgroundColor(ContextCompat.getColor(this,
            R.color.colorOthersPrimary))
        window.statusBarColor = ContextCompat.getColor(this, R.color.colorOthersDark)
        findViewById<FloatingActionButton>(R.id.fab).visibility = View.GONE
        findViewById<TabLayout>(R.id.tabs).visibility = View.VISIBLE
        findViewById<ViewPager>(R.id.view_pager).visibility = View.VISIBLE
    }

    private fun showPermissionDialogToUploadContact() {
        val builder: AlertDialog.Builder = AlertDialog.Builder(this)
        builder.setMessage(getString(R.string.user_message))
        builder.setCancelable(true)
        builder.setPositiveButton(getString(R.string.yes)) { dialog, _ ->
            requestPermissionToReadContact()
            dialog.dismiss()
        }
        builder.setNegativeButton(getString(R.string.no)) { dialog, _ ->
            dialog.dismiss()
        }
        val dialog: Dialog = builder.create()
        dialog.show()
    }

    private fun loadUItoLayout() {
        runOnUiThread {
            sectionsPagerAdapter = SectionsPagerAdapter(
                this,
                AppConstants.Companion.TABS.FRIENDS,
                supportFragmentManager,
                null)

            val viewPager: ViewPager = findViewById(R.id.view_pager)
            viewPager.visibility = View.VISIBLE
            viewPager.adapter = sectionsPagerAdapter
            viewPager.addOnPageChangeListener(object: ViewPager.OnPageChangeListener{
                override fun onPageScrollStateChanged(state: Int) {}

                override fun onPageScrolled(
                    position: Int,
                    positionOffset: Float,
                    positionOffsetPixels: Int
                ) {
                    val safetyFrag: List<SafetyContactListFragment> = supportFragmentManager.fragments.filter {
                        it is SafetyContactListFragment && it.getFragmentType() == FRIENDS_VIEW_CONNECTION_LIST
                    } as List<SafetyContactListFragment>

                    /**if position = FRIENDS_VIEW_CONNECTION_LIST (connection fragment is visible) &
                      contact read permission is not available**/
                    if (position == FRIENDS_VIEW_CONNECTION_LIST && userContact?.isNotEmpty()!! &&
                        userContactUpdated != 0 && !safetyFrag[0].isListPopulated() &&
                        (isContactReadPermissionAvailable() || !isContactReadPermissionAvailable())){
                        homePresenter!!.requestActiveContact()
                    }else if(position == FRIENDS_VIEW_CONNECTION_LIST && userContact?.isNotEmpty()!! &&
                        isContactReadPermissionAvailable() && !safetyFrag[0].isListPopulated()) {
                        homePresenter?.requestContactUpload()
                    }else if(position == FRIENDS_VIEW_CONNECTION_LIST && userContact?.isNotEmpty()!! &&
                        !isContactReadPermissionAvailable() && !safetyFrag[0].isListPopulated()) {
                        showPermissionDialogToUploadContact()
                    }
                }
                override fun onPageSelected(position: Int) {}

            })
            val tabs: TabLayout = findViewById(R.id.tabs)
            tabs.setupWithViewPager(viewPager)
            viewPager.currentItem = FRIENDS_VIEW_REGISTER
        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (grantResults[0] == PackageManager.PERMISSION_GRANTED){
            homePresenter?.requestContactUpload()
        }
    }

    override fun onListFragmentInteraction(item: ContactInfo) {
        Toast.makeText(this, "Tapped list item.", Toast.LENGTH_SHORT).show()
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
        runOnUiThread {
            try {
                val placeHolderFrag: List<PlaceholderFragment> =
                    supportFragmentManager.fragments.filterIsInstance<PlaceholderFragment>()

                placeHolderFrag[0].updateUIOnRegistrationSuccess(userToken.toString(),
                        userContact.toString())
                    supportFragmentManager
                        .beginTransaction()
                        .detach(placeHolderFrag[0])
                        .attach(placeHolderFrag[0])
                        .commit()
            }catch (e: Exception){}
        }
    }

    fun loadConnectionSafeConnectionList(
        contactInfo: List<ContactInfo>,
        friendsViewListType: Int
    ) {
        runOnUiThread {
            try {
                val safetyFrag: List<SafetyContactListFragment> = supportFragmentManager.fragments.filter {
                    it is SafetyContactListFragment && it.getFragmentType() == friendsViewListType
                } as List<SafetyContactListFragment>
                safetyFrag[0].updateContactInfo(contactInfo)
                supportFragmentManager
                    .beginTransaction()
                    .detach(safetyFrag[0])
                    .attach(safetyFrag[0])
                    .commit()
            }catch (e: Exception){}
        }
    }
}