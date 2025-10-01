package com.ipcc.ipccchurch

import com.ipcc.ipccchurch.models.Playlist
import com.ipcc.ipccchurch.models.Sermon
import com.ipcc.ipccchurch.models.SliderImage
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
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