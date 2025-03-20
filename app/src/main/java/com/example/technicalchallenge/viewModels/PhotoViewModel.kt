package com.example.technicalchallenge.viewModels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.cachedIn
import com.example.technicalchallenge.data.PhotoRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class PhotoViewModel @Inject constructor(
    private val repository: PhotoRepository
): ViewModel() {

    val pagedData = repository.getPagedPhotos().cachedIn(viewModelScope)

    fun fetchAndSavePhotos() {
        viewModelScope.launch {
            repository.fetchAndStorePhotos()
        }
    }
}
