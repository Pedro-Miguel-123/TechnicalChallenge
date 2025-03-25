package com.example.technicalchallenge.data

import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import com.example.technicalchallenge.data.api.LebonCoinAPIService
import com.example.technicalchallenge.data.local.Photo
import com.example.technicalchallenge.data.local.PhotoDao
import com.example.technicalchallenge.util.network.NetworkMonitor
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.util.concurrent.atomic.AtomicBoolean

class AlbumRepositoryImpl(
    private val photoDao: PhotoDao,
    networkMonitor: NetworkMonitor,
    private val lebonCoinApiService: LebonCoinAPIService,
    private val coroutineScope: CoroutineScope
): AlbumRepository {

    override val isFetchInProgress = AtomicBoolean(false)

    init {
        networkMonitor.register()
        networkMonitor.setOnNetworkAvailable {
            coroutineScope.launch {
                if(isFetchInProgress.compareAndSet(false, true)) {
                    fetchAndStoreAlbums().also {
                        isFetchInProgress.set(false)
                    }
                }

            }
        }
    }

    override suspend fun fetchAndStoreAlbums() {
        lebonCoinApiService.fetchPhotos().also {
            photoDao.insertPhotos(it)
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

    override suspend fun getPhotoByPhotoId(photoId: Int): Photo =
        withContext(Dispatchers.IO) {
            photoDao.getPhotoById(photoId)
        }

}
