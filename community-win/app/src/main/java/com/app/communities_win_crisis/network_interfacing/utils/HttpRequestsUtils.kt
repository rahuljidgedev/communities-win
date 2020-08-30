package com.app.communities_win_crisis.network_interfacing.utils

import com.app.communities_win_crisis.presentor.VendorPresenter
import com.app.communities_win_crisis.ui_activities.home_page_ui.HomePresenter
import com.app.communities_win_crisis.ui_activities.home_screen.AppHomePresenter
import com.app.communities_win_crisis.ui_activities.make_a_list_ui.MakeAListPresenter
import com.app.communities_win_crisis.ui_activities.splash_screen_ui.presenter.SplashPresenter
import com.google.gson.Gson
import okhttp3.*
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.RequestBody.Companion.toRequestBody
import java.io.IOException

class HttpRequestsUtils {

    companion object {

        fun httpRequestGetTokenStatus(url: String, map: HashMap<String, String>, context: Any)
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

        fun httpRequestTokenUpdate(url: String, map: HashMap<String, String>, context: Any)
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
                    when (context) {
                        is SplashPresenter -> {
                            context.onFailure(e.message)
                        }/*else if (context is HomePresenter) {
                                        context.onFailure(e.message)
                                    }*/
                        is MakeAListPresenter -> {
                            context.onFailure(e.message)
                        }
                        is AppHomePresenter -> {
                            context.onFailure(e.message)
                        }
                    }
                }

