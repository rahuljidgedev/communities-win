package com.app.app_demo.ui.main

import android.content.Context
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter
import com.app.app_demo.R
import com.app.app_demo.models.ContactInfo
import com.app.app_demo.models.UserSession
import com.app.app_demo.network_interfacing.data_models.UserToken
import com.app.app_demo.ui_activities.HomePage
import com.app.app_demo.utils.AppConstants.Companion.VIEW_CONNECTION_LIST
import com.app.app_demo.utils.AppConstants.Companion.VIEW_REGISTER
import com.app.app_demo.utils.AppConstants.Companion.VIEW_REGISTER_UPDATE

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
    private val friends: Boolean,
    fm: FragmentManager,
    private val contactInfo: List<ContactInfo>?
) :
    FragmentPagerAdapter(fm), SafetyContactListFragment.OnListFragmentInteractionListener {

    override fun getItem(position: Int): Fragment {
        // getItem is called to instantiate the fragment for the given page.
        // Return a PlaceholderFragment (defined as a static inner class below).
        return if(friends){
            when (position) {
                VIEW_REGISTER ->{
                    if((context as HomePage).userContact?.isNotEmpty()!! &&
                        (context as HomePage).userToken != 0){
                        val userSession: UserSession = UserSession((context as HomePage).userContact.toString(),
                            (context as HomePage).userToken.toString())
                        PlaceholderFragment.newInstance(position + 1, VIEW_REGISTER_UPDATE,
                            userSession)
                    }else{
                        PlaceholderFragment.newInstance(position + 1, VIEW_REGISTER,
                            UserSession("",""))
                    }
                }
                VIEW_CONNECTION_LIST ->
                    SafetyContactListFragment.newInstance(position, contactInfo)
                else ->
                    SafetyContactListFragment.newInstance(position, contactInfo)
            }
        } else {
            /*add Fragments based on others tab*/
            PlaceholderFragment.newInstance(position + 1, VIEW_REGISTER_UPDATE,
                UserSession("",""))
        }
    }

    override fun getPageTitle(position: Int): CharSequence? {
        return if(friends)
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
}