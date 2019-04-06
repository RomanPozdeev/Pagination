package com.example.pagination.data.common

import com.example.pagination.data.common.db.DbDataModule
import com.example.pagination.data.common.network.NetworkDataModule
import dagger.Module

@Module(
    includes = [
        DbDataModule::class,
        NetworkDataModule::class
    ]
)
interface CommonDataModule