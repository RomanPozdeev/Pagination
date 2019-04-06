package com.example.pagination.domain.photos

import io.reactivex.Single
import javax.inject.Inject

class PhotosInteractorImpl
@Inject constructor(private val repository: PhotosRepository) : PhotosInteractor {

    override fun fetch(page: Int): Single<List<Photo>> {
        return repository.fetch(page)
    }
}