package com.example.pagination.presentation.photos

import android.arch.lifecycle.MutableLiveData
import com.example.pagination.domain.photos.Photo
import com.example.pagination.presentation.common.Paginator

class ViewStateController(private val state: MutableLiveData<State>) : Paginator.ViewController<Photo> {
    override fun showEmptyProgress(show: Boolean) {
        state.value!!.copy(emptyProgress = show).also { state.value = it }
    }

    override fun showEmptyView(show: Boolean) {
        state.value!!.copy(emptyView = show).also { state.value = it }
    }

    override fun showData(show: Boolean, data: List<Photo>) {
        state.value!!.copy(showData = show, data = data.toList()).also { state.value = it }
    }

    override fun showRefreshProgress(show: Boolean) {
        state.value!!.copy(refreshProgress = show).also { state.value = it }
    }

    override fun showPageProgress(show: Boolean) {
        state.value!!.copy(pageProgress = show, showPageError = false).also { state.value = it }
    }

    override fun processError(error: Throwable) {
        state.value!!.copy(showPageError = true, error = error).also { state.value = it }
    }

    override fun processEmptyError(show: Boolean, error: Throwable?) {
        state.value!!.copy(showEmptyError = show, error = error).also { state.value = it }
    }
}