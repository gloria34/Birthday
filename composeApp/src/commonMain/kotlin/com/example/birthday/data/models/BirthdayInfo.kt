package com.example.birthday.data.models

import kotlinx.serialization.Serializable

@Serializable
data class BirthdayInfo(
    val name: String?,
    val dob: Long,
    val theme: String
)