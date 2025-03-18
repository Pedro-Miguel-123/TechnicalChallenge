package com.example.technicalchallenge.data

import android.content.Context
import android.util.Log
import androidx.paging.Pager
import androidx.paging.PagingConfig
import com.example.technicalchallenge.data.api.RetrofitInstance
import com.example.technicalchallenge.data.db.PhotoDao
import com.example.technicalchallenge.data.net.NetworkUtil

class PhotoRepository(private val photoDao: PhotoDao, private val context: Context) {
    suspend fun fetchAndStorePhotos() {
        if (NetworkUtil.isOnline(context)) {
            val response = RetrofitInstance.api.fetchPhotos()
            photoDao.insertPhotos(response)
        } else {
            Log.e("Repository", "Network not connected, unable to fetch new list")
        }
    }

    fun getPagedPhotos(): Pager<Int, Photo> {
        Log.d("Repository", "Getting photos in pager")
        val pager = Pager(
            config = PagingConfig(
                pageSize = 20,
                enablePlaceholders = false
            ),
            pagingSourceFactory = { photoDao.getPagedPhotos() }
        )
        return pager
    }
}