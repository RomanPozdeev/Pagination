package com.example.pagination.data.photos

import com.example.pagination.BuildConfig
import com.example.pagination.data.photos.local.PhotosStorage
import com.example.pagination.data.photos.local.PhotosStorageModule
import com.example.pagination.data.photos.remote.PagedApi
import com.example.pagination.data.photos.remote.PhotosApiModule
import com.example.pagination.domain.photos.PhotosRepository
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Module(
    includes = [
        PhotosStorageModule::class,
        PhotosApiModule::class
    ]
)
class PhotosDataModule {
    @Provides
    @Singleton
    fun providePhotosRepository(api: PagedApi, storage: PhotosStorage): PhotosRepository {
        return PhotosRepositoryImpl(BuildConfig.PAGE_SIZE, api, storage)
    }
}