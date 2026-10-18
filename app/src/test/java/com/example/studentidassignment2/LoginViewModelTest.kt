package com.example.studentidassignment2

import com.example.studentidassignment2.data.AppRepository
import com.example.studentidassignment2.data.LoginResponse
import com.example.studentidassignment2.data.Resource
import com.example.studentidassignment2.ui.login.LoginUiState
import com.example.studentidassignment2.ui.login.LoginViewModel
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Rule
import org.junit.Test

class LoginViewModelTest {

    @get:Rule
    val mainDispatcherRule = MainDispatcherRule()

    @OptIn(ExperimentalCoroutinesApi::class)
    @Test
    fun `login emits success when repository returns keypass`() = runTest(mainDispatcherRule.testDispatcher) {
        val repository = mockk<AppRepository>()
        coEvery { repository.login("12345678", "Alice", "footscray") } returns Resource.Success(LoginResponse("topicName"))

        val viewModel = LoginViewModel(repository)
        viewModel.login("12345678", "Alice", "footscray")
        advanceUntilIdle()

        val state = viewModel.uiState.value
        assertTrue(state is LoginUiState.Success)
        assertEquals("topicName", (state as LoginUiState.Success).keypass)
    }
}
