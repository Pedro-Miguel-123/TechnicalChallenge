package com.example.technicalchallenge.data

import com.example.technicalchallenge.data.api.RetrofitInstance
import com.example.technicalchallenge.data.db.PhotoDao

class PhotoRepository(private val photoDao: PhotoDao) {
    suspend fun fetchAndStorePhotos() {
        val response = RetrofitInstance.api.fetchPhotos()
        photoDao.insertPhotos(response)
    }

    suspend fun getPhotosFromDb(): List<Photo> {
        return photoDao.getAllPhotos()
    }
}