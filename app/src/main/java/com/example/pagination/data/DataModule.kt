package com.example.pagination.data

import com.example.pagination.data.common.CommonDataModule
import com.example.pagination.data.photos.PhotosDataModule
import dagger.Module

@Module(
    includes = [
        CommonDataModule::class,
        PhotosDataModule::class
    ]
)
interface DataModule