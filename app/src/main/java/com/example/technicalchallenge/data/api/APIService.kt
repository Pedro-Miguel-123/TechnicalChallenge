package com.example.technicalchallenge.data.api

import com.example.technicalchallenge.data.db.Photo
import retrofit2.http.GET

interface APIService {
    @GET("technical-test.json")
    suspend fun fetchPhotos(): List<Photo>
}
