package com.example.birthday

interface Platform {
    val name: String
}

expect fun getPlatform(): Platform