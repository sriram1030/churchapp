package com.ipcc.ipccchurch.ui.screens.home

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.ipcc.ipccchurch.ui.screens.home.components.FeaturedContentCard
import com.ipcc.ipccchurch.ui.screens.home.components.HorizontalPlaylistList
import com.ipcc.ipccchurch.ui.screens.home.components.HorizontalSermonList
import com.ipcc.ipccchurch.ui.screens.home.components.RadioPlayerCard

@Composable
fun HomeScreen(
    onSermonClick: (playlistId: String, sermonId: String) -> Unit,
    onPlaylistClick: (playlistId: String) -> Unit,
    homeViewModel: HomeViewModel = viewModel()
) {
    val sliderImages by homeViewModel.sliderImages
    val latestSermons by homeViewModel.latestSermons
    val sundayPlaylists by homeViewModel.sundayPlaylists
    val isLoading by homeViewModel.isLoading

    if (isLoading) {
        Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
            CircularProgressIndicator()
        }
    } else {
        LazyColumn(
            contentPadding = PaddingValues(vertical = 16.dp),
            verticalArrangement = Arrangement.spacedBy(24.dp)
        ) {
            item {
                FeaturedContentCard(sliderItems = sliderImages)
            }
            item {
                // THIS CALL IS NOW CORRECTED
                HorizontalSermonList(
                    title = "Latest Sermons",
                    sermons = latestSermons,
                    playlistId = "latest", // We add the required playlistId
                    onSermonClick = onSermonClick // We pass the handler directly
                )
            }
            item {
                HorizontalPlaylistList(
                    title = "Sunday Sermons",
                    playlists = sundayPlaylists,
                    onPlaylistClick = onPlaylistClick
                )
            }
            item { RadioPlayerCard() }
        }
    }
}