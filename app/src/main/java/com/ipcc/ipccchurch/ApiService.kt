package com.ipcc.ipccchurch

import com.ipcc.ipccchurch.models.LoginResponse
import com.ipcc.ipccchurch.models.Playlist
import com.ipcc.ipccchurch.models.Sermon
import com.ipcc.ipccchurch.models.SliderImage
import com.ipcc.ipccchurch.models.UserLogin
import com.ipcc.ipccchurch.models.UserRegistration
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Query

interface ApiService {
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

    // In ApiService.kt
    @GET("api/playlist_detail.php")
    suspend fun getPlaylistDetails(@Query("id") playlistId: String): Playlist

    @POST("api/register.php")
    suspend fun registerUser(@Body user: UserRegistration): Response<Unit>

    @POST("api/login.php")
    suspend fun loginUser(@Body user: UserLogin): LoginResponse
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