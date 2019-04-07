package com.example.pagination.data.photos.remote

import io.reactivex.Single

interface PagedApi {
    fun fetch(page: Int): Single<List<PhotoResponse>>
}