package com.example.technicalchallenge.ui.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.Card
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.navigation.NavController
import androidx.paging.compose.LazyPagingItems
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.example.technicalchallenge.R
import com.example.technicalchallenge.data.local.Photo
import com.example.technicalchallenge.ui.theme.dimensions

@Composable
fun PhotoList(
    modifier: Modifier, photos: LazyPagingItems<Photo>, navController: NavController
) {
    LazyColumn(
        modifier = modifier.fillMaxSize(), contentPadding = PaddingValues(dimensions.space2x)
    ) {
        items(photos.itemCount) { index ->
            photos[index]?.let { photo ->
                PhotoItem(photo = photo) {
                    navController.navigate("photo_detail/${photo.id}")
                }
            }
        }
    }
}


@Composable
fun PhotoItem(photo: Photo, onClick: () -> Unit) {
    Card(modifier = Modifier
        .fillMaxWidth()
        .padding(bottom = dimensions.space1x)
        .clickable {
            onClick()
        }) {
        Row(
            modifier = Modifier.padding(dimensions.space2x),
            verticalAlignment = Alignment.CenterVertically
        ) {
            AsyncImage(
                model = ImageRequest.Builder(LocalContext.current).data(photo.thumbnailUrl)
                    .placeholder(R.drawable.baseline_broken_image_24)
                    .error(R.drawable.baseline_broken_image_24).crossfade(true).build(),
                contentDescription = stringResource(R.string.image_description, photo.thumbnailUrl),
                contentScale = ContentScale.Crop,
                modifier = Modifier.padding(dimensions.space1x)
            )
            Text(text = photo.title)
        }
    }
}
