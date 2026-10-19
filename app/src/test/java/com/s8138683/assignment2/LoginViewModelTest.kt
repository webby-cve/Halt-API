package com.s8138683.assignment2

import com.s8138683.assignment2.data.repository.AuthRepository
import com.s8138683.assignment2.ui.login.LoginUiState
import com.s8138683.assignment2.ui.login.LoginViewModel
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
class LoginViewModelTest {

    private val testDispatcher = UnconfinedTestDispatcher()
    private lateinit var authRepository: AuthRepository
    private lateinit var viewModel: LoginViewModel

    @Before
    fun setUp() {
        Dispatchers.setMain(testDispatcher)
        authRepository = mockk()
        viewModel = LoginViewModel(authRepository)
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun `empty username emits validation error and does not call repository`() = runTest {
        viewModel.login("", "Arpan")

        assertEquals(
            LoginUiState.Error("Username and password are required"),
            viewModel.uiState.value,
        )
        coVerify(exactly = 0) { authRepository.login(any(), any()) }
    }

    @Test
    fun `empty password emits validation error`() = runTest {
        viewModel.login("8138683", "  ")

        assertTrue(viewModel.uiState.value is LoginUiState.Error)
    }

    @Test
    fun `successful login emits loading then success with keypass`() = runTest {
        coEvery { authRepository.login("8138683", "Arpan") } returns
            Result.success("australian_plants")

        viewModel.login("8138683", "Arpan")

        val state = viewModel.uiState.value
        assertTrue(state is LoginUiState.Success)
        assertEquals("australian_plants", (state as LoginUiState.Success).keypass)
    }

    @Test
    fun `failed login emits error message`() = runTest {
        coEvery { authRepository.login("8138683", "arpan") } returns
            Result.failure(Exception("Invalid username or password. Please try again."))

        viewModel.login("8138683", "arpan")

        val state = viewModel.uiState.value
        assertTrue(state is LoginUiState.Error)
        assertEquals(
            "Invalid username or password. Please try again.",
            (state as LoginUiState.Error).message,
        )
    }
}
