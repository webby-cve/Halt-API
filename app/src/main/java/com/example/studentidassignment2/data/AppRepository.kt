package com.example.studentidassignment2.data

import retrofit2.Response

class AppRepository(private val api: Nit3213Api) {

    suspend fun login(username: String, password: String, location: String): Resource<LoginResponse> {
        return safeApiCall {
            api.login(location, LoginRequest(username, password))
        }
    }

    suspend fun getDashboard(keypass: String): Resource<DashboardResponse> {
        return safeApiCall {
            api.getDashboard(keypass)
        }
    }

    private suspend fun <T> safeApiCall(apiCall: suspend () -> Response<T>): Resource<T> {
        return try {
            val response = apiCall()
            if (response.isSuccessful) {
                val body = response.body()
                if (body != null) {
                    Resource.Success(body)
                } else {
                    Resource.Error("The server returned an empty response.")
                }
            } else {
                Resource.Error("Request failed with status ${response.code()}")
            }
        } catch (exception: Exception) {
            Resource.Error("Unable to reach the API. Please try again.")
        }
    }
}
