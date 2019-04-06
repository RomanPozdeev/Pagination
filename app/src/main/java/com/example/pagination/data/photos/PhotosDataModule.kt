package com.example.pagination.data.photos

import com.example.pagination.data.photos.local.PhotosStorageModule
import com.example.pagination.data.photos.remote.PhotosApiModule
import com.example.pagination.domain.photos.PhotosRepository
import dagger.Binds
import dagger.Module
import javax.inject.Singleton

@Module(
    includes = [
        PhotosStorageModule::class,
        PhotosApiModule::class
    ]
)
interface PhotosDataModule {
    @Binds
    @Singleton
    fun bind(impl: PhotosRepositoryImpl): PhotosRepository
}