package com.example.pagination.data.photos.local

import com.example.pagination.BuildConfig
import com.example.pagination.data.common.db.PaginationDatabase
import dagger.Lazy
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Module
class PhotosStorageModule {
    @Provides
    @Singleton
    fun bindPhotoStorage(db: Lazy<PaginationDatabase>): PhotosStorage {
        return PhotosStorageImpl(BuildConfig.PAGE_SIZE, db)
    }
}