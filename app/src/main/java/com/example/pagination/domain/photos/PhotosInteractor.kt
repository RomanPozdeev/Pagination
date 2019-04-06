package com.example.pagination.domain.photos

import io.reactivex.Single

interface PhotosInteractor {
    fun fetch(page: Int): Single<List<Photo>>
}