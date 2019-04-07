package com.example.pagination.utils

import android.arch.lifecycle.ViewModel
import android.arch.lifecycle.ViewModelProvider
import android.arch.lifecycle.ViewModelProviders
import android.support.v4.app.FragmentActivity
import android.view.View
import com.example.pagination.data.common.network.NoInternetException
import java.net.ConnectException
import java.net.SocketTimeoutException
import java.net.UnknownHostException
import java.util.concurrent.TimeoutException

inline fun <reified T : ViewModel> FragmentActivity.injectViewModel(factory: ViewModelProvider.Factory): T {
    return ViewModelProviders.of(this, factory)[T::class.java]
}

fun View.visible(visible: Boolean) {
    if (visible) {
        this.visibility = View.VISIBLE
    } else {
        this.visibility = View.GONE
    }
}

fun Throwable.isNetworkError(): Boolean {
    return (this is NoInternetException
            || this is TimeoutException
            || this is ConnectException
            || this is UnknownHostException
            || this is SocketTimeoutException
            || (this.cause != null && this.cause!!.isNetworkError()))
}

fun <T> List<T>.page(page: Int, pageSize: Int): List<T> {
    return if (pageSize * (page - 1) < this.size) {
        if (pageSize * page > this.size) {
            this
        } else {
            this.subList(pageSize * (page - 1), pageSize * page)
        }
    } else {
        emptyList()
    }
}
