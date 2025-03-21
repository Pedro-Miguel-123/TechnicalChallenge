package com.example.technicalchallenge.ui.components

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
import androidx.compose.runtime.currentCompositionLocalContext
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.paging.LoadState
import androidx.paging.compose.LazyPagingItems
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.example.technicalchallenge.R
import com.example.technicalchallenge.data.local.Photo
import com.example.technicalchallenge.ui.theme.dimensions

@Composable
fun PhotoList(
    modifier: Modifier = Modifier,
    photos: LazyPagingItems<Photo>
) {
    LazyColumn(
        modifier = modifier.fillMaxSize(),
        contentPadding = PaddingValues(dimensions.space2x)
    ) {
        items(photos.itemCount) { index ->
            val photo = photos[index]
            if (photo != null) {
                PhotoItem(photo = photo)
            }
        }
    }

}


@Composable
fun PhotoItem(photo: Photo) {
    var showDialog by remember { mutableStateOf(false) }
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(bottom = dimensions.space1x)
            .clickable {
                showDialog = true
            }
    ) {
        Column(modifier = Modifier.padding(dimensions.space2x)) {
            Text(text = photo.title)
            AsyncImage(
                model = ImageRequest.Builder
                    (LocalContext.current)
                    .data(photo.thumbnailUrl)
                    .crossfade(true)
                    .build(),
                contentDescription = stringResource(R.string.image_description, photo.thumbnailUrl),
                modifier = Modifier
                    .fillMaxWidth()
                    .size(dimensions.imageSize),
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
