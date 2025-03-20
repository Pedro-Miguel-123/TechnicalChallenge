package com.example.technicalchallenge.data

import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import com.example.technicalchallenge.data.api.APIService
import com.example.technicalchallenge.data.db.Photo
import com.example.technicalchallenge.data.db.PhotoDao
import com.example.technicalchallenge.data.net.NetworkMonitor
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch
import timber.log.Timber

class PhotoRepositoryImpl(
    private val photoDao: PhotoDao,
    networkMonitor: NetworkMonitor,
    private val apiService: APIService,
    private val coroutineScope: CoroutineScope = CoroutineScope(Dispatchers.IO)
): PhotoRepository {

    init {
        networkMonitor.register()
        networkMonitor.setOnNetworkAvailable {
            coroutineScope.launch {
                fetchAndStorePhotos()
            }
        }
    }

    override suspend fun fetchAndStorePhotos() {
        try {
            val response = apiService.fetchPhotos()
            photoDao.insertPhotos(response)
            Timber.d("Repository", "Photos successfully fetched and stored")
        } catch (e: Exception) {
            Timber.e("Repository", "${e.message}")
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