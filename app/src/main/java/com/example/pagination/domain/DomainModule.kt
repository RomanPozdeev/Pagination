package com.example.pagination.domain

import com.example.pagination.domain.photos.PhotosModule
import dagger.Module

@Module(
    includes = [
        PhotosModule::class
    ]
)
interface DomainModule