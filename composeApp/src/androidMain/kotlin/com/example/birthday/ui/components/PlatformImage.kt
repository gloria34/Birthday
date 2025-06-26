package com.example.birthday.ui.components

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import birthday.composeapp.generated.resources.Res
import coil3.ImageLoader
import coil3.compose.AsyncImage
import coil3.svg.SvgDecoder

@Composable
actual fun PlatformImage(
    path: String,
    modifier: Modifier
) {
    AsyncImage(
        imageLoader = ImageLoader.Builder(LocalContext.current)
            .components {
                add(SvgDecoder.Factory())
            }
            .build(),
        model = Res.getUri(path),
        contentDescription = null,
        modifier = modifier
    )

}