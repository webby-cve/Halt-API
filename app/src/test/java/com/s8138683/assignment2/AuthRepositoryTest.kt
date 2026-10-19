package com.s8138683.assignment2

import com.s8138683.assignment2.data.model.LoginRequest
import com.s8138683.assignment2.data.model.LoginResponse
import com.s8138683.assignment2.data.remote.ApiService
import com.s8138683.assignment2.data.repository.AuthRepository
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.ResponseBody.Companion.toResponseBody
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Test
import retrofit2.HttpException
import retrofit2.Response
import java.io.IOException

class AuthRepositoryTest {

    private val apiService: ApiService = mockk()
    private val repository = AuthRepository(apiService)

    @Test
    fun `login returns keypass on success`() = runTest {
        coEvery { apiService.login(LoginRequest("8138683", "Arpan")) } returns
            LoginResponse("plants")

        val result = repository.login("8138683", "Arpan")

        assertTrue(result.isSuccess)
        assertEquals("plants", result.getOrNull())
    }

    @Test
    fun `login fails when server returns blank keypass`() = runTest {
        coEvery { apiService.login(any()) } returns LoginResponse("   ")

        val result = repository.login("8138683", "Arpan")

        assertTrue(result.isFailure)
    }

    @Test
    fun `login maps HttpException to friendly invalid-credentials message`() = runTest {
        coEvery { apiService.login(any()) } throws httpException(401)

        val result = repository.login("8138683", "wrong")

        assertTrue(result.isFailure)
        assertEquals(
            "Invalid username or password. Please try again.",
            result.exceptionOrNull()?.message,
        )
    }

    @Test
    fun `login maps IOException to network error message`() = runTest {
        coEvery { apiService.login(any()) } throws IOException("timeout")

        val result = repository.login("8138683", "Arpan")

        assertEquals(
            "Network error. Please check your internet connection.",
            result.exceptionOrNull()?.message,
        )
    }

    private fun httpException(code: Int): HttpException = HttpException(
        Response.error<String>(code, "error".toResponseBody("text/plain".toMediaType()))
    )
}
