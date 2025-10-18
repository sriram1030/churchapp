package com.ipcc.ipccchurch.ui

import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Pause
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.ripple.rememberRipple
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import com.ipcc.ipccchurch.SharedPlayerViewModel

@Composable
fun MiniPlayer(
    sharedPlayerViewModel: SharedPlayerViewModel,
    onNavigateToPlayer: () -> Unit
) {
    val currentSermon by sharedPlayerViewModel.currentSermon
    val isPlaying by sharedPlayerViewModel.isPlaying
    val currentTime by sharedPlayerViewModel.currentTime
    val totalDuration by sharedPlayerViewModel.totalDuration
    val progress = if (totalDuration > 0) currentTime.toFloat() / totalDuration else 0f

    // The AnimatedVisibility wrapper is now removed
    Surface(
        modifier = Modifier.fillMaxWidth(),
        shadowElevation = 8.dp
    ) {
        Column {
            LinearProgressIndicator(
                progress = { progress },
                modifier = Modifier.fillMaxWidth().height(2.dp)
            )
            Row(
                modifier = Modifier
                    .padding(horizontal = 8.dp, vertical = 8.dp)
                    .clickable(
                        interactionSource = remember { MutableInteractionSource() },
                        indication = rememberRipple(),
                        onClick = onNavigateToPlayer
                    ),
                verticalAlignment = Alignment.CenterVertically
            ) {
                AsyncImage(
                    model = currentSermon?.imageUrl,
                    contentDescription = "Sermon Artwork",
                    modifier = Modifier.size(48.dp),
                    contentScale = ContentScale.Crop
                )
                Spacer(modifier = Modifier.width(12.dp))
                Column(modifier = Modifier.weight(1f)) {
                    Text(currentSermon?.title ?: "No Title", maxLines = 1, overflow = TextOverflow.Ellipsis)
                    Text("Pas.Samuvel", style = MaterialTheme.typography.bodySmall)
                }
                IconButton(onClick = { sharedPlayerViewModel.playPause() }) {
                    Icon(if (isPlaying) Icons.Default.Pause else Icons.Default.PlayArrow, "Play/Pause", modifier = Modifier.size(36.dp))
                }
                IconButton(onClick = { sharedPlayerViewModel.stopAndClearPlayer() }) {
                    Icon(Icons.Default.Close, "Close Player", modifier = Modifier.size(36.dp))
                }
            }
        }
    }
}