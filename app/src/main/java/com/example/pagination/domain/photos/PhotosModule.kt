package com.example.pagination.domain.photos

import dagger.Binds
import dagger.Module

@Module
interface PhotosModule {
    @Binds
    fun bind(impl: PhotosInteractorImpl): PhotosInteractor
}