package com.ipcc.ipccchurch.models

import com.google.gson.annotations.SerializedName

// --- Content Models ---
data class Playlist(
    val id: Int,
    val name: String,
    val description: String?,
    @SerializedName("image_url") val imageUrl: String
)

data class Sermon(
    val id: Int,
    val title: String,
    val description: String,
    @SerializedName("image_url") val imageUrl: String,
    @SerializedName("mp3_url") val mp3Url: String
)

data class SliderImage(
    val id: Int,
    val title: String,
    @SerializedName("image_url") val imageUrl: String
)



// --- Authentication & User Models ---
data class UserLogin(
    val email: String,
    val password: String
)

data class UserRegistration(
    val name: String,
    val email: String,
    val password: String
)

data class User(
    val id: Int,
    val name: String,
    val email: String
)

data class AuthResponse(
    val status: String,
    val message: String,
    val token: String,
    val user: User
)

data class UserProfile(
    val id: Int,
    val name: String,
    val email: String,
    @SerializedName("profile_image_url") val profileImageUrl: String?
)

// --- Profile Update Models ---
data class UpdateNameRequest(val name: String)
data class ChangePasswordRequest(val old_password: String, val new_password: String)