package com.ipcc.ipccchurch.ui.screens.auth

import android.content.Context
import android.util.Log
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ipcc.ipccchurch.RetrofitClient
import com.ipcc.ipccchurch.SessionManager
import com.ipcc.ipccchurch.models.UserLogin
import com.ipcc.ipccchurch.models.UserRegistration
import kotlinx.coroutines.launch

class AuthViewModel : ViewModel() {
    private val _isLoading = mutableStateOf(false)
    val isLoading: State<Boolean> = _isLoading

    private val _errorMessage = mutableStateOf<String?>(null)
    val errorMessage: State<String?> = _errorMessage

    fun registerUser(
        name: String,
        email: String,
        pass: String,
        context: Context,
        onSuccess: () -> Unit
    ) {
        viewModelScope.launch {
            _isLoading.value = true
            _errorMessage.value = null
            try {
                val user = UserRegistration(name = name, email = email, password = pass)
                val response = RetrofitClient.instance.registerUser(user)
                SessionManager(context).saveAuthToken(response.token)
                Log.d("AuthViewModel", "Registration successful for ${response.user.name}")
                onSuccess()
            } catch (e: Exception) {
                _errorMessage.value = "Registration failed: ${e.message}"
                Log.e("AuthViewModel", "Registration exception: ", e)
            } finally {
                _isLoading.value = false
            }
        }
    }

    fun loginUser(
        email: String,
        pass: String,
        context: Context,
        onSuccess: () -> Unit
    ) {
        viewModelScope.launch {
            _isLoading.value = true
            _errorMessage.value = null
            try {
                val user = UserLogin(email = email, password = pass)
                val response = RetrofitClient.instance.loginUser(user)
                SessionManager(context).saveAuthToken(response.token)
                Log.d("AuthViewModel", "Login successful for ${response.user.name}")
                onSuccess()
            } catch (e: Exception) {
                _errorMessage.value = "Login failed: ${e.message}"
                Log.e("AuthViewModel", "Login exception: ", e)
            } finally {
                _isLoading.value = false
            }
        }
    }
}