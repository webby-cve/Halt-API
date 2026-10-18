package com.example.studentidassignment2.ui.dashboard

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.studentidassignment2.data.AppRepository
import com.example.studentidassignment2.data.DashboardEntity
import com.example.studentidassignment2.data.Resource
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

sealed class DashboardUiState {
    data object Idle : DashboardUiState()
    data object Loading : DashboardUiState()
    data class Success(val entities: List<DashboardEntity>) : DashboardUiState()
    data class Error(val message: String) : DashboardUiState()
}

class DashboardViewModel(private val repository: AppRepository) : ViewModel() {

    private val _uiState = MutableStateFlow<DashboardUiState>(DashboardUiState.Idle)
    val uiState: StateFlow<DashboardUiState> = _uiState.asStateFlow()

    fun loadDashboard(keypass: String) {
        if (keypass.isBlank()) {
            _uiState.value = DashboardUiState.Error("No keypass was provided.")
            return
        }

        viewModelScope.launch {
            _uiState.value = DashboardUiState.Loading
            when (val response = repository.getDashboard(keypass)) {
                is Resource.Success -> {
                    val entities = response.data.entities.ifEmpty {
                        emptyList()
                    }
                    _uiState.value = DashboardUiState.Success(entities)
                }

                is Resource.Error -> {
                    _uiState.value = DashboardUiState.Error(response.message)
                }
            }
        }
    }

    fun clearState() {
        _uiState.value = DashboardUiState.Idle
    }
}
