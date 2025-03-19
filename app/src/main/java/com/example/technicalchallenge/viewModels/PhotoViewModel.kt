package com.example.technicalchallenge.viewModels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.cachedIn
import com.example.technicalchallenge.data.PhotoRepository
import kotlinx.coroutines.launch

class PhotoViewModel(private val repository: PhotoRepository): ViewModel() {

    val pagedData = repository.getPagedPhotos().cachedIn(viewModelScope)

    fun fetchAndSavePhotos() {
        viewModelScope.launch {
            repository.fetchAndStorePhotos()
        }
    }

}