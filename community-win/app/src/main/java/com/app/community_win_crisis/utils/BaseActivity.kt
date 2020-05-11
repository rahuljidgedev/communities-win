package com.app.community_win_crisis.utils

import android.content.Context
import android.content.SharedPreferences
import android.content.SharedPreferences.Editor
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity

/**
 * Created by Rahul on 15-Apr-2020.
 */
open class BaseActivity: AppCompatActivity() {
    private val constUserToken = "user_token"
    private val constContact = "contact"
    private val constContactUpdated = "contact_updated"
    private var mPreferences: SharedPreferences? = null
    private var mEditor: Editor? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val prefName = "community_win_preferences"
        mPreferences = getSharedPreferences(prefName, Context.MODE_PRIVATE)
        mEditor = mPreferences?.edit()
    }

   fun setUserToken(token: Int) {
        storePreferenceKeyWithValue(Int::class.java.toString(), constUserToken, token)
    }

   val userToken: Int?
        get() = retrievePreferenceKeyWithValue(Int::class.java.toString(), constUserToken) as Int?

   fun setUserContact(contact: String) {
        storePreferenceKeyWithValue(String::class.java.toString(), constContact, contact)
    }

   val userContact: String?
        get() = retrievePreferenceKeyWithValue(String::class.java.toString(), constContact) as String?

    fun setUserContactUpdated(count: Int) {
        storePreferenceKeyWithValue(Int::class.java.toString(), constContactUpdated, count)
    }

    val userContactUpdated: Int?
        get() = retrievePreferenceKeyWithValue(Int::class.java.toString(), constContactUpdated) as Int?


   private fun storePreferenceKeyWithValue(classType: String, key: String, `val`: Any) {
        when (classType) {
            Int::class.java.toString() -> mEditor?.putInt(key, (`val` as Int))?.commit()
            Long::class.java.toString() -> mEditor?.putLong(key, (`val` as Long))?.commit()
            Float::class.java.toString() -> mEditor?.putFloat(key, (`val` as Float))?.commit()
            Boolean::class.java.toString() -> mEditor?.putBoolean(key, (`val` as Boolean))?.commit()
            String::class.java.toString() -> mEditor?.putString(key, `val` as String)?.commit()
        }
   }

   private fun retrievePreferenceKeyWithValue(classType: String, key: String): Any? {
        var valueOfKey: Any? = null
        when (classType) {
            Int::class.java.toString() -> valueOfKey = mPreferences?.getInt(key, 0)
            Long::class.java.toString() -> valueOfKey = mPreferences?.getLong(key, 0L)
            Float::class.java.toString() -> valueOfKey = mPreferences?.getFloat(key, 0.0f)
            Boolean::class.java.toString() -> valueOfKey = mPreferences?.getBoolean(key, false)
            String::class.java.toString() -> valueOfKey = mPreferences?.getString(key, "")
        }
        return valueOfKey
   }
}