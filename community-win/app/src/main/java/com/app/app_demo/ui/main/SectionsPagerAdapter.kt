package com.app.app_demo.ui.main

import android.content.Context
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter
import com.app.app_demo.R
import com.app.app_demo.models.ContactInfo
import com.app.app_demo.models.UserSession
import com.app.app_demo.ui_activities.HomePageActivity
import com.app.app_demo.utils.AppConstants
import com.app.app_demo.utils.AppConstants.Companion.FRIENDS_VIEW_CONNECTION_LIST
import com.app.app_demo.utils.AppConstants.Companion.FRIENDS_VIEW_REGISTER
import com.app.app_demo.utils.AppConstants.Companion.GROCERY_VIEW_TAB_ONE
import com.app.app_demo.utils.AppConstants.Companion.OTHERS_VIEW_TAB_ONE

private val TAB_FRIENDS_TITLES = arrayOf(
    R.string.tab_friends_text_1,
    R.string.tab_friends_text_2,
    R.string.tab_friends_text_3
)
private val TAB_OTHERS_TITLES = arrayOf(
    R.string.tab_others_text_1,
    R.string.tab_others_text_2,
    R.string.tab_others_text_3
)

/**
 * A [FragmentPagerAdapter] that returns a fragment corresponding to
 * one of the sections/tabs/pages.
 */
class SectionsPagerAdapter(
    private val context: Context,
    private val tabType: AppConstants.Companion.TABS,
    fm: FragmentManager,
    private val contactInfo: List<ContactInfo>?
) :
    FragmentPagerAdapter(fm), SafetyContactListFragment.OnListFragmentInteractionListener,
    PlaceholderFragment.OnRegisterButtonTappedListener {

    override fun getItem(position: Int): Fragment {
        var userSession: UserSession? = null
        if (context is HomePageActivity) {
            userSession = UserSession( context.userContact.toString(), context.userToken.toString())
        }
        // getItem is called to instantiate the fragment for the given page.
        // Return a PlaceholderFragment (defined as a static inner class below).
        return when (tabType) {
            AppConstants.Companion.TABS.FRIENDS -> {

                when (position) {
                    FRIENDS_VIEW_REGISTER ->{
                        if((context as HomePageActivity).userContact?.isNotEmpty()!! &&
                            context.userToken != 0){
                            PlaceholderFragment.newInstance(position, FRIENDS_VIEW_REGISTER,
                                userSession!!
                            )
                        }else{
                            PlaceholderFragment.newInstance(position, FRIENDS_VIEW_REGISTER,
                                userSession!!)
                        }
                    }
                    FRIENDS_VIEW_CONNECTION_LIST ->
                        SafetyContactListFragment.newInstance(position, contactInfo, userSession!!)
                    else ->
                        SafetyContactListFragment.newInstance(position, contactInfo, userSession!!)
                }
            }
            AppConstants.Companion.TABS.GROCERY -> {
                /*add Fragments based on others tab*/
                PlaceholderFragment.newInstance(position, GROCERY_VIEW_TAB_ONE,
                    UserSession("",""))
            }
            AppConstants.Companion.TABS.OTHERS -> {
                PlaceholderFragment.newInstance(position, OTHERS_VIEW_TAB_ONE,
                    UserSession("",""))
            }
        }
    }

    override fun getPageTitle(position: Int): CharSequence? {
        return if(tabType == AppConstants.Companion.TABS.FRIENDS)
                context.resources.getString(TAB_FRIENDS_TITLES[position])
               else
                context.resources.getString(TAB_OTHERS_TITLES[position])
    }

    override fun getCount(): Int {
        // Show 3 total pages.
        return 3
    }

    override fun onListFragmentInteraction(item: ContactInfo) {
        TODO("Not yet implemented")
    }

    override fun onLoadRequestConnectionUpdate() {
        TODO("Not yet implemented")
    }

    override fun onRegisterButtonTapped(contact: String) {
        TODO("Not yet implemented")
    }
}