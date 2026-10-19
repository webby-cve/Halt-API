package com.s8138683.assignment2.ui.dashboard

import com.s8138683.assignment2.data.model.DashboardData

sealed interface DashboardUiState {
    data object Loading : DashboardUiState
    data class Success(val data: DashboardData) : DashboardUiState
    data class Error(val message: String) : DashboardUiState
}
