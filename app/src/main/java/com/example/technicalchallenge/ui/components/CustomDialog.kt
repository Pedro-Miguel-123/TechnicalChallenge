package com.example.technicalchallenge.ui.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.window.Dialog
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.example.technicalchallenge.R
import com.example.technicalchallenge.data.local.Photo
import com.example.technicalchallenge.ui.theme.dimensions

@Composable
fun CustomDialog(
    photo: Photo,
    onDismissRequest: () -> Unit
) {
    Dialog(onDismissRequest = { onDismissRequest() }) {
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .height(dimensions.cardHeight)
                .padding(dimensions.space2x),
            shape = RoundedCornerShape(dimensions.space2x),
        ) {
            Column(
                modifier = Modifier
                    .fillMaxSize(),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally,
            ) {
                AsyncImage(
                    model = ImageRequest.Builder(LocalContext.current)
                        .data(photo.url)
                        .crossfade(true)
                        .build(),
                    contentDescription = stringResource(R.string.image_description, photo.url),
                    modifier = Modifier
                        .size(dimensions.imageSize)
                        .padding(dimensions.space05x),
                    contentScale = ContentScale.Crop
                )
                Text(
                    text = photo.title,
                    modifier = Modifier.padding(dimensions.space1x),
                )
                Row(
                    modifier = Modifier
                        .fillMaxWidth(),
                    horizontalArrangement = Arrangement.Center,
                ) {
                    TextButton(
                        onClick = { onDismissRequest() }
                    ) {
                        Text(text = stringResource( R.string.dismiss_button))
                    }
                }
            }
        }
    }
}
