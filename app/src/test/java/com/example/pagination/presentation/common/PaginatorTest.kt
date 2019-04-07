package com.example.pagination.presentation.common

import android.arch.core.executor.testing.InstantTaskExecutorRule
import android.arch.lifecycle.MutableLiveData
import com.example.pagination.common.SchedulersProvider
import com.example.pagination.data.common.network.NoInternetException
import com.example.pagination.domain.photos.Photo
import com.example.pagination.presentation.photos.State
import com.example.pagination.presentation.photos.ViewStateController
import io.reactivex.Scheduler
import io.reactivex.Single
import io.reactivex.schedulers.Schedulers
import org.assertj.core.api.Assertions.assertThat
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.rules.TestRule

class PaginatorTest {
    @get: Rule
    var rule: TestRule = InstantTaskExecutorRule()
    private lateinit var schedulersProvider: SchedulersProvider

    @Before
    fun setUp() {
        schedulersProvider = object : SchedulersProvider {
            override fun io(): Scheduler {
                return Schedulers.trampoline()
            }

            override fun computation(): Scheduler {
                return Schedulers.trampoline()
            }

            override fun main(): Scheduler {
                return Schedulers.trampoline()
            }
        }
    }

    @Test
    fun `restart new paginator should doesn't change state`() {
        val liveData = MutableLiveData<State>().apply { value = State() }
        val paginator = Paginator(ViewStateController(liveData), schedulersProvider) {
            Single.just(emptyList())
        }
        paginator.restart()
        assertThat(liveData.value).isEqualTo(State())
    }

    @Test
    fun `refresh new paginator should load data and change state`() {
        val states = mutableListOf<State>()
        val liveData = MutableLiveData<State>().apply { value = State() }
        liveData.observeForever {
            states.add(it!!)
        }
        val element = Photo(albumId = 1, id = 1, title = "title1", url = "url1", thumbnailUrl = "thumbnailUrl1")

        val paginator = Paginator(ViewStateController(liveData), schedulersProvider) {
            Single.just(listOf(element))
        }
        paginator.refresh()

        assertThat(states).hasSize(4)
        assertThat(states).containsAll(
            listOf(
                State(),
                State(emptyProgress = true),
                State(showData = true, data = listOf(element), emptyProgress = true),
                State(showData = true, data = listOf(element), emptyProgress = false)
            )
        )
    }

    @Test
    fun `refresh new paginator without internet should change state to error`() {
        val states = mutableListOf<State>()
        val liveData = MutableLiveData<State>().apply { value = State() }
        liveData.observeForever {
            states.add(it!!)
        }
        val paginator = Paginator(ViewStateController(liveData), schedulersProvider) {
            Single.error(NoInternetException)
        }
        paginator.refresh()

        assertThat(states).hasSize(4)
        assertThat(states).containsAll(
            listOf(
                State(),
                State(emptyProgress = true),
                State(emptyProgress = false),
                State(showEmptyError = true, error = NoInternetException)
            )
        )
    }

    @Test
    fun `paginator should continue after the appearance of the internet`() {
        val states = mutableListOf<State>()
        val liveData = MutableLiveData<State>().apply { value = State() }
        liveData.observeForever {
            states.add(it!!)
        }
        val element = Photo(albumId = 1, id = 1, title = "title1", url = "url1", thumbnailUrl = "thumbnailUrl1")
        var first = true
        val paginator = Paginator(ViewStateController(liveData), schedulersProvider) {
            return@Paginator if (first) {
                first = false
                Single.error(NoInternetException)
            } else {
                Single.just(listOf(element))
            }
        }
        paginator.refresh()
        paginator.restart()
        assertThat(states).hasSize(8)
        assertThat(states).containsAll(
            listOf(
                State(),
                State(emptyProgress = true),
                State(emptyProgress = false),
                State(showEmptyError = true, error = NoInternetException),
                State(),
                State(emptyProgress = true),
                State(showData = true, data = listOf(element), emptyProgress = true),
                State(showData = true, data = listOf(element), emptyProgress = false)
            )
        )
    }

    @Test
    fun `paginator should continue after internet is restored and user click retry`() {
        val states = mutableListOf<State>()
        val liveData = MutableLiveData<State>().apply { value = State() }
        liveData.observeForever {
            states.add(it!!)
        }
        val element = Photo(albumId = 1, id = 1, title = "title1", url = "url1", thumbnailUrl = "thumbnailUrl1")
        var step = 1
        val paginator = Paginator(ViewStateController(liveData), schedulersProvider) {
            when (step) {
                1 -> {
                    step = 2
                    return@Paginator Single.just(listOf(element))
                }
                2 -> {
                    step = 3
                    return@Paginator Single.error(NoInternetException)
                }
                else -> return@Paginator Single.just(listOf(element))
            }
        }
        paginator.refresh()
        paginator.loadNewPage()
        paginator.loadNewPage()
        assertThat(states).hasSize(10)
        assertThat(states).containsExactlyElementsOf(
            listOf(
                State(),
                State(emptyProgress = true),
                State(emptyProgress = true, showData = true, data = listOf(element)),
                State(showData = true, data = listOf(element)),
                State(pageProgress = true, showData = true, data = listOf(element)),
                State(showData = true, data = listOf(element)),
                State(showData = true, data = listOf(element), showPageError = true, error = NoInternetException),
                State(
                    pageProgress = true,
                    showData = true,
                    data = listOf(element),
                    showPageError = false,
                    error = NoInternetException
                ),
                State(showData = true, data = listOf(element), showPageError = false, error = NoInternetException),
                State(
                    showData = true,
                    data = listOf(element, element),
                    showPageError = false,
                    error = NoInternetException
                )

            )
        )
    }
}