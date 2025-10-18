package com.ipcc.ipccchurch.ui.screens.home.components

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.dp
import com.ipcc.ipccchurch.ui.components.shimmerEffect

@Composable
fun HomeScreenSkeleton() {
    // A single column that holds all the placeholders
    Column(
        modifier = Modifier.fillMaxSize().padding(vertical = 16.dp),
        verticalArrangement = Arrangement.spacedBy(24.dp)
    ) {
        // 1. Placeholder for the Image Slider
        Spacer(
            modifier = Modifier
                .fillMaxWidth()
                .height(200.dp)
                .padding(horizontal = 16.dp)
                .clip(RoundedCornerShape(12.dp))
                .shimmerEffect()
        )

        // 2. Placeholder for a Horizontal List (Latest Sermons)
        ShimmerHorizontalListPlaceholder(titleWidthFraction = 0.5f)

        // 3. Placeholder for another Horizontal List (Sermon Series)
        ShimmerHorizontalListPlaceholder(titleWidthFraction = 0.4f)

        // 4. Placeholder for the Radio Card
        Spacer(
            modifier = Modifier
                .fillMaxWidth()
                .height(70.dp) // Approximate height of RadioPlayerCard
                .padding(horizontal = 16.dp)
                .clip(RoundedCornerShape(8.dp))
                .shimmerEffect()
        )
    }
}

// This is a private helper function used only within this file
@Composable
private fun ShimmerHorizontalListPlaceholder(titleWidthFraction: Float) {
    Column(verticalArrangement = Arrangement.spacedBy(16.dp)) {
        // Title placeholder
        Spacer(
            modifier = Modifier
                .height(28.dp)
                .fillMaxWidth(titleWidthFraction)
                .padding(horizontal = 16.dp)
                .shimmerEffect()
        )
        // Row of card placeholders
        LazyRow(
            contentPadding = PaddingValues(horizontal = 16.dp),
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            items(3) {
                Spacer(
                    modifier = Modifier
                        .width(160.dp)
                        .height(180.dp)
                        .clip(RoundedCornerShape(8.dp))
                        .shimmerEffect()
                )
            }
        }
    }
}