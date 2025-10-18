package com.ipcc.ipccchurch.ui.screens.events

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ipcc.ipccchurch.RetrofitClient
import com.ipcc.ipccchurch.models.Event
import kotlinx.coroutines.launch

class EventsViewModel : ViewModel() {

    private val _events = mutableStateOf<List<Event>>(emptyList())
    val events: State<List<Event>> = _events

    private val _isLoading = mutableStateOf(true)
    val isLoading: State<Boolean> = _isLoading

    init {
        loadEvents()
    }

    // NEW: Public function to trigger a refresh
    fun refresh() {
        loadEvents()
    }

    private fun loadEvents() {
        viewModelScope.launch {
            _isLoading.value = true
            try {
                _events.value = RetrofitClient.instance.getEvents()
            } catch (e: Exception) {
                println("API Error in EventsViewModel: ${e.message}")
            } finally {
                _isLoading.value = false
            }
        }
    }
}