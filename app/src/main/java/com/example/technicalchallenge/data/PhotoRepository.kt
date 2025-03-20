package com.example.technicalchallenge.data

import androidx.paging.PagingData
import com.example.technicalchallenge.data.db.Photo
import kotlinx.coroutines.flow.Flow

interface PhotoRepository {
    suspend fun fetchAndStorePhotos()
    fun getPagedPhotos(): Flow<PagingData<Photo>>
}
