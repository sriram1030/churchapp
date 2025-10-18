package com.ipcc.ipccchurch.ui.screens.radio

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.PauseCircle
import androidx.compose.material.icons.filled.PlayCircle
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.ipcc.ipccchurch.R
import com.ipcc.ipccchurch.SharedPlayerViewModel

@Composable
fun RadioScreen(
    sharedPlayerViewModel: SharedPlayerViewModel
) {
    val isRadioPlaying by sharedPlayerViewModel.isRadioPlaying
    val isOverallPlaying by sharedPlayerViewModel.isPlaying

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Image(
            painter = painterResource(id = R.drawable.church_logo),
            contentDescription = "Church Logo",
            modifier = Modifier
                .fillMaxWidth(0.6f)
                .aspectRatio(1f)
        )
        Spacer(modifier = Modifier.height(24.dp))

        Text("IPCC Internet Radio", style = MaterialTheme.typography.headlineSmall, fontWeight = FontWeight.Bold)

        // "LIVE" indicator
        AnimatedVisibility(visible = isRadioPlaying && isOverallPlaying) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.padding(top = 8.dp)
            ) {
                Box(modifier = Modifier.size(10.dp).clip(CircleShape).background(Color.Red))
                Spacer(modifier = Modifier.width(8.dp))
                Text("LIVE", color = Color.Red, style = MaterialTheme.typography.labelMedium, fontWeight = FontWeight.Bold)
            }
        }

        Spacer(modifier = Modifier.height(32.dp))

        // Large Play/Pause Button
        IconButton(
            onClick = { sharedPlayerViewModel.playRadio() },
            modifier = Modifier.size(80.dp)
        ) {
            Icon(
                imageVector = if (isRadioPlaying && isOverallPlaying) Icons.Default.PauseCircle else Icons.Default.PlayCircle,
                contentDescription = "Play/Pause Radio",
                modifier = Modifier.fillMaxSize(),
                tint = MaterialTheme.colorScheme.primary
            )
        }
    }
}