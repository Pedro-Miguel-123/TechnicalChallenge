package com.example.technicalchallenge.views

import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.paging.compose.collectAsLazyPagingItems
import com.example.technicalchallenge.viewModels.PhotoViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainScreen(photoViewModel: PhotoViewModel = hiltViewModel()) {
    LaunchedEffect(Unit) {
        photoViewModel.fetchAndSavePhotos()
    }

    val photos = photoViewModel.pagedData.collectAsLazyPagingItems()

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("LIST OF PHOTO ALBUMS") }
            )
        }
    ) { padding ->
        MyScrollableList(modifier = Modifier.padding(padding), photos)
    }
}
