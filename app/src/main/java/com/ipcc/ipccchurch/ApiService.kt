package com.ipcc.ipccchurch

import com.ipcc.ipccchurch.models.*
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.*

interface ApiService {
    // Content Endpoints

    @GET("api/events.php")
    suspend fun getEvents(): List<Event>
    @GET("api/playlists.php")
    suspend fun getPlaylists(): List<Playlist>

    @GET("api/sermons.php")
    suspend fun getSermonsByPlaylist(@Query("playlist_id") playlistId: String): List<Sermon>

    @GET("api/sermons.php")
    suspend fun getSermonById(@Query("id") sermonId: String): Sermon

    @GET("api/slider.php")
    suspend fun getSliderImages(): List<SliderImage>

    @GET("api/latest_sermons.php")
    suspend fun getLatestSermons(): List<Sermon>

    @GET("api/playlist_detail.php")
    suspend fun getPlaylistDetails(@Query("id") playlistId: Int): Playlist

    // Authentication & User Endpoints
    @POST("api/register.php")
    suspend fun registerUser(@Body user: UserRegistration): AuthResponse

    @POST("api/login.php")
    suspend fun loginUser(@Body user: UserLogin): AuthResponse

    @GET("api/get_profile.php")
    suspend fun getUserProfile(@Header("Authorization") token: String): UserProfile

    // Simplified Update Endpoints
    @POST("api/update_name.php")
    suspend fun updateName(
        @Header("Authorization") token: String,
        @Body request: UpdateNameRequest
    ): UserProfile

    @POST("api/change_password.php")
    suspend fun changePassword(
        @Header("Authorization") token: String,
        @Body request: ChangePasswordRequest
    ): Response<Unit>
}

object RetrofitClient {
    private const val BASE_URL = "https://church.sriramsri.in/church_admin/"

    val instance: ApiService by lazy {
        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(ApiService::class.java)
    }
}