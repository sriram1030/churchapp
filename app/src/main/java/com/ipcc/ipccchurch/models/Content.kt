package com.ipcc.ipccchurch.models

import com.google.gson.annotations.SerializedName

data class Playlist(
    val id: String,
    val name: String,
    @SerializedName("image_url") val imageUrl: String
)

data class Sermon(
    val id: String,
    val title: String,
    val description: String,
    @SerializedName("image_url") val imageUrl: String,
    @SerializedName("mp3_url") val mp3Url: String
)

data class SliderImage(
    val id: String,
    val title: String,
    @SerializedName("image_url") val imageUrl: String
)