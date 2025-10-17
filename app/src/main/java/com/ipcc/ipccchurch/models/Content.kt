package com.ipcc.ipccchurch.models

import com.google.gson.annotations.SerializedName

data class Playlist(
    val id: Int, // Changed from String to Int
    val name: String,
    val description: String?,
    @SerializedName("image_url") val imageUrl: String
)

data class Sermon(
    val id: Int, // Changed from String to Int
    val title: String,
    val description: String,
    @SerializedName("image_url") val imageUrl: String,
    @SerializedName("mp3_url") val mp3Url: String
)

data class SliderImage(
    val id: Int, // Changed from String to Int
    val title: String,
    @SerializedName("image_url") val imageUrl: String
)

data class UserRegistration(
    val name: String,
    val email: String,
    val password: String
)

// In models/Content.kt

// Data to send to the login API
data class UserLogin(
    val email: String,
    val password: String
)

// Data received from the login API on success
data class LoginResponse(
    val token: String,
    val user_id: Int,
    val name: String
)