package com.example.pagination.presentation.photos

import android.arch.lifecycle.MutableLiveData
import android.arch.lifecycle.ViewModel
import com.example.pagination.common.SchedulersProvider
import com.example.pagination.domain.photos.PhotosInteractor
import com.example.pagination.presentation.common.Paginator
import javax.inject.Inject

class PhotosViewModel @Inject constructor(
    interactor: PhotosInteractor,
    schedulers: SchedulersProvider
) : ViewModel() {

    val state = MutableLiveData<State>()
    private val paginator = Paginator(ViewStateController(state), schedulers) { page ->
        interactor.fetch(page)
    }

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
}