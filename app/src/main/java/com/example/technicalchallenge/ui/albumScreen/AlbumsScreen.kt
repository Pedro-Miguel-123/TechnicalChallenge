package com.example.technicalchallenge.ui.albumScreen

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.paging.compose.collectAsLazyPagingItems
import com.example.technicalchallenge.R
import com.example.technicalchallenge.ui.components.PhotoList

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AlbumsScreen(albumsViewModel: AlbumsViewModel = hiltViewModel()) {
    val viewState = albumsViewModel.uiState
    val loading = viewState.loading

    LaunchedEffect(albumsViewModel) {
        albumsViewModel.fetchAndSaveAlbums()
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
            PhotoList(modifier = Modifier.padding(padding), photos)
        }
    }
}
