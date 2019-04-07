package com.example.pagination.data.photos.remote

import com.example.pagination.utils.page
import io.reactivex.Single
import javax.inject.Inject


class PagedApiImpl
@Inject constructor(
    private val pageSize: Int,
    private val api: PhotosApi
) : PagedApi {

    override fun fetch(page: Int): Single<List<PhotoResponse>> {
        return api.photos()
            .map {
                return@map it.page(page, pageSize)
            }
    }
}