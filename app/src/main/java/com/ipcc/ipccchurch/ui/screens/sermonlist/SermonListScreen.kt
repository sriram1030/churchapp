package com.ipcc.ipccchurch.ui.screens.sermonlist

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.ipcc.ipccchurch.ui.screens.home.components.SermonCard

@Composable
fun SermonListScreen(
    playlistId: String?,
    onSermonClick: (playlistId: String, sermonId: String) -> Unit,
    sermonListViewModel: SermonListViewModel = viewModel()
) {
    val sermons by sermonListViewModel.sermons
    val isLoading by sermonListViewModel.isLoading

    LaunchedEffect(key1 = playlistId) {
        playlistId?.let {
            sermonListViewModel.loadSermonsForPlaylist(it)
        }
    }

    if (isLoading) {
        Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
            CircularProgressIndicator()
        }
    } else {
        LazyColumn(
            modifier = Modifier.fillMaxSize(),
            contentPadding = PaddingValues(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            items(items = sermons, key = { it.id }) { sermon ->
                SermonCard(
                    sermon = sermon,
                    onClick = {
                        if (playlistId != null) {
                            onSermonClick(playlistId, sermon.id.toString())
                        }
                    }
                )
            }
        }
    }
}