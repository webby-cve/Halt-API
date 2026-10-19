package com.s8138683.assignment2

import com.s8138683.assignment2.data.model.DashboardData
import com.s8138683.assignment2.data.model.EntityItem
import com.s8138683.assignment2.data.model.FieldEntry
import com.s8138683.assignment2.data.repository.DashboardRepository
import com.s8138683.assignment2.ui.dashboard.DashboardUiState
import com.s8138683.assignment2.ui.dashboard.DashboardViewModel
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class DashboardViewModelTest {

    private val testDispatcher = UnconfinedTestDispatcher()
    private lateinit var dashboardRepository: DashboardRepository
    private lateinit var viewModel: DashboardViewModel

    private val testData = DashboardData(
        entities = listOf(
            EntityItem(
                fields = listOf(
                    FieldEntry("name", "Eucalyptus"),
                    FieldEntry("habitat", "Forest"),
                ),
                description = "A fast-growing Australian tree.",
            )
        ),
        entityTotal = 7,
    )

    @Before
    fun setUp() {
        Dispatchers.setMain(testDispatcher)
        dashboardRepository = mockk()
        viewModel = DashboardViewModel(dashboardRepository)
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun `successful load emits success with entities and total`() = runTest {
        coEvery { dashboardRepository.getDashboard("australian_plants") } returns
            Result.success(testData)

        viewModel.loadDashboard("australian_plants")

        val state = viewModel.uiState.value
        assertTrue(state is DashboardUiState.Success)
        val data = (state as DashboardUiState.Success).data
        assertEquals(7, data.entityTotal)
        assertEquals("Eucalyptus", data.entities[0].fields[0].value)
        assertEquals("A fast-growing Australian tree.", data.entities[0].description)
    }

    @Test
    fun `failed load emits error message`() = runTest {
        coEvery { dashboardRepository.getDashboard("bad_keypass") } returns
            Result.failure(Exception("Failed to load dashboard (HTTP 401)."))

        viewModel.loadDashboard("bad_keypass")

        val state = viewModel.uiState.value
        assertTrue(state is DashboardUiState.Error)
        assertEquals("Failed to load dashboard (HTTP 401).", (state as DashboardUiState.Error).message)
    }

    @Test
    fun `loadDashboard calls repository with the keypass`() = runTest {
        coEvery { dashboardRepository.getDashboard("australian_plants") } returns
            Result.success(testData)

        viewModel.loadDashboard("australian_plants")

        coVerify(exactly = 1) { dashboardRepository.getDashboard("australian_plants") }
    }
}
