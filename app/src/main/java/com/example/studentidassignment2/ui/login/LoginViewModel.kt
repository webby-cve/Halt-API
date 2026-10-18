package com.example.studentidassignment2.ui.login

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.studentidassignment2.data.AppRepository
import com.example.studentidassignment2.data.Resource
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

sealed class LoginUiState {
    data object Idle : LoginUiState()
    data object Loading : LoginUiState()
    data class Success(val keypass: String) : LoginUiState()
    data class Error(val message: String) : LoginUiState()
}

class LoginViewModel(private val repository: AppRepository) : ViewModel() {

    private val _uiState = MutableStateFlow<LoginUiState>(LoginUiState.Idle)
    val uiState: StateFlow<LoginUiState> = _uiState.asStateFlow()

    fun login(username: String, password: String, location: String) {
        val cleanedUsername = username.trim()
        val cleanedPassword = password.trim()

        if (cleanedUsername.isEmpty() || cleanedPassword.isEmpty()) {
            _uiState.value = LoginUiState.Error("Please enter both your student ID and first name.")
            return
        }

        viewModelScope.launch {
            _uiState.value = LoginUiState.Loading
            when (val response = repository.login(cleanedUsername, cleanedPassword, location)) {
                is Resource.Success -> {
                    if (response.data.keypass.isNotBlank()) {
                        _uiState.value = LoginUiState.Success(response.data.keypass)
                    } else {
                        _uiState.value = LoginUiState.Error("Login was successful but the keypass was empty.")
                    }
                }

                is Resource.Error -> {
                    _uiState.value = LoginUiState.Error(response.message)
                }
            }
        }
    }

    fun clearError() {
        _uiState.value = LoginUiState.Idle
    }
}
