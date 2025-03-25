package com.example.technicalchallenge.ui.album

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.cachedIn
import com.example.technicalchallenge.data.AlbumRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

data class AlbumViewState (
    val loading: Boolean = true,
    val showSnackBar: Boolean = false
)

@HiltViewModel
class AlbumsViewModel @Inject constructor(
    private val repository: AlbumRepository
): ViewModel() {

    var uiState by mutableStateOf(AlbumViewState())
        private set

    val pagedData = repository.getPagedPhotos().cachedIn(viewModelScope)

    fun fetchAndSaveAlbums() {
        viewModelScope.launch {
            runCatching {
                repository.fetchAndStoreAlbums()
            }.onSuccess {
                uiState = uiState.copy(
                    loading = false
                )
            }.onFailure {
                uiState = uiState.copy(
                    loading = false,
                    showSnackBar = true
                )
            }.also {
                repository.isFetchInProgress.set(false)
            }
        }
    }
}

