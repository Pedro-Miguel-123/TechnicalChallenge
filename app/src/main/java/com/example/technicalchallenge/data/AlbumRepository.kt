package com.example.technicalchallenge.data

import androidx.paging.PagingData
import com.example.technicalchallenge.data.local.Photo
import kotlinx.coroutines.flow.Flow
import java.util.concurrent.atomic.AtomicBoolean

interface AlbumRepository {
    val isFetchInProgress: AtomicBoolean
    suspend fun fetchAndStoreAlbums()
    fun getPagedPhotos(): Flow<PagingData<Photo>>
    suspend fun getPhotoByPhotoId(photoId: Int): Photo
}
