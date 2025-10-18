package com.ipcc.ipccchurch.ui.screens.home

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.pulltorefresh.PullToRefreshContainer
import androidx.compose.material3.pulltorefresh.rememberPullToRefreshState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.ipcc.ipccchurch.SharedPlayerViewModel
import com.ipcc.ipccchurch.ui.screens.home.components.FeaturedContentCard
import com.ipcc.ipccchurch.ui.screens.home.components.HomeScreenSkeleton
import com.ipcc.ipccchurch.ui.screens.home.components.HorizontalPlaylistList
import com.ipcc.ipccchurch.ui.screens.home.components.HorizontalSermonList
import com.ipcc.ipccchurch.ui.screens.home.components.RadioPlayerCard

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(
    onSermonClick: (playlistId: String, sermonId: String) -> Unit,
    onPlaylistClick: (playlistId: String) -> Unit,
    homeViewModel: HomeViewModel = viewModel(),
    sharedPlayerViewModel: SharedPlayerViewModel
) {
    // We only get the single isLoading flag now
    val sliderImages by homeViewModel.sliderImages
    val latestSermons by homeViewModel.latestSermons
    val sundayPlaylists by homeViewModel.sundayPlaylists
    val isLoading by homeViewModel.isLoading
    val isRadioPlaying by sharedPlayerViewModel.isRadioPlaying

    val pullToRefreshState = rememberPullToRefreshState()

    Box(
        modifier = Modifier
            .fillMaxSize()
            .nestedScroll(pullToRefreshState.nestedScrollConnection)
    ) {
        // Show the skeleton only on the initial load
        if (isLoading && sliderImages.isEmpty()) {
            HomeScreenSkeleton()
        } else {
            LazyColumn(
                modifier = Modifier.fillMaxSize(),
                contentPadding = PaddingValues(vertical = 16.dp),
                verticalArrangement = Arrangement.spacedBy(24.dp)
            ) {
                if (sliderImages.isNotEmpty()) {
                    item {
                        Box(modifier = Modifier.padding(horizontal = 16.dp)) {
                            FeaturedContentCard(sliderItems = sliderImages)
                        }
                    }
                }
                if (latestSermons.isNotEmpty()) {
                    item {
                        HorizontalSermonList(
                            title = "Latest Sermons",
                            sermons = latestSermons,
                            playlistId = "latest",
                            onSermonClick = onSermonClick
                        )
                    }
                }
                if (sundayPlaylists.isNotEmpty()) {
                    item {
                        HorizontalPlaylistList(
                            title = "Sermon Series",
                            playlists = sundayPlaylists,
                            onPlaylistClick = onPlaylistClick
                        )
                    }
                }

            }
        }

        // This handles the pull-to-refresh action
        if (pullToRefreshState.isRefreshing) {
            LaunchedEffect(true) {
                homeViewModel.refresh()
            }
        }

        // This hides the refresh indicator when loading is finished
        LaunchedEffect(isLoading) {
            if (!isLoading) {
                pullToRefreshState.endRefresh()
            }
        }

        PullToRefreshContainer(
            state = pullToRefreshState,
            modifier = Modifier.align(Alignment.TopCenter)
        )
    }
}