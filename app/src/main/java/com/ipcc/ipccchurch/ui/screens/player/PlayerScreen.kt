package com.ipcc.ipccchurch.ui.screens.player

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.media3.common.Player
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import coil.compose.AsyncImage
import com.ipcc.ipccchurch.SharedPlayerViewModel
import java.util.concurrent.TimeUnit

@Composable
fun PlayerScreen(
    playlistId: String?,
    initialSermonId: String?,
    navController: NavController,
    sharedPlayerViewModel: SharedPlayerViewModel
) {
    val sermon by sharedPlayerViewModel.currentSermon
    val isPlaying by sharedPlayerViewModel.isPlaying
    val currentTime by sharedPlayerViewModel.currentTime
    val totalDuration by sharedPlayerViewModel.totalDuration
    val repeatMode by sharedPlayerViewModel.repeatMode
    val sliderPosition = if (totalDuration > 0) currentTime.toFloat() / totalDuration else 0f

    LaunchedEffect(key1 = playlistId, key2 = initialSermonId) {
        if (playlistId != null && initialSermonId != null) {
            sharedPlayerViewModel.playSermonList(playlistId, initialSermonId)
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 24.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        if (sermon == null) {
            CircularProgressIndicator()
        } else {
            Spacer(modifier = Modifier.weight(1f))

            AsyncImage(
                model = sermon!!.imageUrl,
                contentDescription = "Sermon Artwork",
                modifier = Modifier
                    .fillMaxWidth()
                    .aspectRatio(1f)
                    .clip(RoundedCornerShape(12.dp)),
                contentScale = ContentScale.Crop
            )
            Spacer(modifier = Modifier.height(32.dp))

            Text(
                text = sermon!!.title,
                style = MaterialTheme.typography.headlineSmall,
                fontWeight = FontWeight.Bold,
                textAlign = TextAlign.Center
            )
            Text(
                text = "Pas.Samuvel",
                style = MaterialTheme.typography.bodyLarge,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
            Spacer(modifier = Modifier.height(24.dp))
            Slider(
                value = sliderPosition,
                onValueChange = { /* TODO: Implement seek while dragging */ }
            )
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(text = formatDuration(currentTime), style = MaterialTheme.typography.bodySmall)
                Text(text = formatDuration(totalDuration), style = MaterialTheme.typography.bodySmall)
            }
            Spacer(modifier = Modifier.height(16.dp))
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceEvenly,
                verticalAlignment = Alignment.CenterVertically
            ) {
                IconButton(onClick = { /* TODO: Shuffle */ }) {
                    Icon(Icons.Default.Shuffle, "Shuffle", modifier = Modifier.size(28.dp))
                }
                IconButton(onClick = { sharedPlayerViewModel.skipToPrevious() }) {
                    Icon(Icons.Default.SkipPrevious, "Previous", modifier = Modifier.size(42.dp))
                }
                IconButton(onClick = { sharedPlayerViewModel.playPause() }) {
                    Icon(
                        imageVector = if (isPlaying) Icons.Default.PauseCircle else Icons.Default.PlayCircle,
                        "Play/Pause",
                        modifier = Modifier.size(72.dp)
                    )
                }
                IconButton(onClick = { sharedPlayerViewModel.skipToNext() }) {
                    Icon(Icons.Default.SkipNext, "Next", modifier = Modifier.size(42.dp))
                }
                IconButton(onClick = { /* TODO: Toggle Repeat */ }) {
                    val icon = if (repeatMode == Player.REPEAT_MODE_ONE) Icons.Default.RepeatOne else Icons.Default.Repeat
                    Icon(icon, "Repeat", modifier = Modifier.size(28.dp))
                }
            }
            Spacer(modifier = Modifier.weight(1f))
        }
    }
}

private fun formatDuration(millis: Long): String {
    val minutes = TimeUnit.MILLISECONDS.toMinutes(millis)
    val seconds = TimeUnit.MILLISECONDS.toSeconds(millis) % 60
    return String.format("%02d:%02d", minutes, seconds)
}