package com.ipcc.ipccchurch

import android.content.ComponentName
import android.content.Context
import android.util.Log
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.media3.common.MediaItem
import androidx.media3.common.MediaMetadata
import androidx.media3.common.Player
import androidx.media3.session.MediaController
import androidx.media3.session.SessionToken
import com.google.common.util.concurrent.MoreExecutors
import com.ipcc.ipccchurch.models.Sermon
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

// Data class to hold the radio's metadata
data class RadioMetadata(val title: String = "Live Broadcast", val artist: String = "IPCC Church")

class SharedPlayerViewModel : ViewModel() {
    private var mediaController: MediaController? = null
    private var progressJob: Job? = null
    private var currentSermonList: List<Sermon> = emptyList()

    // Sermon-specific state
    private val _currentSermon = mutableStateOf<Sermon?>(null)
    val currentSermon: State<Sermon?> = _currentSermon

    // Radio-specific state
    private val _isRadioPlaying = mutableStateOf(false)
    val isRadioPlaying: State<Boolean> = _isRadioPlaying
    private val _radioMetadata = mutableStateOf(RadioMetadata())
    val radioMetadata: State<RadioMetadata> = _radioMetadata

    // General player state
    private val _isPlaying = mutableStateOf(false)
    val isPlaying: State<Boolean> = _isPlaying
    private val _currentTime = mutableStateOf(0L)
    val currentTime: State<Long> = _currentTime
    private val _totalDuration = mutableStateOf(0L)
    val totalDuration: State<Long> = _totalDuration
    private val _repeatMode = mutableStateOf(Player.REPEAT_MODE_OFF)
    val repeatMode: State<Int> = _repeatMode

    fun initializeController(context: Context) {
        if (mediaController != null) return
        val sessionToken = SessionToken(context, ComponentName(context, PlaybackService::class.java))
        val controllerFuture = MediaController.Builder(context, sessionToken).buildAsync()
        controllerFuture.addListener({
            mediaController = controllerFuture.get()
            addPlayerListener()
        }, MoreExecutors.directExecutor())
    }

    fun playSermonList(playlistId: String, initialSermonId: String) {
        _isRadioPlaying.value = false // Stop radio tracking
        viewModelScope.launch {
            try {
                currentSermonList = if (playlistId == "latest") {
                    RetrofitClient.instance.getLatestSermons()
                } else {
                    RetrofitClient.instance.getSermonsByPlaylist(playlistId)
                }

                if (currentSermonList.isNotEmpty()) {
                    val initialIndex = currentSermonList.indexOfFirst { it.id == initialSermonId.toInt() }.coerceAtLeast(0)
                    val mediaItems = currentSermonList.map { createMediaItem(it) }

                    mediaController?.run {
                        setMediaItems(mediaItems, initialIndex, 0L)
                        prepare()
                        play()
                    }
                    _currentSermon.value = currentSermonList[initialIndex]
                }
            } catch (e: Exception) {
                Log.e("SharedPlayerViewModel", "Error playing sermon list", e)
            }
        }
    }

    fun playRadio() {
        if (_isRadioPlaying.value && isPlaying.value) {
            stopAndClearPlayer()
            return
        }
        stopAndClearPlayer()
        _isRadioPlaying.value = true
        _radioMetadata.value = RadioMetadata() // Reset to default when starting

        val radioUrl = "https://streams.radio.co/s790fe269d/listen"
        val radioMetadata = MediaMetadata.Builder()
            .setTitle("IPCC Internet Radio")
            .setArtist("Live Broadcast")
            .build()

        val mediaItem = MediaItem.Builder().setUri(radioUrl).setMediaMetadata(radioMetadata).build()
        mediaController?.run {
            setMediaItem(mediaItem)
            prepare()
            play()
        }
    }

    private fun addPlayerListener() {
        mediaController?.addListener(object : Player.Listener {
            override fun onIsPlayingChanged(isPlayingValue: Boolean) {
                _isPlaying.value = isPlayingValue
                if (isPlayingValue) startProgressUpdates() else stopProgressUpdates()
            }
            override fun onPlaybackStateChanged(playbackState: Int) {
                if (playbackState == Player.STATE_READY) _totalDuration.value = mediaController?.duration ?: 0L
            }
            override fun onMediaMetadataChanged(mediaMetadata: MediaMetadata) {
                super.onMediaMetadataChanged(mediaMetadata)
                if (_isRadioPlaying.value) {
                    _radioMetadata.value = RadioMetadata(
                        title = mediaMetadata.title?.toString() ?: "Live Broadcast",
                        artist = mediaMetadata.artist?.toString() ?: "IPCC Church"
                    )
                }
            }
            override fun onMediaItemTransition(mediaItem: MediaItem?, reason: Int) {
                super.onMediaItemTransition(mediaItem, reason)
                if (!_isRadioPlaying.value) {
                    val newIndex = mediaController?.currentMediaItemIndex ?: 0
                    _currentSermon.value = currentSermonList.getOrNull(newIndex)
                }
            }
        })
    }

    private fun createMediaItem(sermon: Sermon): MediaItem {
        val mediaMetadata = MediaMetadata.Builder()
            .setTitle(sermon.title)
            .setArtist("Pas.Samuvel")
            .setArtworkUri(android.net.Uri.parse(sermon.imageUrl))
            .build()
        return MediaItem.Builder()
            .setUri(sermon.mp3Url)
            .setMediaId(sermon.id.toString())
            .setMediaMetadata(mediaMetadata)
            .build()
    }

    fun playPause() = mediaController?.playWhenReady?.let { mediaController?.playWhenReady = !it }

    fun stopAndClearPlayer() {
        mediaController?.stop()
        mediaController?.clearMediaItems()
        _currentSermon.value = null
        _isRadioPlaying.value = false
        _radioMetadata.value = RadioMetadata()
    }

    fun skipToNext() = mediaController?.seekToNextMediaItem()
    fun skipToPrevious() = mediaController?.seekToPreviousMediaItem()

    fun seekTo(position: Float) {
        mediaController?.let { player ->
            val seekPosition = (player.duration * position).toLong()
            player.seekTo(seekPosition)
        }
    }

    fun toggleRepeatMode() {
        val currentMode = mediaController?.repeatMode ?: Player.REPEAT_MODE_OFF
        val newMode = if (currentMode == Player.REPEAT_MODE_ONE) Player.REPEAT_MODE_OFF else Player.REPEAT_MODE_ONE
        mediaController?.repeatMode = newMode
        _repeatMode.value = newMode
    }

    private fun startProgressUpdates() {
        stopProgressUpdates()
        progressJob = viewModelScope.launch {
            while (true) {
                _currentTime.value = mediaController?.currentPosition ?: 0L
                delay(1000)
            }
        }
    }

    private fun stopProgressUpdates() {
        progressJob?.cancel()
        progressJob = null
    }

    override fun onCleared() {
        mediaController?.release()
        super.onCleared()
    }
}