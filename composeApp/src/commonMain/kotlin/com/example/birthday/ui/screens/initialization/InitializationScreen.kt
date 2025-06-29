package com.example.birthday.ui.screens.initialization

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import cafe.adriel.voyager.navigator.LocalNavigator
import com.example.birthday.navigation.Birthday

@Composable
fun InitializationScreen(
    viewModel: InitializationViewModel
) {
    val navigator = LocalNavigator.current
    Column(
        modifier = Modifier
            .fillMaxSize()
            .wrapContentSize(Alignment.Center),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = "Connect to the server",
            style = MaterialTheme.typography.titleLarge
        )
        TextField(
            value = viewModel.ip,
            onValueChange = { viewModel.ip = it },
            label = { Text("IP Address") }
        )
        TextField(
            value = viewModel.port.toString(),
            onValueChange = { viewModel.port = it.toIntOrNull() ?: 8080 },
            label = { Text("Port") }
        )
        Button(
            onClick = {
                viewModel.connectToTheServer {
                    navigator?.push(
                        Birthday(
                            it.name,
                            it.dob,
                            it.theme
                        )
                    )
                }
            }
        ) {
            Text("Connect")
        }
    }
}