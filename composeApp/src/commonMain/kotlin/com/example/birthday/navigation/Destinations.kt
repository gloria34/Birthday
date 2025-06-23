package com.example.birthday.navigation

import cafe.adriel.voyager.core.screen.Screen
import androidx.compose.runtime.Composable
import com.example.birthday.ui.screens.InitializationScreen
import com.example.birthday.ui.screens.InitializationViewModel

object Initialization : Screen {
    @Composable
    override fun Content() {
        InitializationScreen(
            InitializationViewModel()
        )
    }
}