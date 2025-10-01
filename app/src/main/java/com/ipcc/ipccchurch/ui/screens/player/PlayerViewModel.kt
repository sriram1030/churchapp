package com.ipcc.ipccchurch.ui.screens.player

import android.content.Context
import android.util.Log
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.media3.common.MediaItem
import androidx.media3.common.Player
import androidx.media3.exoplayer.ExoPlayer
import com.ipcc.ipccchurch.RetrofitClient
import com.ipcc.ipccchurch.models.Sermon
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class PlayerViewModel : ViewModel() {
    private var exoPlayer: ExoPlayer? = null
    private var progressJob: Job? = null

    private val _currentSermon = mutableStateOf<Sermon?>(null)
    val currentSermon: State<Sermon?> = _currentSermon

    private val _isPlaying = mutableStateOf(false)
    val isPlaying: State<Boolean> = _isPlaying

    private val _totalDuration = mutableStateOf(0L)
    val totalDuration: State<Long> = _totalDuration

    private val _currentTime = mutableStateOf(0L)
    val currentTime: State<Long> = _currentTime

    private val _repeatMode = mutableStateOf(Player.REPEAT_MODE_OFF)
    val repeatMode: State<Int> = _repeatMode

    fun loadPlaylist(playlistId: String, initialSermonId: String, context: Context) {
        viewModelScope.launch {
            try {
                val sermonList = if (playlistId == "latest") {
                    RetrofitClient.instance.getLatestSermons()
                } else {
                    RetrofitClient.instance.getSermonsByPlaylist(playlistId)
                }

                if (sermonList.isNotEmpty()) {
                    val initialIndex = sermonList.indexOfFirst { it.id == initialSermonId }.coerceAtLeast(0)
                    _currentSermon.value = sermonList[initialIndex]
                    initializePlayer(context, sermonList, initialIndex)
                }
            } catch (e: Exception) {
                Log.e("PlayerViewModel", "Error loading playlist: ", e)
            }
        }
    }

    private fun initializePlayer(context: Context, sermonList: List<Sermon>, initialIndex: Int) {
        exoPlayer?.release()
        exoPlayer = ExoPlayer.Builder(context).build().apply {
            val mediaItems = sermonList.map { MediaItem.fromUri(it.mp3Url) }
            setMediaItems(mediaItems)
            seekToDefaultPosition(initialIndex)
            prepare()
            playWhenReady = true

            addListener(object : Player.Listener {
                override fun onIsPlayingChanged(isPlayingValue: Boolean) {
                    _isPlaying.value = isPlayingValue
                    if (isPlayingValue) startProgressUpdates() else stopProgressUpdates()
                }
                override fun onPlaybackStateChanged(playbackState: Int) {
                    if (playbackState == Player.STATE_READY) _totalDuration.value = duration
                }
                override fun onMediaItemTransition(mediaItem: MediaItem?, reason: Int) {
                    super.onMediaItemTransition(mediaItem, reason)
                    _currentSermon.value = sermonList.getOrNull(currentMediaItemIndex)
                }
            })
        }
    }

    fun playPause() = exoPlayer?.playWhenReady?.let { exoPlayer?.playWhenReady = !it }
    fun seekForward() = exoPlayer?.seekForward()
    fun seekBackward() = exoPlayer?.seekBack()

    // THIS IS THE MISSING FUNCTION
    fun seekTo(position: Float) {
        exoPlayer?.let { player ->
            val seekPosition = (player.duration * position).toLong()
            player.seekTo(seekPosition)
        }
    }

    fun toggleRepeatMode() {
        val currentMode = exoPlayer?.repeatMode ?: Player.REPEAT_MODE_OFF
        _repeatMode.value = when (currentMode) {
            Player.REPEAT_MODE_OFF -> Player.REPEAT_MODE_ONE
            Player.REPEAT_MODE_ONE -> Player.REPEAT_MODE_ALL
            else -> Player.REPEAT_MODE_OFF
        }
        exoPlayer?.repeatMode = _repeatMode.value
    }

    private fun startProgressUpdates() {
        stopProgressUpdates()
        progressJob = viewModelScope.launch {
            while (true) {
                _currentTime.value = exoPlayer?.currentPosition ?: 0L
                delay(1000)
            }
        }
    }

    private fun stopProgressUpdates() {
        progressJob?.cancel()
        progressJob = null
    }

    override fun onCleared() {
        exoPlayer?.release()
        super.onCleared()
    }
}