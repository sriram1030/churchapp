package com.ipcc.ipccchurch.ui.screens.splash

import android.content.Context
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ipcc.ipccchurch.RetrofitClient
import com.ipcc.ipccchurch.SessionManager
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch

// Represents the possible outcomes of the authentication check
sealed class AuthState {
    object Loading : AuthState()
    object Authenticated : AuthState()
    object Unauthenticated : AuthState()
}

class SplashViewModel : ViewModel() {

    private val _authState = mutableStateOf<AuthState>(AuthState.Loading)
    val authState: State<AuthState> = _authState

    fun checkAuthStatus(context: Context) {
        viewModelScope.launch {
            val sessionManager = SessionManager(context)
            val token = sessionManager.authToken.first()

            if (token.isNullOrEmpty()) {
                _authState.value = AuthState.Unauthenticated
                return@launch
            }

            // Try to validate the token by fetching the user profile
            try {
                RetrofitClient.instance.getUserProfile("Bearer $token")
                // If the call succeeds, the token is valid
                _authState.value = AuthState.Authenticated
            } catch (e: Exception) {
                // If the call fails (e.g., 401 Unauthorized), the token is invalid
                sessionManager.clearAuthToken() // Clear the bad token
                _authState.value = AuthState.Unauthenticated
            }
        }
    }
}