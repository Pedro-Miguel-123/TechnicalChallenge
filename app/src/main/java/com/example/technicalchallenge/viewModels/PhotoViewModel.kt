package com.example.technicalchallenge.viewModels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.technicalchallenge.data.Photo
import com.example.technicalchallenge.data.PhotoRepository
import kotlinx.coroutines.launch

class PhotoViewModel(private val repository: PhotoRepository): ViewModel() {

    fun fetchAndSavePhotos() {
        viewModelScope.launch {
            repository.fetchAndStorePhotos()
        }
    }

    fun getPhotosFromDb(onResult: (List<Photo>) -> Unit) {
        viewModelScope.launch {
            val photos = repository.getPhotosFromDb()
            onResult(photos)
        }
    }
}