package com.example.birthday.ui.components

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier

@Composable
expect fun PlatformImage(
    path: String,
    modifier: Modifier = Modifier
)