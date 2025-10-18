package com.ipcc.ipccchurch.ui.screens.events

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.pulltorefresh.PullToRefreshContainer
import androidx.compose.material3.pulltorefresh.rememberPullToRefreshState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.ipcc.ipccchurch.ui.components.ShimmerListItem
import com.ipcc.ipccchurch.ui.screens.events.components.EventCard

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EventsScreen(
    eventsViewModel: EventsViewModel = viewModel()
) {
    val events by eventsViewModel.events
    val isLoading by eventsViewModel.isLoading
    val pullToRefreshState = rememberPullToRefreshState()

    Box(
        modifier = Modifier
            .fillMaxSize()
            .nestedScroll(pullToRefreshState.nestedScrollConnection)
    ) {
        if (isLoading && events.isEmpty()) {
            // Show shimmer only on the very first load
            LazyColumn(
                contentPadding = PaddingValues(16.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                items(5) {
                    ShimmerListItem()
                }
            }
        } else {
            // Show the real event cards
            LazyColumn(
                modifier = Modifier.fillMaxSize(),
                contentPadding = PaddingValues(16.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                items(items = events, key = { it.id }) { event ->
                    EventCard(event = event)
                }
            }
        }

        // Logic to trigger the refresh
        if (pullToRefreshState.isRefreshing) {
            LaunchedEffect(true) {
                eventsViewModel.refresh()
            }
        }

        // Logic to stop the refresh indicator
        LaunchedEffect(isLoading) {
            if (!isLoading) {
                pullToRefreshState.endRefresh()
            }
        }

        // The visual refresh indicator
        PullToRefreshContainer(
            state = pullToRefreshState,
            modifier = Modifier.align(Alignment.TopCenter)
        )
    }
}

@Preview(showBackground = true)
@Composable
fun EventsScreenPreview() {
    EventsScreen()
}