package com.example.technicalchallenge.views

import android.util.Log
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.Card
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.paging.LoadState
import androidx.paging.compose.LazyPagingItems
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.example.technicalchallenge.data.Photo

@Composable
fun MyScrollableList(modifier: Modifier = Modifier, photos: LazyPagingItems<Photo>
) {
    Log.d("VIEW", "REFRESHED WHEN -> ${photos.itemCount}")
    LazyColumn(
        modifier = modifier.fillMaxSize(),
        contentPadding = PaddingValues(16.dp)
    ) {
        items(photos.itemCount) { index ->
            val photo = photos[index]
            if (photo != null) {
                ListPhotoComponent(photo = photo)
            }
        }

        when (photos.loadState.append) {
            is LoadState.Loading -> {
                item { CircularProgressIndicator() }
            }
            is LoadState.Error -> {
                item { Text("Error loading more data") }
            }
            else -> {}
        }
    }
}


@Composable
fun ListPhotoComponent(photo: Photo) {
    var showDialog by remember { mutableStateOf(false) }
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(bottom = 8.dp)
            .clickable {
                showDialog = true
            }
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(text = photo.title)
            AsyncImage(
                model = ImageRequest.Builder
                    (LocalContext.current)
                    .data(photo.thumbnailUrl)
                    .crossfade(true)
                    .build(),
                contentDescription = "Thumbnail from ${photo.title}",
                modifier = Modifier
                    .fillMaxWidth()
                    .size(150.dp),
                contentScale = ContentScale.Crop
            )
        }

        if (showDialog) {
            CustomDialog(
                photo = photo
            ) {
                showDialog = false
            }

        }
    }
}