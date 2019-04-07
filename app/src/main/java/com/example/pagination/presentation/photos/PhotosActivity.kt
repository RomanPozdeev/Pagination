package com.example.pagination.presentation.photos

import android.arch.lifecycle.Observer
import android.arch.lifecycle.ViewModelProvider
import android.os.Bundle
import android.support.design.widget.Snackbar
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import com.example.pagination.R
import com.example.pagination.presentation.common.EndlessRecyclerViewScrollListener

import com.example.pagination.utils.injectViewModel
import com.example.pagination.utils.isNetworkError
import com.example.pagination.utils.visible
import dagger.android.support.DaggerAppCompatActivity
import kotlinx.android.synthetic.main.activity_main.*
import javax.inject.Inject

class PhotosActivity : DaggerAppCompatActivity() {

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory

    lateinit var viewModel: PhotosViewModel

    private var lastState: State? = null
    private var snackbar: Snackbar? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        items.hasFixedSize()
        items.adapter = PhotosAdapter()
        items.layoutManager = LinearLayoutManager(this, RecyclerView.VERTICAL, false)

        val listener = object : EndlessRecyclerViewScrollListener(items.layoutManager as LinearLayoutManager) {
            override fun onLoadMore(page: Int, totalItemsCount: Int, view: RecyclerView) {
                viewModel.loadMore()
            }
        }

        items.addOnScrollListener(listener)

        viewModel = injectViewModel(viewModelFactory)
        viewModel.state.observe(this, Observer(::renderState))
    }

    private fun renderState(newState: State?) {
        if (newState != null) {
            emptyView.visible(newState.emptyView)
            progress.visible(newState.emptyProgress)
            refresh.isRefreshing = newState.refreshProgress

            refresh.visible(newState.showData)

            if (newState.showData && lastState?.data != newState.data) {
                (items.adapter as PhotosAdapter).update(newState.data)
            }

            pageProgress.visible(newState.pageProgress)

            if (lastState?.showPageError != newState.showPageError) {
                if (newState.showPageError) {
                    showError(newState.error!!) { viewModel.loadMore() }
                } else {
                    snackbar?.dismiss()
                }
            }

            if (lastState?.showEmptyError != newState.showEmptyError) {
                if (newState.showEmptyError) {
                    showError(newState.error!!) { viewModel.restart() }
                } else {
                    snackbar?.dismiss()
                }
            }


            newState.showEmptyError
        }
        lastState = newState
    }

    private fun showError(throwable: Throwable, action: () -> Unit) {
        throwable.printStackTrace()
        snackbar?.dismiss()
        val message = if (throwable.isNetworkError()) {
            R.string.network_error
        } else {
            R.string.error
        }
        snackbar = Snackbar.make(coordinator, message, Snackbar.LENGTH_INDEFINITE)
            .setAction(R.string.retry) { action.invoke() }.apply { show() }
    }

}
