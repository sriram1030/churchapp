package com.ipcc.ipccchurch.ui.screens.events

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.material3.pulltorefresh.PullToRefreshContainer
import androidx.compose.material3.pulltorefresh.rememberPullToRefreshState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.ipcc.ipccchurch.ui.components.ShimmerHorizontalList
import com.ipcc.ipccchurch.ui.screens.events.components.EventCard

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EventsScreen(
    eventsViewModel: EventsViewModel = viewModel()
) {
    val churchEvents by eventsViewModel.churchEvents
    val birthdays by eventsViewModel.birthdays
    val weddings by eventsViewModel.weddings
    val isLoading by eventsViewModel.isLoading
    val pullToRefreshState = rememberPullToRefreshState()

    Box(
        modifier = Modifier
            .fillMaxSize()
            .nestedScroll(pullToRefreshState.nestedScrollConnection)
    ) {
        if (isLoading && churchEvents.isEmpty() && birthdays.isEmpty() && weddings.isEmpty()) {
            // Show shimmer only on the very first, completely empty load
            LazyColumn(
                modifier = Modifier.fillMaxSize(),
                contentPadding = PaddingValues(vertical = 16.dp),
                verticalArrangement = Arrangement.spacedBy(24.dp)
            ) {
                items(3) { ShimmerHorizontalList() }
            }
        } else {
            // Show the real content once loaded
            LazyColumn(
                modifier = Modifier.fillMaxSize(),
                contentPadding = PaddingValues(vertical = 16.dp),
                verticalArrangement = Arrangement.spacedBy(24.dp)
            ) {
                // --- Church Events Section ---
                item {
                    SectionHeader("Church Events")
                    if (churchEvents.isNotEmpty()) {
                        LazyRow(
                            contentPadding = PaddingValues(horizontal = 16.dp),
                            horizontalArrangement = Arrangement.spacedBy(12.dp)
                        ) {
                            items(items = churchEvents, key = { it.id }) { event ->
                                EventCard(event = event, modifier = Modifier.width(300.dp))
                            }
                        }
                    } else {
                        EmptyStateMessage("No upcoming church events.")
                    }
                }

                // --- Upcoming Birthdays Section ---
                item {
                    SectionHeader("Upcoming Birthdays")
                    if (birthdays.isNotEmpty()) {
                        LazyRow(
                            contentPadding = PaddingValues(horizontal = 16.dp),
                            horizontalArrangement = Arrangement.spacedBy(12.dp)
                        ) {
                            items(items = birthdays, key = { it.id }) { event ->
                                EventCard(event = event, modifier = Modifier.width(300.dp))
                            }
                        }
                    } else {
                        EmptyStateMessage("No upcoming birthdays to show.")
                    }
                }

                // --- Upcoming Weddings Section ---
                item {
                    SectionHeader("Upcoming Weddings")
                    if (weddings.isNotEmpty()) {
                        LazyRow(
                            contentPadding = PaddingValues(horizontal = 16.dp),
                            horizontalArrangement = Arrangement.spacedBy(12.dp)
                        ) {
                            items(items = weddings, key = { it.id }) { event ->
                                EventCard(event = event, modifier = Modifier.width(300.dp))
                            }
                        }
                    } else {
                        EmptyStateMessage("No upcoming weddings to show.")
                    }
                }
            }
        }

        // Pull-to-refresh logic
        if (pullToRefreshState.isRefreshing) {
            LaunchedEffect(true) { eventsViewModel.refresh() }
        }
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

// Helper composable for the section titles
@Composable
private fun SectionHeader(title: String) {
    Text(
        text = title,
        style = MaterialTheme.typography.titleLarge,
        fontWeight = FontWeight.Bold,
        modifier = Modifier.padding(start = 16.dp, end = 16.dp, bottom = 8.dp)
    )
}

// Helper composable for the "empty" message
@Composable
private fun EmptyStateMessage(message: String) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = message,
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )
    }
}

@Preview(showBackground = true)
@Composable
fun EventsScreenPreview() {
    EventsScreen()
}