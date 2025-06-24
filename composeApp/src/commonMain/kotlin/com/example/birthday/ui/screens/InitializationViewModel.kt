package com.example.birthday.ui.screens

import androidx.lifecycle.ViewModel
import com.example.birthday.network.createHttpClient
import io.ktor.client.plugins.websocket.webSocket
import io.ktor.http.HttpMethod
import io.ktor.websocket.Frame
import io.ktor.websocket.readText
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.launch

class InitializationViewModel() : ViewModel() {
    private val _messages = MutableSharedFlow<String>()
    val messages: SharedFlow<String> = _messages
    var ip: String = "192.168.0.147"
    var port: Int = 8080

    fun connectToTheServer() {
        CoroutineScope(Dispatchers.Main).launch {
            val httpClient = createHttpClient()
            try {
                httpClient.webSocket(
                    method = HttpMethod.Get,
                    host = ip,
                    port = port,
                    path = "/nanit"
                ) {
                    send(Frame.Text("HappyBirthday"))
                    for (frame in incoming) {
                        when (frame) {
                            is Frame.Text -> {
                                val receivedText = frame.readText()
                                _messages.emit(receivedText)
                            }

                            else -> Unit
                        }
                    }
                }
            } catch (e: Exception) {
                println("Error connecting to the server: ${e.message}")
            }
        }
    }
}