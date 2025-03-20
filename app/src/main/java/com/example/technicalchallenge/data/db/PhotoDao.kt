package com.example.technicalchallenge.data.db

import androidx.paging.PagingSource
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query

@Dao
interface PhotoDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertPhotos(photos: List<Photo>)

    @Query("SELECT * FROM photos ORDER BY id ASC")
    fun getPagedPhotos(): PagingSource<Int, Photo>
}