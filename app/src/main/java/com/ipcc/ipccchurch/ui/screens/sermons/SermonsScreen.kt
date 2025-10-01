package com.ipcc.ipccchurch.ui.screens.sermons

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.ipcc.ipccchurch.models.Playlist
import com.ipcc.ipccchurch.models.Sermon
import com.ipcc.ipccchurch.ui.screens.home.components.HorizontalPlaylistList
import com.ipcc.ipccchurch.ui.screens.home.components.HorizontalSermonList

@Composable
fun SermonsScreen(
    onSermonClick: (playlistId: String, sermonId: String) -> Unit,
    onPlaylistClick: (playlistId: String) -> Unit
) {
    // Corrected fake data with all required properties
    val latestSermons = List(5) {
        Sermon(
            id = "s${it + 1}",
            title = "Latest Sermon ${it + 1}",
            description = "Description for latest sermon",
            imageUrl = "",
            mp3Url = ""
        )
    }
    val sundayPlaylists = List(5) {
        Playlist(
            id = "p${it + 1}",
            name = "Sunday Playlist ${it + 1}",
            imageUrl = ""
        )
    }
    val otherSermons = List(5) {
        Sermon(
            id = "o${it + 1}",
            title = "Guest Sermon ${it + 1}",
            description = "Guest Speaker",
            imageUrl = "",
            mp3Url = ""
        )
    }

    LazyColumn(
        contentPadding = PaddingValues(vertical = 16.dp),
        verticalArrangement = Arrangement.spacedBy(24.dp)
    ) {
        item {
            HorizontalSermonList(
                title = "Latest Sermons",
                sermons = latestSermons,
                playlistId = "latest",
                onSermonClick = onSermonClick
            )
        }
        item {
            HorizontalPlaylistList(
                title = "Sunday Sermons",
                playlists = sundayPlaylists,
                onPlaylistClick = onPlaylistClick
            )
        }
        item {
            HorizontalSermonList(
                title = "Other Sermons",
                sermons = otherSermons,
                playlistId = "other",
                onSermonClick = onSermonClick
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
fun SermonsScreenPreview() {
    SermonsScreen(onSermonClick = { _, _ -> }, onPlaylistClick = {})
}