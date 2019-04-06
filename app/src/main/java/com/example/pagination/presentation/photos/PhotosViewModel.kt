package com.example.pagination.presentation.photos

import android.arch.lifecycle.MutableLiveData
import android.arch.lifecycle.ViewModel
import com.example.pagination.common.SchedulersProvider
import com.example.pagination.domain.photos.Photo
import com.example.pagination.domain.photos.PhotosInteractor
import com.example.pagination.presentation.common.Paginator
import javax.inject.Inject

class PhotosViewModel @Inject constructor(
    interactor: PhotosInteractor,
    schedulers: SchedulersProvider
) : ViewModel(),
    Paginator.ViewController<Photo> {

    private val paginator = Paginator(this, schedulers) { page ->
        interactor.fetch(page)
    }

    val state = MutableLiveData<State>()

    init {
        state.value = State()
        paginator.refresh()
    }

    override fun onCleared() {
        paginator.release()
        super.onCleared()
    }

    fun loadMore() {
        paginator.loadNewPage()
    }

    fun restart() {
        paginator.restart()
    }

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
        state.value!!.copy(pageProgress = show).also { state.value = it }
    }

    override fun processError(error: Throwable) {
        state.value!!.copy(showPageError = true, error = error).also { state.value = it }
    }

    override fun processEmptyError(show: Boolean, error: Throwable?) {
        state.value!!.copy(showEmptyError = show, error = error).also { state.value = it }
    }

}