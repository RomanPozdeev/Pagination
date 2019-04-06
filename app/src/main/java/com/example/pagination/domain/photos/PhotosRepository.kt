package com.example.pagination.domain.photos

import io.reactivex.Single

interface PhotosRepository {
    fun fetch(page: Int): Single<List<Photo>>
}