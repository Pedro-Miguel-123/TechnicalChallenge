package com.example.technicalchallenge.data

import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import com.example.technicalchallenge.data.api.LebonCoinAPIService
import com.example.technicalchallenge.data.local.Photo
import com.example.technicalchallenge.data.local.PhotoDao
import com.example.technicalchallenge.data.network.NetworkMonitor
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch
import timber.log.Timber

class AlbumRepositoryImpl(
    private val photoDao: PhotoDao,
    networkMonitor: NetworkMonitor,
    private val lebonCoinApiService: LebonCoinAPIService,
    private val coroutineScope: CoroutineScope = CoroutineScope(Dispatchers.IO)
): AlbumRepository {

    init {
        networkMonitor.register()
        networkMonitor.setOnNetworkAvailable {
            coroutineScope.launch {
                fetchAndStoreAlbums()
            }
        }
    }

    override suspend fun fetchAndStoreAlbums() {
        try {
            val response = lebonCoinApiService.fetchPhotos()
            photoDao.insertPhotos(response)
        } catch (e: Exception) {
            Timber.e("Repository -> ${e.message}")
        }
    }

    override fun getPagedPhotos(): Flow<PagingData<Photo>> {
        return Pager(
            config = PagingConfig(
                pageSize = 20,
                enablePlaceholders = false
            ),
            pagingSourceFactory = { photoDao.getPagedPhotos() }
        ).flow
    }
}
