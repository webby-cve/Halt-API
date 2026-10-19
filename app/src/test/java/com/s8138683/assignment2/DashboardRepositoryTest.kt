package com.s8138683.assignment2

import com.s8138683.assignment2.data.model.DashboardResponse
import com.s8138683.assignment2.data.remote.ApiService
import com.s8138683.assignment2.data.repository.DashboardRepository
import com.google.gson.JsonObject
import com.google.gson.JsonParser
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

class DashboardRepositoryTest {

    private val apiService: ApiService = mockk()
    private val repository = DashboardRepository(apiService)

    @Test
    fun `getDashboard maps entities and returns total`() = runTest {
        val entityJson = JsonParser.parseString(
            """{"name": "Eucalyptus", "habitat": "Forest", "description": "A tree."}"""
        ).asJsonObject
        coEvery { apiService.getDashboard("plants") } returns
            DashboardResponse(entities = listOf(entityJson), entityTotal = 7)

        val result = repository.getDashboard("plants")

        assertTrue(result.isSuccess)
        val data = result.getOrNull()!!
        assertEquals(7, data.entityTotal)
        assertEquals(1, data.entities.size)
        assertEquals("Eucalyptus", data.entities[0].fields[0].value)
        assertEquals("A tree.", data.entities[0].description)
    }

    @Test
    fun `getDashboard maps HttpException to friendly message`() = runTest {
        coEvery { apiService.getDashboard("bad") } throws httpException(401)

        val result = repository.getDashboard("bad")

        assertTrue(result.isFailure)
        assertEquals("Failed to load dashboard (HTTP 401).", result.exceptionOrNull()?.message)
    }

    @Test
    fun `getDashboard maps IOException to network error message`() = runTest {
        coEvery { apiService.getDashboard("plants") } throws IOException("timeout")

        val result = repository.getDashboard("plants")

        assertEquals(
            "Network error. Please check your internet connection.",
            result.exceptionOrNull()?.message,
        )
    }

    private fun httpException(code: Int): HttpException = HttpException(
        Response.error<String>(code, "error".toResponseBody("text/plain".toMediaType()))
    )
}
