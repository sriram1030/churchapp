package com.ipcc.ipccchurch.ui.screens.events

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ipcc.ipccchurch.RetrofitClient
import com.ipcc.ipccchurch.models.Event
import kotlinx.coroutines.launch

class EventsViewModel : ViewModel() {

    private val _churchEvents = mutableStateOf<List<Event>>(emptyList())
    val churchEvents: State<List<Event>> = _churchEvents

    private val _birthdays = mutableStateOf<List<Event>>(emptyList())
    val birthdays: State<List<Event>> = _birthdays

    private val _weddings = mutableStateOf<List<Event>>(emptyList())
    val weddings: State<List<Event>> = _weddings

    private val _isLoading = mutableStateOf(true)
    val isLoading: State<Boolean> = _isLoading

    init {
        loadAllEvents()
    }

    // NEW: Public function to trigger a refresh
    fun refresh() {
        loadAllEvents()
    }

    private fun loadAllEvents() {
        viewModelScope.launch {
            _isLoading.value = true
            try {
                _churchEvents.value = RetrofitClient.instance.getEvents()
                _birthdays.value = RetrofitClient.instance.getEvents("birthday")
                _weddings.value = RetrofitClient.instance.getEvents("wedding")
            } catch (e: Exception) {
                println("API Error in EventsViewModel: ${e.message}")
            } finally {
                _isLoading.value = false
            }
        }
    }
}