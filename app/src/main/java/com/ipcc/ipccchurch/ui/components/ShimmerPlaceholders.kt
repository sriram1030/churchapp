package com.ipcc.ipccchurch.ui.components

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.dp

@Composable
fun ShimmerSliderPlaceholder() {
    Spacer(
        modifier = Modifier
            .fillMaxWidth()
            .height(200.dp)
            .padding(horizontal = 16.dp)
            .clip(RoundedCornerShape(12.dp))
            .shimmerEffect()
    )
}

@Composable
fun ShimmerHorizontalList(title: String? = null) {
    Column(verticalArrangement = Arrangement.spacedBy(16.dp)) {
        // Title placeholder
        Spacer(
            modifier = Modifier
                .height(28.dp)
                .fillMaxWidth(0.5f)
                .padding(horizontal = 16.dp)
                .clip(RoundedCornerShape(4.dp))
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