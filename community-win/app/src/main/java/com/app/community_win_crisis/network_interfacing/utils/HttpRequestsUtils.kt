package com.app.community_win_crisis.network_interfacing.utils

import com.app.community_win_crisis.ui.main.presentor.HomePresenter
import com.app.community_win_crisis.ui.main.presentor.SplashPresenter
import com.google.gson.Gson
import okhttp3.*
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.RequestBody.Companion.toRequestBody
import java.io.IOException

class HttpRequestsUtils {

    companion object {

        fun httpGetTokenStatusRequest(url: String, map: HashMap<String, String>, context: Any)
                = run {
            val client = OkHttpClient()
            val jsonString: String  = Gson().toJson(map)

            val requestBody =
                jsonString.toRequestBody("application/json; charset=utf-8".toMediaTypeOrNull())

            val request = Request.Builder()
                .url(url)
                .post(requestBody)
                .build()
            client.newCall(request).enqueue(object: Callback {
                override fun onFailure(call: Call, e: IOException) {
                    if (context is SplashPresenter){
                        context.onFailure(e.message)
                    }
                }

                override fun onResponse(call: Call, response: Response) {
                    if (response.isSuccessful) {
                        (context as SplashPresenter).onSucceed(
                            response.body!!.string(),
                            map[HttpConstants.REQ_BODY_NAME_CEL],
                            HttpConstants.SERVICE_REQUEST_TOKEN
                        )
                    }else{
                        if (context is SplashPresenter){
                            context.onFailure(response.body!!.string())
                        }
                    }
                }
            })
        }

        fun httpTokenUpdateRequest(url: String, map: HashMap<String, String>, context: Any)
                = run {
            val client = OkHttpClient()
            val jsonString: String  = Gson().toJson(map)

            val requestBody =
                jsonString.toRequestBody("application/json; charset=utf-8".toMediaTypeOrNull())

            val request = Request.Builder()
                .url(url)
                .post(requestBody)
                .build()
            client.newCall(request).enqueue(object: Callback {
                override fun onFailure(call: Call, e: IOException) {
                    if (context is SplashPresenter){
                        context.onFailure(e.message)
                    }else if (context is HomePresenter) {
                        context.onFailure(e.message)
                    }
                }

                override fun onResponse(call: Call, response: Response) {
                    if (response.isSuccessful){
                        if (context is SplashPresenter){
                            context.onSucceed(
                                response.body!!.string(),
                                map[HttpConstants.REQ_BODY_NAME_CEL],
                                HttpConstants.SERVICE_REQUEST_TOKEN_UPDATE
                            )
                        }else if (context is HomePresenter) {
                            context.onSucceed(
                                response.body!!.string(),
                                map[HttpConstants.REQ_BODY_NAME_CEL],
                                HttpConstants.SERVICE_REQUEST_TOKEN_UPDATE
                            )
                        }
                    }else{
                        if (context is SplashPresenter){
                            context.onFailure(response.body!!.string())
                        }else if (context is HomePresenter) {
                            context.onFailure(response.body!!.string())
                        }
                    }
                }
            })
        }

        fun httpUserActiveConnectionListRequest(url: String, map: HashMap<String, String>, context: Any)
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
                    if (context is HomePresenter)
                        context.onFailure(e.message)
                }

                override fun onResponse(call: Call, response: Response) {
                    if (context is HomePresenter){
                        context.onSucceed(
                            response.body!!.string(),
                            map[HttpConstants.REQ_BODY_NAME_CEL],
                            HttpConstants.SERVICE_REQUEST_USER_CONTACT_LIST
                        )
                    }
                }
            })
        }

        fun httpUserAnonymousContactUploadRequest(url: String, map: HashMap<String, String>, context: Any)
                = run {
            val client = OkHttpClient()
            val jsonString: String  = Gson().toJson(map)

            val requestBody =
                jsonString.toRequestBody("application/json; charset=utf-8".toMediaTypeOrNull())

            val request = Request.Builder()
                .url(url)
                .post(requestBody)
                .build()

            client.newCall(request).enqueue(object: Callback {
                override fun onFailure(call: Call, e: IOException) {
                    if (context is UploadAnonymousContactRequest)
                        context.onFailure(e.message)
                }

                override fun onResponse(call: Call, response: Response) {
                    if (response.isSuccessful){
                        if (context is UploadAnonymousContactRequest) {
                            context.onSucceed(
                                response.body!!.string(),
                                map[HttpConstants.REQ_BODY_NAME_CEL],
                                HttpConstants.SERVICE_REQUEST_ANONYMOUS_CONTACT_UPLOAD
                            )
                        }
                    }else{
                        if (context is UploadAnonymousContactRequest) {
                            context.onFailure(response.body!!.string())
                        }
                    }
                }
            })
        }
    }
}