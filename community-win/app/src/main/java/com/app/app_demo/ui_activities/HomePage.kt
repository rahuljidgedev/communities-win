package com.app.app_demo.ui_activities

import android.Manifest
import android.app.AlertDialog
import android.app.Dialog
import android.content.DialogInterface
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.view.View
import android.widget.FrameLayout
import android.widget.Toast
import androidx.viewpager.widget.ViewPager
import com.app.app_demo.R
import com.app.app_demo.models.ContactInfo
import com.app.app_demo.network_interfacing.data_models.ActiveContactList
import com.app.app_demo.network_interfacing.data_models.PushedContact
import com.app.app_demo.network_interfacing.data_models.UserToken
import com.app.app_demo.network_interfacing.interfaces.HttpResponseHandler
import com.app.app_demo.network_interfacing.utils.HttpConstants
import com.app.app_demo.network_interfacing.utils.HttpConstants.Companion.SERVICE_REQUEST_ANONYMOUS_CONTACT_UPLOAD
import com.app.app_demo.network_interfacing.utils.HttpConstants.Companion.SERVICE_REQUEST_TOKEN_UPDATE
import com.app.app_demo.network_interfacing.utils.HttpConstants.Companion.SERVICE_REQUEST_USER_CONTACT_LIST
import com.app.app_demo.network_interfacing.utils.HttpRequestsUtils
import com.app.app_demo.network_interfacing.utils.HttpRequestsUtils.Companion.httpUserAnonymousContactUploadRequest
import com.app.app_demo.ui.main.SafetyContactListFragment
import com.app.app_demo.ui.main.SectionsPagerAdapter
import com.app.app_demo.utils.AppConstants
import com.app.app_demo.utils.AppConstants.Companion.USER_TYPE_CONNECTION
import com.app.app_demo.utils.AppConstants.Companion.VIEW_REGISTER
import com.app.app_demo.utils.AppConstants.Companion.VIEW_REGISTER_UPDATE
import com.app.app_demo.utils.BaseActivity
import com.app.app_demo.utils.ContactUtils
import com.google.android.material.appbar.AppBarLayout
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.android.material.snackbar.Snackbar
import com.google.android.material.tabs.TabLayout
import com.google.gson.Gson


