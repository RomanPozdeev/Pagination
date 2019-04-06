package com.example.pagination.data.photos.local

import com.example.pagination.data.common.db.PaginationDatabase
import com.example.pagination.domain.photos.Photo
import dagger.Lazy
import io.reactivex.Completable
import io.reactivex.Single
import javax.inject.Inject

class PhotosStorageImpl @Inject constructor(
    private val pageSize: Int,
    private val db: Lazy<PaginationDatabase>
) : PhotosStorage {

    override fun fetch(page: Int): Single<List<Photo>> {
        return Single.fromCallable {
            db.get()
                .photosDao()
                .fetch(limit = pageSize, offset = (page - 1) * pageSize)
        }.map { list ->
            list.map {
                Photo(it.albumId, it.id, it.title, it.url, it.thumbnailUrl)
            }
        }
    }

    override fun saveBlocking(data: List<Photo>) {
        val mapped = data.map { PhotoEntity(it.id, it.albumId, it.title, it.url, it.thumbnailUrl) }
        db.get().runInTransaction {
            db.get()
                .photosDao()
                .insert(mapped)
        }
    }

    override fun save(data: List<Photo>): Completable {
        return Completable.fromAction {
            saveBlocking(data)
        }
    }
}