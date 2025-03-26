package com.example.technicalchallenge.ui.photo

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.technicalchallenge.data.AlbumRepository
import com.example.technicalchallenge.data.local.Photo
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject


data class PhotoViewState(
    val photo: Photo? = null
)

@HiltViewModel
class PhotoDetailViewModel @Inject constructor(
    private val repository: AlbumRepository
) : ViewModel() {
    var uiState by mutableStateOf(PhotoViewState())
        private set

    fun getPhoto(photoId: Int) {
        viewModelScope.launch {
            val photo = repository.getPhotoByPhotoId(photoId)
            uiState = uiState.copy(
                photo = photo
            )
        }
    }
}
