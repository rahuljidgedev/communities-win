package com.app.app_demo.ui_activities

import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.os.CountDownTimer
import android.view.Window
import android.view.WindowManager
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import android.widget.ImageView
import android.widget.ProgressBar
import android.widget.Toast
import com.app.app_demo.R
import com.app.app_demo.network_interfacing.data_models.UserToken
import com.app.app_demo.network_interfacing.interfaces.HttpResponseHandler
import com.app.app_demo.network_interfacing.utils.HttpConstants
import com.app.app_demo.network_interfacing.utils.HttpConstants.Companion.REQ_BODY_NAME_CEL
import com.app.app_demo.network_interfacing.utils.HttpConstants.Companion.TOKEN_EXPIRED
import com.app.app_demo.network_interfacing.utils.HttpRequestsUtils
import com.app.app_demo.utils.BaseActivity
import com.google.gson.Gson
import kotlinx.android.synthetic.main.activity_splash.*


class SplashActivity : BaseActivity(), HttpResponseHandler {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        requestWindowFeature(Window.FEATURE_NO_TITLE)
        window.setFlags(
            WindowManager.LayoutParams.FLAG_FULLSCREEN,
            WindowManager.LayoutParams.FLAG_FULLSCREEN)
        setContentView(R.layout.activity_splash)

        val animFadeIn: Animation = AnimationUtils.loadAnimation(this,
            R.anim.fade_out
        )
        animFadeIn.reset()
        val imageView = findViewById<ImageView>(R.id.home)
        imageView.clearAnimation()
        imageView.startAnimation(animFadeIn)
        val progressBar: ProgressBar = findViewById(R.id.progressBar)
        val timer = object: CountDownTimer(20000,1000){
            override fun onFinish() {}

            override fun onTick(p0: Long) {
                progressBar.progress = progressBar.progress+5
                if (progressBar.progress > 20 && userToken == 0 && userContact?.isEmpty()!!){
                    startActivity(Intent(applicationContext,  HomePage::class.java))
                    this.cancel()
                    finish()
                }
            }
        }
        timer.start()
        val map: HashMap<String,String> = HashMap(1)
        map[REQ_BODY_NAME_CEL] = userContact!!
        if (userToken != 0 && userContact?.isNotEmpty()!!){
            HttpRequestsUtils.httpGetTokenStatusRequest(HttpConstants.SERVICE_REQUEST_TOKEN, map,this)
        }
    }

    override fun onSucceed(responseString: String?, contact: String?, requestName: String?) {
        val userToken = Gson().fromJson(responseString, UserToken::class.java)
        if (userToken.table[0].tknNum != 0 && userToken.table[0].expiryVal == TOKEN_EXPIRED){
            val map: HashMap<String,String> = HashMap(3)
            map[REQ_BODY_NAME_CEL] = userContact!!
            map[HttpConstants.REQ_BODY_NAME_APP] = HttpConstants.REQ_APP
            map[HttpConstants.REQ_BODY_NAME_DEVICE_DETAILS] = Build.MODEL
            HttpRequestsUtils.httpTokenUpdateRequest(
                HttpConstants.SERVICE_REQUEST_TOKEN_UPDATE, map, this)
        }else{
            runOnUiThread {
                progressBar.progress = 100
                setUserToken(userToken.table[0].tknNum)
                setUserContactUpdated(userToken.table2[0].count)
                startActivity(Intent(applicationContext,  HomePage::class.java))
                finish()
            }
        }
    }

    override fun onFailure(message: String?) {
        runOnUiThread {
            Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
        }
    }
}