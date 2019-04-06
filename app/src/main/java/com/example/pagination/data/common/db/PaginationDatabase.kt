package com.example.pagination.data.common.db

import android.arch.persistence.room.Database
import android.arch.persistence.room.RoomDatabase
import com.example.pagination.data.photos.local.PhotoEntity
import com.example.pagination.data.photos.local.PhotosDao

@Database(entities = [PhotoEntity::class], version = 1, exportSchema = false)
abstract class PaginationDatabase : RoomDatabase() {
    abstract fun photosDao(): PhotosDao
}