class HomePage: BaseActivity(), HttpResponseHandler,
    SafetyContactListFragment.OnListFragmentInteractionListener {
    private var bottomNavigation: BottomNavigationView? = null
    private var contacts: MutableList<ContactInfo> = arrayListOf()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home_page)
        if (userToken == 0 && userContact?.isEmpty()!!){
            loadUItoLayout(true, VIEW_REGISTER, null)
        }else{
            loadUItoLayout(false, VIEW_REGISTER, null)
        }

        val fab: FloatingActionButton = findViewById(R.id.fab)
        bottomNavigation = findViewById(R.id.bottom_navigation)
        bottomNavigation?.setOnNavigationItemSelectedListener {
                item ->
            when(item.itemId){
                R.id.menu_friends -> {
                    findViewById<AppBarLayout>(R.id.app_bar).setBackgroundColor(
                        resources.getColor(R.color.colorFriendsPrimary))
                    bottomNavigation?.setBackgroundColor(resources.getColor(R.color.colorFriendsPrimary))
                    window.statusBarColor = resources.getColor(R.color.colorFriendsDark)
                    fab.visibility = View.GONE
                    findViewById<TabLayout>(R.id.tabs).visibility = View.VISIBLE
                    findViewById<ViewPager>(R.id.view_pager).visibility = View.VISIBLE
                    if(userContactUpdated!! == 0){
                        showPermissionDialogToUploadContact()
                    }else{
                        findViewById<FrameLayout>(R.id.busy_progress).visibility = View.VISIBLE
                        val map: HashMap<String, String> = HashMap(3)
                        map[HttpConstants.REQ_BODY_NAME_CEL] = userContact!!
                        map[HttpConstants.REQ_BODY_TOKEN] = userToken!!.toString()
                        map[HttpConstants.REQ_BODY_TYP] = HttpConstants.REQ_BODY_VALUE_TYP
                        HttpRequestsUtils.httpUserActiveConnectionListRequest(
                            SERVICE_REQUEST_USER_CONTACT_LIST,
                            map,
                            this
                        )
                    }
                    return@setOnNavigationItemSelectedListener true
                }
                R.id.menu_grocery -> {
                    findViewById<AppBarLayout>(R.id.app_bar).setBackgroundColor(
                        resources.getColor(R.color.colorGroceryPrimary))
                    bottomNavigation?.setBackgroundColor(resources.getColor(R.color.colorGroceryPrimary))
                    window.statusBarColor = resources.getColor(R.color.colorGroceryDark)
                    fab.visibility = View.VISIBLE
                    findViewById<TabLayout>(R.id.tabs).visibility = View.GONE
                    findViewById<ViewPager>(R.id.view_pager).visibility = View.GONE
                    return@setOnNavigationItemSelectedListener true
                }
                R.id.menu_others -> {
                    findViewById<AppBarLayout>(R.id.app_bar).setBackgroundColor(
                        resources.getColor(R.color.colorOthersPrimary))
                    bottomNavigation?.setBackgroundColor(resources.getColor(R.color.colorOthersPrimary))
                    window.statusBarColor = resources.getColor(R.color.colorOthersDark)
                    fab.visibility = View.GONE
                    findViewById<TabLayout>(R.id.tabs).visibility = View.VISIBLE
                    findViewById<ViewPager>(R.id.view_pager).visibility = View.VISIBLE
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

    private fun showPermissionDialogToUploadContact() {
        val builder: AlertDialog.Builder = AlertDialog.Builder(this)
        builder.setTitle(getString(R.string.user_message))
        builder.setCancelable(true)
        builder.setPositiveButton(getString(R.string.yes), DialogInterface.OnClickListener {
                dialog, which ->
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M && checkSelfPermission(
                    Manifest.permission.READ_CONTACTS) != PackageManager.PERMISSION_GRANTED) {
                requestPermissions(arrayOf(Manifest.permission.READ_CONTACTS),
                    AppConstants.PERMISSIONS_REQUEST_READ_CONTACTS
                )
            } else {
                findViewById<FrameLayout>(R.id.busy_progress).visibility = View.VISIBLE
                val nextContacts = createRequestData()
                requestHttpUserAnonymousUser(nextContacts.
                removeRange(nextContacts.length-1, nextContacts.length))
            }
            dialog.dismiss()
        })
        builder.setNegativeButton(getString(R.string.no), DialogInterface.OnClickListener {
                dialog, which ->
                dialog.dismiss()
        })
        val dialog:Dialog= builder.create()
        dialog.show()
    }

    private fun createRequestData(): String {
        contacts = ContactUtils.getContacts(this)
        var nextContacts = ""
        if (contacts.size > 50){
            for (x in 0..49){
                nextContacts += "${contacts[x].number},"
            }
        }
        return nextContacts
    }

    private fun loadUItoLayout(isFriendsView: Boolean, position: Int, contactInfo: List<ContactInfo>?) {
        val sectionsPagerAdapter = SectionsPagerAdapter(
            this,
            isFriendsView,
            supportFragmentManager,
            contactInfo)

        val viewPager: ViewPager = findViewById(R.id.view_pager)
        viewPager.visibility = View.VISIBLE
        viewPager.adapter = sectionsPagerAdapter
        val tabs: TabLayout = findViewById(R.id.tabs)
        tabs.setupWithViewPager(viewPager)
        viewPager.currentItem = position
    }

    private fun requestHttpUserAnonymousUser(contacts: String){
        val map: HashMap<String,String> = HashMap(3)
        map[HttpConstants.REQ_BODY_NAME_CEL] = userContact!!
        map[HttpConstants.REQ_BODY_NAME_APP] = HttpConstants.REQ_APP
        map[HttpConstants.REQ_BODY_CONTACT_ALL] = contacts
        httpUserAnonymousContactUploadRequest(SERVICE_REQUEST_ANONYMOUS_CONTACT_UPLOAD, map,this)
    }

    override fun onSucceed(responseString: String?, contact: String?, requestName: String?) {
        when (requestName) {
            SERVICE_REQUEST_TOKEN_UPDATE -> {
                val userToken = Gson().fromJson(responseString, UserToken::class.java)
                setUserToken(userToken.table[0].tknNum)
                setUserContact(contact!!)
                /***
                 * 1) Request for contact Read Permission
                 * 2) Read all contact make them in string form
                 ***/
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M && checkSelfPermission(
                        Manifest.permission.READ_CONTACTS) != PackageManager.PERMISSION_GRANTED) {
                    requestPermissions(arrayOf(Manifest.permission.READ_CONTACTS),
                        AppConstants.PERMISSIONS_REQUEST_READ_CONTACTS
                    )
                } else {
                    val nextContacts = createRequestData()
                    requestHttpUserAnonymousUser(nextContacts.removeRange(nextContacts.length-1, nextContacts.length))
                }
            }
            SERVICE_REQUEST_USER_CONTACT_LIST -> {
                runOnUiThread {
                    val contactActiveList = Gson().fromJson(responseString, ActiveContactList::class.java)
                    val contactInfo: List<ContactInfo> = ContactUtils.getContacts(this)

                    for (x in contactInfo.indices){
                        for (y in contactActiveList.activeTable.indices){
                            if (contactInfo[x].number.toString().contains(contactActiveList.activeTable[y].toString())){
                                contactInfo[x].lastAct = contactActiveList.activeTable[y].prvAct
                                contactInfo[x].userType = USER_TYPE_CONNECTION
                                break
                            }
                        }
                    }

                    findViewById<FrameLayout>(R.id.busy_progress).visibility = View.GONE
                    if(contactActiveList != null)
                        loadUItoLayout(true, AppConstants.VIEW_CONNECTION_LIST, contactInfo)
                    else
                        loadUItoLayout(true, AppConstants.VIEW_INVITE_LIST, contactInfo)
                }
            }
            SERVICE_REQUEST_ANONYMOUS_CONTACT_UPLOAD -> {
                val pushedContact = Gson().fromJson(responseString,
                    PushedContact::class.java)

                /*Removed uploaded contacts from list*/
                var x = -1;
                while(++x < pushedContact.pushedContactTable[0].rwc)
                    contacts.removeAt(0)

                /*If list is empty, it depicts all contacts are uploaded then request for connections*/
                if (contacts.isEmpty()){
                    val map: HashMap<String, String> = HashMap(3)
                    map[HttpConstants.REQ_BODY_NAME_CEL] = userContact!!
                    map[HttpConstants.REQ_BODY_TOKEN] = userToken!!.toString()
                    map[HttpConstants.REQ_BODY_TYP] = HttpConstants.REQ_BODY_VALUE_TYP
                    HttpRequestsUtils.httpUserActiveConnectionListRequest(
                        SERVICE_REQUEST_USER_CONTACT_LIST,
                        map,
                        this
                    )
                    return
                }

                /*If list is not empty, then upload next set of contacts*/
                var nextContacts = ""
                if (contacts.size > 50){
                    for (x in 0..49){
                        nextContacts += "${contacts[x].number},"
                    }
                }else if(contacts.size < 50 && contacts.isNotEmpty()){
                    for (contact in contacts){
                        nextContacts += "${contact.number},"
                    }
                }
                requestHttpUserAnonymousUser(nextContacts.removeRange(nextContacts.length-1, nextContacts.length))
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
            findViewById<FrameLayout>(R.id.busy_progress).visibility = View.VISIBLE
            val nextContacts = createRequestData()
            requestHttpUserAnonymousUser(nextContacts.removeRange(nextContacts.length-1, nextContacts.length))
        }
    }

    override fun onFailure(message: String?) {
        runOnUiThread {
            Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
        }
    }

    override fun onListFragmentInteraction(item: ContactInfo) {
        Toast.makeText(this, "Tapped list item.", Toast.LENGTH_SHORT).show()
    }
}