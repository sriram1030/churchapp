package com.ipcc.ipccchurch.ui.screens.events

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.ipcc.ipccchurch.models.Event
import com.ipcc.ipccchurch.ui.screens.events.components.EventCard

@Composable
fun EventsScreen() {
    // Create fake data for the list
    val fakeEvents = listOf(
        Event(id = "1", title = "Youth Fellowship", description = "7:00 PM at the Main Hall", month = "OCT", day = "25"),
        Event(id = "2", title = "Sunday Service", description = "10:00 AM in the Sanctuary", month = "OCT", day = "27"),
        Event(id = "3", title = "Community Outreach", description = "9:00 AM, Downtown Mission", month = "NOV", day = "02"),
        Event(id = "4", title = "Choir Practice", description = "6:30 PM in the Music Room", month = "NOV", day = "05")
    )

    LazyColumn(
        contentPadding = PaddingValues(16.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        items(items = fakeEvents, key = { event -> event.id }) { event ->
            EventCard(event = event)
        }
    }
}

@Preview(showBackground = true)
@Composable
fun EventsScreenPreview() {
    EventsScreen()
}