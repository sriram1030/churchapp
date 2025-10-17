package com.ipcc.ipccchurch.ui.screens.sermons

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.ipcc.ipccchurch.ui.screens.home.components.HorizontalSermonList
import com.ipcc.ipccchurch.ui.screens.sermons.components.PlaylistGridCard

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SermonsScreen(
    onSermonClick: (playlistId: String, sermonId: String) -> Unit,
    onPlaylistClick: (playlistId: String) -> Unit,
    sermonsViewModel: SermonsViewModel = viewModel()
) {
    val playlists by sermonsViewModel.playlists
    val latestSermons by sermonsViewModel.latestSermons
    val isLoading by sermonsViewModel.isLoading

    var selectedChipIndex by remember { mutableIntStateOf(0) }
    val filterChips = listOf("All", "Sermon Series", "Guest Speakers")

    if (isLoading) {
        Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
            CircularProgressIndicator()
        }
    } else {
        LazyColumn(
            modifier = Modifier.fillMaxSize(),
            contentPadding = PaddingValues(vertical = 16.dp),
            verticalArrangement = Arrangement.spacedBy(24.dp)
        ) {
            item {
                LazyRow(
                    contentPadding = PaddingValues(horizontal = 16.dp),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    items(filterChips.size) { index ->
                        FilterChip(
                            selected = selectedChipIndex == index,
                            onClick = { selectedChipIndex = index },
                            label = { Text(filterChips[index]) }
                        )
                    }
                }
            }

            item {
                Text(
                    text = "Sermon Series",
                    style = MaterialTheme.typography.titleLarge,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.padding(horizontal = 16.dp)
                )
            }
            item {
                LazyVerticalGrid(
                    columns = GridCells.Fixed(2),
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(190.dp),
                    contentPadding = PaddingValues(horizontal = 16.dp),
                    horizontalArrangement = Arrangement.spacedBy(12.dp),
                    verticalArrangement = Arrangement.spacedBy(12.dp),
                    userScrollEnabled = false
                ) {
                    items(items = playlists, key = { it.id }) { playlist ->
                        PlaylistGridCard(
                            playlist = playlist,
                            onClick = { onPlaylistClick(playlist.id.toString()) }
                        )
                    }
                }
            }

            item {
                HorizontalSermonList(
                    title = "Latest Sermons",
                    sermons = latestSermons,
                    playlistId = "latest",
                    onSermonClick = { playlistId, sermonId ->
                        onSermonClick(playlistId, sermonId.toString())
                    }
                )
            }
        }
    }
}