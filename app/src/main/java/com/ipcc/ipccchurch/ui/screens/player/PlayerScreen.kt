package com.ipcc.ipccchurch.ui.screens.player

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.media3.common.Player
import coil.compose.AsyncImage
import java.util.concurrent.TimeUnit

@Composable
fun PlayerScreen(
    playlistId: String?,
    initialSermonId: String?,
    playerViewModel: PlayerViewModel = viewModel()
) {
    val context = LocalContext.current
    val sermon by playerViewModel.currentSermon
    val isPlaying by playerViewModel.isPlaying
    val currentTime by playerViewModel.currentTime
    val totalDuration by playerViewModel.totalDuration
    val repeatMode by playerViewModel.repeatMode
    val sliderPosition = if (totalDuration > 0) currentTime.toFloat() / totalDuration else 0f

    LaunchedEffect(key1 = playlistId, key2 = initialSermonId) {
        if (playlistId != null && initialSermonId != null) {
            playerViewModel.loadPlaylist(playlistId, initialSermonId, context)
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        if (sermon == null) {
            CircularProgressIndicator()
        } else {
            AsyncImage(
                model = sermon!!.imageUrl,
                contentDescription = "Sermon Artwork",
                modifier = Modifier
                    .fillMaxWidth(0.8f)
                    .aspectRatio(1f)
                    .clip(RoundedCornerShape(16.dp)),
                contentScale = ContentScale.Crop
            )
            Spacer(modifier = Modifier.height(32.dp))
            Text(
                text = sermon!!.title,
                fontSize = 22.sp,
                fontWeight = FontWeight.Bold,
                textAlign = TextAlign.Center
            )
            Text(
                text = "Pas.Samuvel",
                fontSize = 16.sp,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                textAlign = TextAlign.Center
            )
            Spacer(modifier = Modifier.height(24.dp))
            Slider(
                value = sliderPosition,
                onValueChange = { playerViewModel.seekTo(it) },
                modifier = Modifier.fillMaxWidth()
            )
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(text = formatDuration(currentTime))
                Text(text = formatDuration(totalDuration))
            }
            Spacer(modifier = Modifier.height(16.dp))
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceEvenly,
                verticalAlignment = Alignment.CenterVertically
            ) {
                IconButton(onClick = { playerViewModel.seekBackward() }) {
                    Icon(Icons.Default.Replay10, "Rewind 10s", modifier = Modifier.size(42.dp))
                }
                IconButton(onClick = { playerViewModel.playPause() }) {
                    Icon(
                        imageVector = if (isPlaying) Icons.Default.PauseCircle else Icons.Default.PlayCircle,
                        "Play/Pause",
                        modifier = Modifier.size(72.dp)
                    )
                }
                IconButton(onClick = { playerViewModel.seekForward() }) {
                    Icon(Icons.Default.Forward30, "Forward 30s", modifier = Modifier.size(42.dp))
                }
            }
            Spacer(modifier = Modifier.height(16.dp))
            IconButton(onClick = { playerViewModel.toggleRepeatMode() }) {
                val icon = when (repeatMode) {
                    Player.REPEAT_MODE_ONE -> Icons.Default.RepeatOne
                    Player.REPEAT_MODE_ALL -> Icons.Default.RepeatOn
                    else -> Icons.Default.Repeat
                }
                Icon(icon, "Repeat Mode")
            }
        }
    }
}

private fun formatDuration(millis: Long): String {
    val minutes = TimeUnit.MILLISECONDS.toMinutes(millis)
    val seconds = TimeUnit.MILLISECONDS.toSeconds(millis) % 60
    return String.format("%02d:%02d", minutes, seconds)
}