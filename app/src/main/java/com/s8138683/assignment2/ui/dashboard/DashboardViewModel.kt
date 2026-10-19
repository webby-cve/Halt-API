package com.s8138683.assignment2.ui.dashboard

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.s8138683.assignment2.data.repository.DashboardRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class DashboardViewModel @Inject constructor(
    private val dashboardRepository: DashboardRepository,
) : ViewModel() {

    private val _uiState = MutableStateFlow<DashboardUiState>(DashboardUiState.Loading)
    val uiState: StateFlow<DashboardUiState> = _uiState.asStateFlow()

    /** Loads the dashboard for the given keypass. Called from the Fragment with the nav argument. */
    fun loadDashboard(keypass: String) {
        viewModelScope.launch {
            _uiState.value = DashboardUiState.Loading
            dashboardRepository.getDashboard(keypass)
                .onSuccess { data ->
                    _uiState.value = DashboardUiState.Success(data)
                }
                .onFailure { error ->
                    _uiState.value = DashboardUiState.Error(
                        error.message ?: "Failed to load dashboard."
                    )
                }
        }
    }
}
