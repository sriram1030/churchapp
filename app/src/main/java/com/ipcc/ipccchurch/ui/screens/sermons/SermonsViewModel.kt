package com.ipcc.ipccchurch.ui.screens.sermons

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ipcc.ipccchurch.RetrofitClient
import com.ipcc.ipccchurch.models.Playlist
import com.ipcc.ipccchurch.models.Sermon
import kotlinx.coroutines.launch

class SermonsViewModel : ViewModel() {

    private val _playlists = mutableStateOf<List<Playlist>>(emptyList())
    val playlists: State<List<Playlist>> = _playlists

    private val _latestSermons = mutableStateOf<List<Sermon>>(emptyList())
    val latestSermons: State<List<Sermon>> = _latestSermons

    // You can create a new API endpoint for "Other Sermons" later
    // For now, we'll just reuse the latest sermons data
    private val _otherSermons = mutableStateOf<List<Sermon>>(emptyList())
    val otherSermons: State<List<Sermon>> = _otherSermons

    private val _isLoading = mutableStateOf(true)
    val isLoading: State<Boolean> = _isLoading

    init {
        fetchSermonsScreenData()
    }

    fun refresh() {
        fetchSermonsScreenData()
    }

    private fun fetchSermonsScreenData() {
        viewModelScope.launch {
            _isLoading.value = true
            try {
                _playlists.value = RetrofitClient.instance.getPlaylists()
                _latestSermons.value = RetrofitClient.instance.getLatestSermons()
                _otherSermons.value = RetrofitClient.instance.getLatestSermons() // Reusing for now
            } catch (e: Exception) {
                println("API Error in SermonsViewModel: ${e.message}")
            } finally {
                _isLoading.value = false
            }
        }
    }
}