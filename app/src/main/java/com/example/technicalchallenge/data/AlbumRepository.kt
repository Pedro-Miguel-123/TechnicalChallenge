package com.example.technicalchallenge.data

import androidx.paging.PagingData
import com.example.technicalchallenge.data.local.Photo
import kotlinx.coroutines.flow.Flow

interface AlbumRepository {
    suspend fun fetchAndStoreAlbums()
    fun getPagedPhotos(): Flow<PagingData<Photo>>
    suspend fun getPhotoByPhotoId(photoId: Int): Photo
    fun setFetchInProgress(value: Boolean)
}
