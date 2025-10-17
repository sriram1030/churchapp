package com.ipcc.ipccchurch.ui.screens.home

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ipcc.ipccchurch.RetrofitClient
import com.ipcc.ipccchurch.models.Playlist
import com.ipcc.ipccchurch.models.Sermon
import com.ipcc.ipccchurch.models.SliderImage
import kotlinx.coroutines.launch

class HomeViewModel : ViewModel() {

    private val _sliderImages = mutableStateOf<List<SliderImage>>(emptyList())
    val sliderImages: State<List<SliderImage>> = _sliderImages

    private val _latestSermons = mutableStateOf<List<Sermon>>(emptyList())
    val latestSermons: State<List<Sermon>> = _latestSermons

    private val _sundayPlaylists = mutableStateOf<List<Playlist>>(emptyList())
    val sundayPlaylists: State<List<Playlist>> = _sundayPlaylists

    private val _isLoading = mutableStateOf(true)
    val isLoading: State<Boolean> = _isLoading

    init {
        fetchHomeScreenData()
    }

    // This function is called by the pull-to-refresh action
    fun refresh() {
        fetchHomeScreenData()
    }

    private fun fetchHomeScreenData() {
        viewModelScope.launch {
            // Set loading to true only if it's a refresh action, not the initial load
            if (!_isLoading.value) {
                _isLoading.value = true
            }
            try {
                _latestSermons.value = RetrofitClient.instance.getLatestSermons()
                _sundayPlaylists.value = RetrofitClient.instance.getPlaylists()
                _sliderImages.value = RetrofitClient.instance.getSliderImages()
            } catch (e: Exception) {
                println("API Error in HomeViewModel: ${e.message}")
            } finally {
                _isLoading.value = false
            }
        }
    }
}