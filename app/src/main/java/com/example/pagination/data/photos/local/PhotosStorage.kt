package com.example.pagination.data.photos.local

import com.example.pagination.domain.photos.Photo
import io.reactivex.Single

interface PhotosStorage {
    fun fetch(page: Int): Single<List<Photo>>
    fun saveBlocking(data: List<Photo>)
}