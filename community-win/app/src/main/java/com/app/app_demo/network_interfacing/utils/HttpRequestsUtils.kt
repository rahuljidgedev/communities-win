package com.app.app_demo.network_interfacing.utils

import android.content.Context
import com.app.app_demo.network_interfacing.data_models.PushedContact
import com.app.app_demo.ui_activities.HomePage
import com.app.app_demo.ui_activities.SplashActivity
import com.google.gson.Gson
import okhttp3.*
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import java.io.IOException

class HttpRequestsUtils {

    companion object {

        fun httpGetTokenStatusRequest(url: String, map: HashMap<String, String>, context: Context)
                = run {
            val client = OkHttpClient()
            val jsonString: String  = Gson().toJson(map)

            val requestBody = RequestBody.create(
                "application/json; charset=utf-8".toMediaTypeOrNull(), jsonString)

            val request = Request.Builder()
                .url(url)
                .post(requestBody)
                .build()
            client.newCall(request).enqueue(object: Callback {
                override fun onFailure(call: Call, e: IOException) {
                    (context as SplashActivity).onFailure(e.message)
                }

                override fun onResponse(call: Call, response: Response) {
                    if (response.isSuccessful) {
                        (context as SplashActivity).onSucceed(
                            response.body!!.string(),
                            map[HttpConstants.REQ_BODY_NAME_CEL],
                            HttpConstants.SERVICE_REQUEST_TOKEN
                        )
                    }else{
                        (context as SplashActivity).onFailure(response.body!!.string())
                    }
                }
            })
        }

        fun httpTokenUpdateRequest(url: String, map: HashMap<String, String>, context: Context)
                = run {
            val client = OkHttpClient()
            val jsonString: String  = Gson().toJson(map)

            val requestBody = RequestBody.create(
                "application/json; charset=utf-8".toMediaTypeOrNull(), jsonString)

            val request = Request.Builder()
                .url(url)
                .post(requestBody)
                .build()
            client.newCall(request).enqueue(object: Callback {
                override fun onFailure(call: Call, e: IOException) {
                    (context as HomePage).onFailure(e.message)
                }

                override fun onResponse(call: Call, response: Response) {
                    if (response.isSuccessful){
                        (context as HomePage).onSucceed(
                            response.body!!.string(),
                            map[HttpConstants.REQ_BODY_NAME_CEL],
                            HttpConstants.SERVICE_REQUEST_TOKEN_UPDATE
                        )
                    }else{
                        (context as HomePage).onFailure(response.body!!.string())
                    }
                }
            })
        }

        fun httpUserActiveConnectionListRequest(url: String, map: HashMap<String, String>, context: Context)
                = run {
            val client = OkHttpClient()

            var modifiedServiceUrl: String = "$url?"
            for ((k,v) in map){
                modifiedServiceUrl = "$modifiedServiceUrl$k=$v&"
            }

            val request = Request.Builder()
                .url(modifiedServiceUrl)
                .build()
            client.newCall(request).enqueue(object: Callback {
                override fun onFailure(call: Call, e: IOException) {
                    (context as HomePage).onFailure(e.message)
                }

                override fun onResponse(call: Call, response: Response) {
                    (context as HomePage).onSucceed(
                        response.body!!.string(),
                        map[HttpConstants.REQ_BODY_NAME_CEL],
                        HttpConstants.SERVICE_REQUEST_USER_CONTACT_LIST
                    )
                }
            })
        }

        fun httpUserAnonymousContactUploadRequest(url: String, map: HashMap<String, String>, context: Context)
                = run {
            val client = OkHttpClient()
            val jsonString: String  = Gson().toJson(map)

            val requestBody = RequestBody.create(
                "application/json; charset=utf-8".toMediaTypeOrNull(), jsonString)

            val request = Request.Builder()
                .url(url)
                .post(requestBody)
                .build()

            client.newCall(request).enqueue(object: Callback {
                override fun onFailure(call: Call, e: IOException) {
                    (context as HomePage).onFailure(e.message)
                }

                override fun onResponse(call: Call, response: Response) {
                    if (response.isSuccessful){
                        (context as HomePage).onSucceed(
                            response.body!!.string(),
                            map[HttpConstants.REQ_BODY_NAME_CEL],
                            HttpConstants.SERVICE_REQUEST_ANONYMOUS_CONTACT_UPLOAD
                        )
                    }else{
                        (context as HomePage).onFailure(response.body!!.string())
                    }
                }
            })
        }
    }
}