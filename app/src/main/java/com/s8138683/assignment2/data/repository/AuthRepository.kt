package com.s8138683.assignment2.data.repository

import com.s8138683.assignment2.data.model.LoginRequest
import com.s8138683.assignment2.data.remote.ApiService
import retrofit2.HttpException
import java.io.IOException
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class AuthRepository @Inject constructor(
    private val apiService: ApiService,
) {

    /**
     * Authenticates against /sydney/auth and returns the keypass on success.
     */
    suspend fun login(username: String, password: String): Result<String> = try {
        val response = apiService.login(LoginRequest(username, password))
        if (response.keypass.isNotBlank()) {
            Result.success(response.keypass)
        } else {
            Result.failure(Exception("Login failed: empty keypass returned by server."))
        }
    } catch (e: HttpException) {
        Result.failure(Exception("Invalid username or password. Please try again."))
    } catch (e: IOException) {
        Result.failure(Exception("Network error. Please check your internet connection."))
    } catch (e: Exception) {
        Result.failure(e)
    }
}
