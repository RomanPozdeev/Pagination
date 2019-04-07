package com.example.pagination.presentation.common

import com.example.pagination.common.SchedulersProvider
import io.reactivex.Single
import io.reactivex.disposables.Disposable

class Paginator<T>(
    private val viewController: ViewController<T>,
    private val schedulers: SchedulersProvider,
    private val requestFactory: (Int) -> Single<List<T>>
) {

    interface ViewController<T> {
        fun showEmptyProgress(show: Boolean)
        fun showEmptyView(show: Boolean)
        fun showData(show: Boolean, data: List<T> = emptyList())
        fun showRefreshProgress(show: Boolean)
        fun showPageProgress(show: Boolean)

        fun processError(error: Throwable)
        fun processEmptyError(show: Boolean, error: Throwable? = null)
    }

    private var currentState: State<T> = EMPTY()
    private var currentPage = 0
    private val currentData = mutableListOf<T>()
    private var disposable: Disposable? = null

    fun restart() {
        currentState.restart()
    }

    fun refresh() {
        currentState.refresh()
    }

    fun loadNewPage() {
        currentState.loadNewPage()
    }

    fun release() {
        currentState.release()
    }

    private fun loadPage(page: Int) {
        disposable?.dispose()
        disposable = requestFactory.invoke(page)
            .subscribeOn(schedulers.io())
            .observeOn(schedulers.main())
            .subscribe(
                { currentState.newData(it) },
                { currentState.fail(it) }
            )
    }

    private abstract inner class State<T> {
        open fun restart() {}
        open fun refresh() {}
        open fun loadNewPage() {}
        open fun newData(data: List<T>) {}
        open fun fail(error: Throwable) {}
        fun release() {
            currentState = RELEASED()
            disposable?.dispose()
        }
    }

    private inner class EMPTY : State<T>() {

        override fun refresh() {
            currentState = EMPTY_PROGRESS()
            viewController.showEmptyProgress(true)
            loadPage(FIRST_PAGE)
        }
    }

    private inner class EMPTY_PROGRESS : State<T>() {

        override fun restart() {
            loadPage(FIRST_PAGE)
        }

        override fun newData(data: List<T>) {
            if (data.isNotEmpty()) {
                currentState = DATA()
                currentData.clear()
                currentData.addAll(data)
                currentPage = FIRST_PAGE
                viewController.showData(true, currentData)
                viewController.showEmptyProgress(false)
            } else {
                currentState = EMPTY_DATA()
                viewController.showEmptyProgress(false)
                viewController.showEmptyView(true)
            }
        }

        override fun fail(error: Throwable) {
            currentState = EMPTY_ERROR()
            viewController.showEmptyProgress(false)
            viewController.processEmptyError(true, error)
        }
    }

    private inner class EMPTY_ERROR : State<T>() {

        override fun restart() {
            currentState = EMPTY_PROGRESS()
            viewController.processEmptyError(false)
            viewController.showEmptyProgress(true)
            loadPage(FIRST_PAGE)
        }

        override fun refresh() {
            currentState = EMPTY_PROGRESS()
            viewController.processEmptyError(false)
            viewController.showEmptyProgress(true)
            loadPage(FIRST_PAGE)
        }
    }

    private inner class EMPTY_DATA : State<T>() {

        override fun restart() {
            currentState = EMPTY_PROGRESS()
            viewController.showEmptyView(false)
            viewController.showEmptyProgress(true)
            loadPage(FIRST_PAGE)
        }

        override fun refresh() {
            currentState = EMPTY_PROGRESS()
            viewController.showEmptyView(false)
            viewController.showEmptyProgress(true)
            loadPage(FIRST_PAGE)
        }
    }

    private inner class DATA : State<T>() {

        override fun restart() {
            currentState = EMPTY_PROGRESS()
            viewController.showData(false)
            viewController.showEmptyProgress(true)
            loadPage(FIRST_PAGE)
        }

        override fun refresh() {
            currentState = REFRESH()
            viewController.showRefreshProgress(true)
            loadPage(FIRST_PAGE)
        }

        override fun loadNewPage() {
            currentState = PAGE_PROGRESS()
            viewController.showPageProgress(true)
            loadPage(currentPage + 1)
        }
    }

    private inner class REFRESH : State<T>() {

        override fun restart() {
            currentState = EMPTY_PROGRESS()
            viewController.showData(false)
            viewController.showRefreshProgress(false)
            viewController.showEmptyProgress(true)
            loadPage(FIRST_PAGE)
        }

        override fun newData(data: List<T>) {
            if (data.isNotEmpty()) {
                currentState = DATA()
                currentData.clear()
                currentData.addAll(data)
                currentPage = FIRST_PAGE
                viewController.showRefreshProgress(false)
                viewController.showData(true, currentData)
            } else {
                currentState = EMPTY_DATA()
                currentData.clear()
                viewController.showData(false)
                viewController.showRefreshProgress(false)
                viewController.showEmptyView(true)
            }
        }

        override fun fail(error: Throwable) {
            currentState = DATA()
            viewController.showRefreshProgress(false)
            viewController.processError(error)
        }
    }

    private inner class PAGE_PROGRESS : State<T>() {

        override fun restart() {
            currentState = EMPTY_PROGRESS()
            viewController.showData(false)
            viewController.showPageProgress(false)
            viewController.showEmptyProgress(true)
            loadPage(FIRST_PAGE)
        }

        override fun newData(data: List<T>) {
            if (data.isNotEmpty()) {
                currentState = DATA()
                currentData.addAll(data)
                currentPage++
                viewController.showPageProgress(false)
                viewController.showData(true, currentData)
            } else {
                currentState = ALL_DATA()
                viewController.showPageProgress(false)
            }
        }

        override fun refresh() {
            currentState = REFRESH()
            viewController.showPageProgress(false)
            viewController.showRefreshProgress(true)
            loadPage(FIRST_PAGE)
        }

        override fun fail(error: Throwable) {
            currentState = DATA()
            viewController.showPageProgress(false)
            viewController.processError(error)
        }
    }

    private inner class ALL_DATA : State<T>() {

        override fun restart() {
            currentState = EMPTY_PROGRESS()
            viewController.showData(false)
            viewController.showEmptyProgress(true)
            loadPage(FIRST_PAGE)
        }

        override fun refresh() {
            currentState = REFRESH()
            viewController.showRefreshProgress(true)
            loadPage(FIRST_PAGE)
        }
    }

    private inner class RELEASED : State<T>()

    companion object {
        const val FIRST_PAGE = 1
    }
}
