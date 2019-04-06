package com.example.pagination.data.photos.remote

import io.reactivex.Single
import javax.inject.Inject


class PagedApiImpl
@Inject constructor(
    private val pageSize: Int,
    private val api: PhotosApi
) : PagedApi {

    override fun fetch(page: Int): Single<List<PhotosResponse>> {
        return api.photos()
            .map {
                it.subList(pageSize * (page - 1), pageSize * page)
            }
    }
}