package com.ipcc.ipccchurch.ui.screens.settings

import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ChevronRight
import androidx.compose.material.ripple.rememberRipple
import androidx.compose.material3.Divider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp

@Composable
fun SettingsScreen() {
    var pushNotificationsEnabled by remember { mutableStateOf(true) }

    LazyColumn(
        modifier = Modifier.fillMaxSize()
    ) {
        item {
            SettingsHeader("Appearance")
            SettingsClickableItem(
                title = "Theme",
                subtitle = "System Default",
                onClick = { /* TODO: Show theme dialog */ }
            )
        }
        item {
            SettingsHeader("Notifications")
            SettingsToggleItem(
                title = "Push Notifications",
                subtitle = "Receive updates on new sermons and events",
                checked = pushNotificationsEnabled,
                onCheckedChange = { pushNotificationsEnabled = it }
            )
        }
        item {
            SettingsHeader("More Info")
            SettingsClickableItem(
                title = "Privacy Policy",
                onClick = { /* TODO: Open Privacy Policy URL */ }
            )
            Divider(modifier = Modifier.padding(horizontal = 16.dp))
            SettingsClickableItem(
                title = "Terms of Service",
                onClick = { /* TODO: Open Terms of Service URL */ }
            )
        }
    }
}

@Composable
private fun SettingsHeader(title: String) { /* ... No changes here ... */ }

@Composable
private fun SettingsClickableItem(title: String, subtitle: String? = null, onClick: () -> Unit) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            // THIS IS THE FIX
            .clickable(
                interactionSource = remember { MutableInteractionSource() },
                indication = rememberRipple(),
                onClick = onClick
            )
            .padding(16.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Column(modifier = Modifier.weight(1f)) {
            Text(text = title, style = MaterialTheme.typography.bodyLarge)
            if (subtitle != null) {
                Text(text = subtitle, style = MaterialTheme.typography.bodyMedium, color = MaterialTheme.colorScheme.onSurfaceVariant)
            }
        }
        Icon(imageVector = Icons.Default.ChevronRight, contentDescription = null)
    }
}

@Composable
private fun SettingsToggleItem(title: String, subtitle: String, checked: Boolean, onCheckedChange: (Boolean) -> Unit) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            // THIS IS THE FIX
            .clickable(
                interactionSource = remember { MutableInteractionSource() },
                indication = rememberRipple(),
                onClick = { onCheckedChange(!checked) }
            )
            .padding(16.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Column(modifier = Modifier.weight(1f)) {
            Text(text = title, style = MaterialTheme.typography.bodyLarge)
            Text(text = subtitle, style = MaterialTheme.typography.bodyMedium, color = MaterialTheme.colorScheme.onSurfaceVariant)
        }
        Spacer(modifier = Modifier.width(16.dp))
        Switch(checked = checked, onCheckedChange = onCheckedChange)
    }
}

@Preview(showBackground = true)
@Composable
fun SettingsScreenPreview() {
    SettingsScreen()
}