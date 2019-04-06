package com.example.pagination.data.photos.local

import android.arch.persistence.room.Dao
import android.arch.persistence.room.Insert
import android.arch.persistence.room.OnConflictStrategy
import android.arch.persistence.room.Query


@Dao
interface PhotosDao {
    @Query("SELECT * FROM photos LIMIT :limit OFFSET :offset")
    fun fetch(limit: Int, offset: Int): List<PhotoEntity>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(photos: List<PhotoEntity>)
}