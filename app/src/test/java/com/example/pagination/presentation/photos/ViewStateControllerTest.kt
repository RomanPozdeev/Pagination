package com.example.pagination.presentation.photos

import android.arch.core.executor.testing.InstantTaskExecutorRule
import android.arch.lifecycle.MutableLiveData
import com.example.pagination.domain.photos.Photo
import org.assertj.core.api.Assertions.assertThat
import org.junit.Rule
import org.junit.Test
import org.junit.rules.TestRule


class ViewStateControllerTest {
    @get: Rule
    var rule: TestRule = InstantTaskExecutorRule()

    @Test
    fun showEmptyProgress() {
        val liveData = MutableLiveData<State>().apply { value = State() }
        val controller = ViewStateController(liveData)
        controller.showEmptyProgress(true)
        assertThat(liveData.value).isEqualTo(State(emptyProgress = true))
        controller.showEmptyProgress(false)
        assertThat(liveData.value).isEqualTo(State(emptyProgress = false))
    }

    @Test
    fun showEmptyView() {
        val liveData = MutableLiveData<State>().apply { value = State() }
        val controller = ViewStateController(liveData)
        controller.showEmptyView(true)
        assertThat(liveData.value).isEqualTo(State(emptyView = true))
        controller.showEmptyView(false)
        assertThat(liveData.value).isEqualTo(State(emptyView = false))
    }

    @Test
    fun showData() {
        val liveData = MutableLiveData<State>().apply { value = State() }
        val controller = ViewStateController(liveData)
        val element = Photo(albumId = 1, id = 1, title = "title1", url = "url1", thumbnailUrl = "thumbnailUrl1")
        controller.showData(true, listOf(element))
        assertThat(liveData.value).isEqualTo(State(showData = true, data = listOf(element)))
        controller.showData(false, listOf(element))
        assertThat(liveData.value).isEqualTo(State(showData = false, data = listOf(element)))
        controller.showData(false, emptyList())
        assertThat(liveData.value).isEqualTo(State(showData = false, data = emptyList()))
    }

    @Test
    fun showRefreshProgress() {
        val liveData = MutableLiveData<State>().apply { value = State() }
        val controller = ViewStateController(liveData)
        controller.showRefreshProgress(true)
        assertThat(liveData.value).isEqualTo(State(refreshProgress = true))
        controller.showRefreshProgress(false)
        assertThat(liveData.value).isEqualTo(State(refreshProgress = false))
    }

    @Test
    fun showPageProgress() {
        val liveData = MutableLiveData<State>().apply { value = State() }
        val controller = ViewStateController(liveData)
        controller.showPageProgress(true)
        assertThat(liveData.value).isEqualTo(State(pageProgress = true))
        controller.showPageProgress(false)
        assertThat(liveData.value).isEqualTo(State(pageProgress = false))
    }

    @Test
    fun processError() {
        val liveData = MutableLiveData<State>().apply { value = State() }
        val controller = ViewStateController(liveData)
        controller.processError(RuntimeException("test"))
        assertThat(liveData.value)
            .isEqualToComparingFieldByFieldRecursively(State(showPageError = true, error = RuntimeException("test")))
    }

    @Test
    fun processEmptyError() {
        val liveData = MutableLiveData<State>().apply { value = State() }
        val controller = ViewStateController(liveData)
        controller.processEmptyError(true, RuntimeException("test"))
        assertThat(liveData.value)
            .isEqualToComparingFieldByFieldRecursively(State(showEmptyError = true, error = RuntimeException("test")))
        controller.processEmptyError(false)
        assertThat(liveData.value).isEqualToComparingFieldByFieldRecursively(
            State(
                showEmptyError = false,
                error = null
            )
        )
    }
}