package com.example.technicalchallenge.ui.photo

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.example.technicalchallenge.R
import com.example.technicalchallenge.ui.theme.dimensions

@Composable
fun PhotoDetailScreen(photoId: Int, viewModel: PhotoViewModel = hiltViewModel(), navController: NavController) {
    LaunchedEffect(viewModel) {
        viewModel.getPhoto(photoId)
    }
    val viewState = viewModel.uiState
    val photo = viewState.photo
    Column(
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = dimensions.space2x)
    ) {
        AsyncImage(
            model = ImageRequest.Builder
                (LocalContext.current)
                .data(photo.url)
                .placeholder(R.drawable.baseline_broken_image_24)
                .error(R.drawable.baseline_broken_image_24)
                .crossfade(true)
                .build(),
            contentDescription = stringResource(R.string.image_description, photo.thumbnailUrl),
            contentScale = ContentScale.Crop,
            modifier = Modifier.padding(dimensions.space1x)
        )

        Text(
            text = photo.title
        )

        Text(
            text = stringResource(R.string.belongs_to_album_with_id, photo.albumId)
        )

        Button(
            onClick = {navController.popBackStack()}
        ) {
            Text(
                text = stringResource(R.string.go_back)
            )
        }
    }

}
