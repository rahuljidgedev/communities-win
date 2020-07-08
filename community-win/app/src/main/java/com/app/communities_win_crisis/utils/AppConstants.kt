package com.app.communities_win_crisis.utils

class AppConstants {

    companion object {

        /**App Constants**/



        enum class  TABS{
            FRIENDS, GROCERY, OTHERS
        }

        const val PERMISSIONS_REQUEST_READ_CONTACTS = 100

        const val FRIENDS_VIEW_REGISTER: Int = 0

        const val FRIENDS_VIEW_CONNECTION_LIST: Int = 1

        const val FRIENDS_VIEW_INVITE_LIST: Int = 2

        const val USER_TYPE_CONNECTION: Int = 1

        const val USER_TYPE_INVITE: Int = 2


        const val GROCERY_VIEW_TAB_ONE: Int = 0

        const val GROCERY_VIEW_TAB_TWO: Int = 1

        const val GROCERY_VIEW_TAB_THREE: Int = 2

        const val GROCERY_VEGETABLES: String = "Vegetables"

        const val GROCERY_FRUITS: String = "Fruits"


        const val OTHERS_VIEW_TAB_ONE: Int = 0

        const val OTHERS_VIEW_TAB_TWO: Int = 1

        const val OTHERS_VIEW_TAB_THREE: Int = 2

        const val EMPTY_TOKEN: Int = 0


    }
}