package com.ipcc.ipccchurch.ui.screens.auth

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.ipcc.ipccchurch.R

@Composable
fun AuthScaffold(
    title: String,
    footerText: String,
    footerLinkText: String,
    onFooterLinkClick: () -> Unit,
    content: @Composable ColumnScope.() -> Unit
) {
    // THE FIX: Use a Box to safely align content to the top and bottom.
    Box(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 24.dp, vertical = 16.dp)
    ) {
        // --- Top & Middle Content (aligned to the top) ---
        Column(
            modifier = Modifier.align(Alignment.TopCenter),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Spacer(modifier = Modifier.height(32.dp))
            Image(
                painter = painterResource(id = R.drawable.church_logo),
                contentDescription = "App Logo",
                modifier = Modifier.size(100.dp)
            )
            Spacer(modifier = Modifier.height(24.dp))
            Text(text = title, style = MaterialTheme.typography.headlineLarge)
            Spacer(modifier = Modifier.height(32.dp))

            // This is where the TextFields and Buttons go
            content()
        }

        // --- Bottom Footer (aligned to the bottom) ---
        Row(
            modifier = Modifier.align(Alignment.BottomCenter),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(footerText, style = MaterialTheme.typography.bodyMedium)
            TextButton(onClick = onFooterLinkClick) {
                Text(footerLinkText, fontWeight = FontWeight.Bold)
            }
        }
    }
}