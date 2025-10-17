package com.ipcc.ipccchurch.ui.screens.home.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.ipcc.ipccchurch.models.Playlist
import com.ipcc.ipccchurch.models.Sermon

@Composable
fun HorizontalSermonList(
    title: String,
    sermons: List<Sermon>,
    playlistId: String,
    onSermonClick: (playlistId: String, sermonId: String) -> Unit,
    modifier: Modifier = Modifier
) {
    Column(modifier = modifier) {
        Text(
            text = title,
            style = MaterialTheme.typography.titleLarge,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp)
        )
        LazyRow(
            contentPadding = PaddingValues(horizontal = 16.dp),
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            items(items = sermons, key = { sermon -> sermon.id }) { sermon ->
                SermonCard(
                    sermon = sermon,
                    // THE FIX IS HERE:
                    onClick = { onSermonClick(playlistId, sermon.id.toString()) }
                )
            }
        }
    }
}

@Composable
fun HorizontalPlaylistList(
    title: String,
    playlists: List<Playlist>,
    onPlaylistClick: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    Column(modifier = modifier) {
        Text(
            text = title,
            style = MaterialTheme.typography.titleLarge,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp)
        )
        LazyRow(
            contentPadding = PaddingValues(horizontal = 16.dp),
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            items(items = playlists, key = { playlist -> playlist.id }) { playlist ->
                SermonCard(
                    sermon = Sermon(
                        id = playlist.id,
                        title = playlist.name,
                        description = "Sermon Series",
                        imageUrl = playlist.imageUrl,
                        mp3Url = ""
                    ),
                    // AND THE FIX IS ALSO HERE:
                    onClick = { onPlaylistClick(playlist.id.toString()) }
                )
            }
        }
    }
}