package com.example.birthday.ui.screens.initialization

import androidx.lifecycle.ViewModel
import com.example.birthday.data.models.BirthdayInfo
import com.example.birthday.network.createHttpClient
import io.ktor.client.plugins.websocket.*
import io.ktor.http.*
import io.ktor.websocket.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.serialization.json.Json

class InitializationViewModel() : ViewModel() {
    var ip: String = "192.168.0.147"
    var port: Int = 8080

    fun connectToTheServer(onBirthdayInfoReceived: (BirthdayInfo) -> Unit) {
        val json = Json {
            encodeDefaults = true
            isLenient = true
            ignoreUnknownKeys = true
        }
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
                                val info = json.decodeFromString<BirthdayInfo>(receivedText)
                                onBirthdayInfoReceived(info)
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