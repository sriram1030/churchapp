package com.ipcc.ipccchurch

import android.content.ComponentName
import android.content.Context
import android.util.Log
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.media3.common.MediaItem
import androidx.media3.common.Player
import androidx.media3.session.MediaController
import androidx.media3.session.SessionToken
import com.google.common.util.concurrent.MoreExecutors
import com.ipcc.ipccchurch.models.Sermon
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class SharedPlayerViewModel : ViewModel() {
    private var mediaController: MediaController? = null
    private var progressJob: Job? = null

    private val _currentSermon = mutableStateOf<Sermon?>(null)
    val currentSermon: State<Sermon?> = _currentSermon

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
        viewModelScope.launch {
            try {
                val sermonList = if (playlistId == "latest") {
                    RetrofitClient.instance.getLatestSermons()
                } else {
                    RetrofitClient.instance.getSermonsByPlaylist(playlistId)
                }

                if (sermonList.isNotEmpty()) {
                    val initialIndex = sermonList.indexOfFirst { it.id == initialSermonId.toInt() }.coerceAtLeast(0)
                    val mediaItems = sermonList.map { createMediaItem(it) }

                    mediaController?.run {
                        setMediaItems(mediaItems, initialIndex, 0L)
                        prepare()
                        play()
                    }
                    _currentSermon.value = sermonList[initialIndex]
                }
            } catch (e: Exception) {
                Log.e("SharedPlayerViewModel", "Error playing sermon list", e)
            }
        }
    }

    private fun addPlayerListener() {
        mediaController?.addListener(object : Player.Listener {
            override fun onIsPlayingChanged(isPlayingValue: Boolean) {
                _isPlaying.value = isPlayingValue
                if (isPlayingValue) startProgressUpdates() else stopProgressUpdates()
            }
            override fun onPlaybackStateChanged(playbackState: Int) {
                if (playbackState == Player.STATE_READY) {
                    _totalDuration.value = mediaController?.duration ?: 0L
                }
            }
            override fun onMediaItemTransition(mediaItem: MediaItem?, reason: Int) {
                super.onMediaItemTransition(mediaItem, reason)
                _currentSermon.value = _currentSermon.value?.copy(
                    title = mediaItem?.mediaMetadata?.title.toString(),
                    imageUrl = mediaItem?.mediaMetadata?.artworkUri.toString()
                )
            }
        })
    }

    private fun createMediaItem(sermon: Sermon): MediaItem {
        val mediaMetadata = androidx.media3.common.MediaMetadata.Builder()
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
    fun skipToNext() = mediaController?.seekToNextMediaItem()
    fun skipToPrevious() = mediaController?.seekToPreviousMediaItem()

    fun stopAndClearPlayer() {
        mediaController?.stop()
        mediaController?.clearMediaItems()
        _currentSermon.value = null // This hides the mini-player
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