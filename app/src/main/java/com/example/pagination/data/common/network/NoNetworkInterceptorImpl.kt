package com.example.pagination.data.common.network

import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkInfo
import okhttp3.Interceptor
import okhttp3.Response
import javax.inject.Inject

class NoNetworkInterceptorImpl @Inject constructor(private val context: Context) : NoNetworkInterceptor {
    override fun intercept(chain: Interceptor.Chain): Response {
        if (!isNetworkAvailable()) {
            throw NoInternetException()
        }

        return chain.proceed(chain.request())
    }

    private fun isNetworkAvailable(): Boolean {
        val connectivityManager = context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val activeNetworkInfo = connectivityManager.activeNetworkInfo
        return !(activeNetworkInfo == null || activeNetworkInfo.state != NetworkInfo.State.CONNECTED)
    }
}