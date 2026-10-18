package com.example.studentidassignment2

import com.example.studentidassignment2.data.AppRepository
import com.example.studentidassignment2.data.DashboardEntity
import com.example.studentidassignment2.data.DashboardResponse
import com.example.studentidassignment2.data.Resource
import com.example.studentidassignment2.ui.dashboard.DashboardUiState
import com.example.studentidassignment2.ui.dashboard.DashboardViewModel
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Rule
import org.junit.Test

class DashboardViewModelTest {

    @get:Rule
    val mainDispatcherRule = MainDispatcherRule()

    @OptIn(ExperimentalCoroutinesApi::class)
    @Test
    fun `loadDashboard emits entities when repository succeeds`() = runTest(mainDispatcherRule.testDispatcher) {
        val repository = mockk<AppRepository>()
        val entity = DashboardEntity(property1 = "Alpha", property2 = "Beta", description = "Detailed description")
        coEvery { repository.getDashboard("topicName") } returns Resource.Success(DashboardResponse(listOf(entity), 1))

        val viewModel = DashboardViewModel(repository)
        viewModel.loadDashboard("topicName")
        advanceUntilIdle()

        val state = viewModel.uiState.value
        assertTrue(state is DashboardUiState.Success)
        assertEquals(1, (state as DashboardUiState.Success).entities.size)
        assertEquals("Alpha", state.entities.first().property1)
    }
}
