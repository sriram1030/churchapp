package com.ipcc.ipccchurch.ui.screens.profile

import android.content.Context
import android.util.Log
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ipcc.ipccchurch.RetrofitClient
import com.ipcc.ipccchurch.SessionManager
import com.ipcc.ipccchurch.models.ChangePasswordRequest
import com.ipcc.ipccchurch.models.UpdateNameRequest
import com.ipcc.ipccchurch.models.UserProfile
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch

class ProfileViewModel : ViewModel() {

    private val _userProfile = mutableStateOf<UserProfile?>(null)
    val userProfile: State<UserProfile?> = _userProfile

    private val _isLoading = mutableStateOf(false)
    val isLoading: State<Boolean> = _isLoading

    fun loadUserProfile(context: Context) {
        viewModelScope.launch {
            _isLoading.value = true
            val token = SessionManager(context).authToken.first()
            if (token.isNullOrEmpty()) {
                Log.e("ProfileViewModel", "Auth token is null or empty.")
                _isLoading.value = false
                return@launch
            }

            try {
                _userProfile.value = RetrofitClient.instance.getUserProfile("Bearer $token")
            } catch (e: Exception) {
                Log.e("ProfileViewModel", "Error fetching profile: ", e)
            } finally {
                _isLoading.value = false
            }
        }
    }

    fun updateName(context: Context, newName: String) {
        viewModelScope.launch {
            _isLoading.value = true
            val token = SessionManager(context).authToken.first()
            if (token.isNullOrEmpty()) { return@launch }
            try {
                val updatedProfile = RetrofitClient.instance.updateName("Bearer $token", UpdateNameRequest(name = newName))
                _userProfile.value = updatedProfile
            } catch (e: Exception) {
                Log.e("ProfileViewModel", "Error updating name: ", e)
            } finally {
                _isLoading.value = false
            }
        }
    }

    fun changePassword(context: Context, oldPass: String, newPass: String) {
        viewModelScope.launch {
            val token = SessionManager(context).authToken.first()
            if (token.isNullOrEmpty()) { return@launch }
            try {
                RetrofitClient.instance.changePassword("Bearer $token", ChangePasswordRequest(old_password = oldPass, new_password = newPass))
                // Optionally add a success message/state here
            } catch (e: Exception) {
                Log.e("ProfileViewModel", "Error changing password: ", e)
            }
        }
    }
}