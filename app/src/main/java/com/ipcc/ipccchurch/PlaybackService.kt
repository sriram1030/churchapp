package com.ipcc.ipccchurch

import androidx.media3.exoplayer.ExoPlayer
import androidx.media3.session.MediaSession
import androidx.media3.session.MediaSessionService

class PlaybackService : MediaSessionService() {
    private var mediaSession: MediaSession? = null

    // 1. Called when the service is created.
    override fun onCreate() {
        super.onCreate()
        // Create the ExoPlayer instance
        val player = ExoPlayer.Builder(this).build()
        // Create a MediaSession, linking it to the player
        mediaSession = MediaSession.Builder(this, player).build()
    }

    // 2. This is what the UI will connect to.
    override fun onGetSession(controllerInfo: MediaSession.ControllerInfo): MediaSession? {
        return mediaSession
    }

    // 3. Called when the service is destroyed.
    override fun onDestroy() {
        mediaSession?.run {
            player.release()
            release()
            mediaSession = null
        }
        super.onDestroy()
    }
}