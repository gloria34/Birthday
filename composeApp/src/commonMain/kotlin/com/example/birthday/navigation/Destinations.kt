package com.example.birthday.navigation

import BirthdayScreen
import cafe.adriel.voyager.core.screen.Screen
import androidx.compose.runtime.Composable
import com.example.birthday.data.models.BirthdayInfo
import com.example.birthday.ui.screens.birthday.BirthdayViewModel
import com.example.birthday.ui.screens.initialization.InitializationScreen
import com.example.birthday.ui.screens.initialization.InitializationViewModel

object Initialization : Screen {
    @Composable
    override fun Content() {
        InitializationScreen(
            InitializationViewModel()
        )
    }
}

class Birthday(
    private val name: String?,
    private val dob: Long,
    private val theme: String
) : Screen {
    @Composable
    override fun Content() {
        val birthdayInfo = BirthdayInfo(name, dob, theme)
        BirthdayScreen(BirthdayViewModel(birthdayInfo))
    }
}