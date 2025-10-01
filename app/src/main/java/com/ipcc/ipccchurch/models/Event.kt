package com.ipcc.ipccchurch.models

data class Event(
    val id: String,
    val title: String,
    val description: String,
    val month: String, // e.g., "OCT"
    val day: String,   // e.g., "25"
)