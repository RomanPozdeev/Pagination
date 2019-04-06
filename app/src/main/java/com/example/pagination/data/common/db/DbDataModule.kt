package com.example.pagination.data.common.db

import android.arch.persistence.room.Room
import android.content.Context
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Module
class DbDataModule {
    @Provides
    @Singleton
    fun provideDatabase(context: Context): PaginationDatabase {
        return Room.databaseBuilder(
            context,
            PaginationDatabase::class.java,
            "pagination.db"
        ).build()
    }
}