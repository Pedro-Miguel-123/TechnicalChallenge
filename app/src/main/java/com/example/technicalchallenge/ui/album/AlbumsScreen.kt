package com.example.technicalchallenge.ui.album

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarDuration
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.paging.compose.collectAsLazyPagingItems
import com.example.technicalchallenge.R
import com.example.technicalchallenge.ui.components.PhotoList
import com.example.technicalchallenge.ui.components.Warning

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AlbumsScreen(albumsViewModel: AlbumsViewModel = hiltViewModel()) {
    val viewState = albumsViewModel.uiState
    val loading = viewState.loading
    val showSnackBar = viewState.showSnackBar
    val snackbarHostState = remember { SnackbarHostState() }

    LaunchedEffect(albumsViewModel) {
        albumsViewModel.fetchAndSaveAlbums()
    }

    if (showSnackBar) {
        LaunchedEffect(albumsViewModel) {
            snackbarHostState.showSnackbar(
                message = "Failed getting remote data, check internet connection",
                duration = SnackbarDuration.Short
            )
        }
    }

    val photos = albumsViewModel.pagedData.collectAsLazyPagingItems()
    if (loading) {
        Column (
            modifier = Modifier.fillMaxSize(),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            CircularProgressIndicator (
                color = Color.Red
            )
        }
    } else {
        Scaffold(
            snackbarHost = {
                SnackbarHost(hostState = snackbarHostState)
            },
            topBar = {
                TopAppBar(
                    title = {
                        Text (
                            text = stringResource(R.string.top_bar_title)
                        )
                    }
                )
            }
        ) { padding ->
            if(photos.itemCount == 0) {
                Warning()
            } else {
                PhotoList(modifier = Modifier.padding(padding), photos)
            }
        }
    }
}