                override fun onResponse(call: Call, response: Response) {
                    if (response.isSuccessful){
                        when (context) {
                            is SplashPresenter -> {
                                context.onSucceed(
                                    response.body!!.string(),
                                    map[HttpConstants.REQ_BODY_NAME_CEL],
                                    HttpConstants.SERVICE_REQUEST_TOKEN_UPDATE
                                )
                            }/*else if (context is HomePresenter) {
                                                context.onSucceed(
                                                    response.body!!.string(),
                                                    map[HttpConstants.REQ_BODY_NAME_CEL],
                                                    HttpConstants.SERVICE_REQUEST_TOKEN_UPDATE
                                                )
                                            }*/
                            is MakeAListPresenter -> {
                                context.onSucceed(
                                    response.body!!.string(),
                                    map[HttpConstants.REQ_BODY_NAME_CEL],
                                    HttpConstants.SERVICE_REQUEST_TOKEN_UPDATE
                                )
                            }
                            is AppHomePresenter -> {
                                context.onSucceed(
                                    response.body!!.string(),
                                    map[HttpConstants.REQ_BODY_NAME_CEL],
                                    HttpConstants.SERVICE_REQUEST_TOKEN_UPDATE
                                )
                            }
                        }
                    }else{
                        when (context) {
                            is SplashPresenter -> {
                                context.onFailure(response.body!!.string())
                            }/*else if (context is HomePresenter) {
                                                context.onFailure(response.body!!.string())
                                            }*/
                            is MakeAListPresenter -> {
                                context.onFailure(response.body!!.string())
                            }
                            is AppHomePresenter -> {
                                context.onFailure(response.body!!.string())
                            }
                        }
                    }
                }
            })
        }

        fun httpRequestUserActiveConnectionList(url: String, map: HashMap<String, String>, context: Any)
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

        fun httpRequestUserAnonymousContactUpload(url: String, map: HashMap<String, String>, context: Any)
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

        /*Vendor related Requests*/
        fun httpRequestVendorRegisterAddUpdate(url: String, map: HashMap<String, String>, context: Any)
                = run {
            val client = OkHttpClient()
            val jsonString: String  = Gson().toJson(map)

            val requestBody =
                jsonString.toRequestBody("application/json; charset=utf-8".toMediaTypeOrNull())

            val request = Request.Builder()
                .url(url)
                .header(HttpConstants.REQ_HEADER_API_KEY,HttpConstants.REQ_APP)
                .post(requestBody)
                .build()
            client.newCall(request).enqueue(object: Callback {
                override fun onFailure(call: Call, e: IOException) {
                    /*(context as VendorPresenter).onFailure(e.message)*/
                    (context as AppHomePresenter).onFailure(e.message)
                }

                override fun onResponse(call: Call, response: Response) {
                    /*(context as VendorPresenter).onSucceed(response.body!!.string(), url)*/
                    (context as AppHomePresenter).onSucceed(response.body!!.string(), url)
                }
            })
        }

        fun httpRequestVendorCategory(url: String, map: HashMap<String, String>, context: Any)
                = run {
            val client = OkHttpClient()
            val jsonString: String  = Gson().toJson(map)

            val requestBody =
                jsonString.toRequestBody("application/json; charset=utf-8".toMediaTypeOrNull())

            val request = Request.Builder()
                .url(url)
                .header(HttpConstants.REQ_HEADER_API_KEY,HttpConstants.REQ_APP)
                .post(requestBody)
                .build()
            client.newCall(request).enqueue(object: Callback {
                override fun onFailure(call: Call, e: IOException) {
                    (context as VendorPresenter).onFailure(e.message)
                }

                override fun onResponse(call: Call, response: Response) {
                    (context as VendorPresenter).onSucceed(response.body!!.string(), url)
                }
            })
        }

        fun httpRequestVendorProductList(url: String, context: Any)
                = run {
            val client = OkHttpClient()

            val request = Request.Builder()
                .header(HttpConstants.REQ_HEADER_API_KEY,HttpConstants.REQ_APP)
                .url(url)
                .build()
            client.newCall(request).enqueue(object: Callback {
                override fun onFailure(call: Call, e: IOException) {
                    if(context is AppHomePresenter)
                    /*if(context is VendorPresenter)*/
                        context.onFailure(e.message)
                    else if(context is MakeAListPresenter)
                        context.onFailure(e.message)
                }

                override fun onResponse(call: Call, response: Response) {
                    if(context is AppHomePresenter)
                    /*if(context is VendorPresenter)*/
                        context.onSucceed(response.body!!.string(), url)
                    else if(context is MakeAListPresenter)
                        context.onSucceed(response.body!!.string(), url)
                }
            })
        }

        fun httpRequestProductPricesVendor(url: String, map: HashMap<String, String>, context: Any)
                = run {
            val client = OkHttpClient()
            val jsonString: String  = Gson().toJson(map)

            val requestBody =
                jsonString.toRequestBody("application/json; charset=utf-8".toMediaTypeOrNull())

            val request = Request.Builder()
                .url(url)
                .header(HttpConstants.REQ_HEADER_API_KEY,HttpConstants.REQ_APP)
                .post(requestBody)
                .build()
            client.newCall(request).enqueue(object: Callback {
                override fun onFailure(call: Call, e: IOException) {
                    /*(context as VendorPresenter).onFailure(e.message)*/
                    (context as AppHomePresenter).onFailure(e.message)
                }

                override fun onResponse(call: Call, response: Response) {
                    /*(context as VendorPresenter).onSucceed(response.body!!.string(), url)*/
                    (context as AppHomePresenter).onSucceed(response.body!!.string(), url)
                }
            })
        }

        fun httpRequestVendorProductPriceList(url: String, map: HashMap<String, String>, context: Any)
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
                override fun onFailure(call: Call, e: IOException) {}

                override fun onResponse(call: Call, response: Response) {}
            })
        }

        fun httpRequestVendorCoronaPrecautionsUpdate(url: String, map: HashMap<String, String>, context: Any)
                = run {
            val client = OkHttpClient()
            val jsonString: String  = Gson().toJson(map)

            val requestBody =
                jsonString.toRequestBody("application/json; charset=utf-8".toMediaTypeOrNull())

            val request = Request.Builder()
                .url(url)
                .header(HttpConstants.REQ_HEADER_API_KEY,HttpConstants.REQ_APP)
                .post(requestBody)
                .build()
            client.newCall(request).enqueue(object: Callback {
                override fun onFailure(call: Call, e: IOException) {
                    /*(context as VendorPresenter).onFailure(e.message)*/
                    (context as AppHomePresenter).onFailure(e.message)
                }

                override fun onResponse(call: Call, response: Response) {
                    /*(context as VendorPresenter).onSucceed(response.body!!.string(), url)*/
                    (context as AppHomePresenter).onSucceed(response.body!!.string(), url)
                }
            })
        }

        fun httpRequestGetVendor(url: String, map: HashMap<String, String>, context: Any)
                = run {
            val client = OkHttpClient()
            //val jsonString: String  = Gson().toJson(map)

            var modifiedServiceUrl: String = "$url?"
            for ((k,v) in map){
                modifiedServiceUrl = "$modifiedServiceUrl$k=$v&"
            }

            val request = Request.Builder()
                .url(modifiedServiceUrl)
                .header(HttpConstants.REQ_HEADER_API_KEY,HttpConstants.REQ_APP)
                .build()
            client.newCall(request).enqueue(object: Callback {
                override fun onFailure(call: Call, e: IOException) {
                    /*(context as VendorPresenter).onFailure(e.message)*/
                    (context as AppHomePresenter).onFailure(e.message)
                }

                override fun onResponse(call: Call, response: Response) {
                    /*(context as VendorPresenter).onSucceed(response.body!!.string(), url)*/
                    (context as AppHomePresenter).onSucceed(response.body!!.string(), url)
                }
            })
        }

        fun httpRequestUploadUserList(url: String, map: java.util.HashMap<String, Any?>, context: Any)
            = run {
                val client = OkHttpClient()
                val jsonString: String  = Gson().toJson(map)

                val requestBody =
                    jsonString.toRequestBody("application/json; charset=utf-8".toMediaTypeOrNull())

                val request = Request.Builder()
                    .url(url)
                    .header(HttpConstants.REQ_HEADER_API_KEY,HttpConstants.REQ_APP)
                    .post(requestBody)
                    .build()
                client.newCall(request).enqueue(object: Callback {
                    override fun onFailure(call: Call, e: IOException) {
                        (context as MakeAListPresenter).onFailure(e.message)
                    }

                    override fun onResponse(call: Call, response: Response) {
                        (context as MakeAListPresenter).onSucceed(response.body!!.string(), url)
                    }
                })
        }
    }
}