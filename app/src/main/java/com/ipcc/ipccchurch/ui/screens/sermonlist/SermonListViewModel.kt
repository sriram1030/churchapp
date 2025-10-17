package com.ipcc.ipccchurch.ui.screens.sermonlist

import android.util.Log
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ipcc.ipccchurch.RetrofitClient
import com.ipcc.ipccchurch.models.Playlist
import com.ipcc.ipccchurch.models.Sermon
import kotlinx.coroutines.launch

class SermonListViewModel : ViewModel() {

    private val _sermons = mutableStateOf<List<Sermon>>(emptyList())
    val sermons: State<List<Sermon>> = _sermons

    private val _playlistDetails = mutableStateOf<Playlist?>(null)
    val playlistDetails: State<Playlist?> = _playlistDetails

    private val _isLoading = mutableStateOf(false)
    val isLoading: State<Boolean> = _isLoading

    fun loadSermonsForPlaylist(playlistId: String) {
        viewModelScope.launch {
            _isLoading.value = true
            try {
                // THIS IS THE CORRECTED LINE
                _playlistDetails.value = RetrofitClient.instance.getPlaylistDetails(playlistId.toInt())
                _sermons.value = RetrofitClient.instance.getSermonsByPlaylist(playlistId)
            } catch (e: Exception) {
                Log.e("SermonListViewModel", "API Error in SermonListViewModel: ${e.message}")
            } finally {
                _isLoading.value = false
            }
        }
    }
}