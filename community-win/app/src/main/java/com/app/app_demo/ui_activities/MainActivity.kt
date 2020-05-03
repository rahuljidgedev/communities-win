package com.app.app_demo.ui_activities

import android.content.Intent

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import com.app.app_demo.R


class MainActivity : AppCompatActivity() {
    private val CONST_REQUEST_CONTACT_SAFETY: Int = 1
    private val CONST_REQUEST_ESSENTIAL_AROUND: Int = 2

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
    }

     fun openContactSafeScreen (view: View) {
        startActivityForResult(Intent(this, ContactSafety::class.java),
            CONST_REQUEST_CONTACT_SAFETY)
    }

     fun openEssentialAroundScreen (view: View) {
        startActivityForResult(Intent(this, EssentialAround::class.java),
            CONST_REQUEST_ESSENTIAL_AROUND)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
    }
}
