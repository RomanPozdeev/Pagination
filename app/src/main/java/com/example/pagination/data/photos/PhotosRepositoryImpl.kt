package com.example.pagination.data.photos

import com.example.pagination.data.photos.local.PhotosStorage
import com.example.pagination.data.photos.remote.PagedApi
import com.example.pagination.domain.photos.Photo
import com.example.pagination.domain.photos.PhotosRepository
import io.reactivex.Single
import javax.inject.Inject

class PhotosRepositoryImpl
@Inject constructor(
    private val pageSize: Int,
    private val api: PagedApi,
    private val storage: PhotosStorage
) : PhotosRepository {

    override fun fetch(page: Int): Single<List<Photo>> {
        return storage.fetch(page)
            .flatMap { local ->
                return@flatMap if (local.size < pageSize) {
                    fetchFromRemote(page)
                        .doOnSuccess { storage.saveBlocking(it) }
                } else {
                    Single.just(local)
                }
            }
    }

    private fun fetchFromRemote(page: Int): Single<List<Photo>> {
        return api.fetch(page)
            .map { list ->
                list.map {
                    Photo(
                        albumId = it.albumId,
                        id = it.id,
                        title = it.title,
                        thumbnailUrl = it.thumbnailUrl,
                        url = it.url
                    )
                }
            }
    }
}