package com.example.birthday.ui.components

import androidx.compose.foundation.Image
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import coil3.compose.rememberAsyncImagePainter

@Composable
actual fun PlatformImage(
    path: String,
    modifier: Modifier
) {
    Image(
        painter = rememberAsyncImagePainter(
            model = path,
        ),
        contentDescription = null,
        modifier = modifier
    )
}