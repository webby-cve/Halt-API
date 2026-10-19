package com.s8138683.assignment2.data.remote

import com.s8138683.assignment2.data.model.DashboardResponse
import com.s8138683.assignment2.data.model.LoginRequest
import com.s8138683.assignment2.data.model.LoginResponse
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path

interface ApiService {

    /** Sydney campus authentication endpoint. */
    @POST("sydney/auth")
    suspend fun login(@Body request: LoginRequest): LoginResponse

    /** Returns the list of entities for the authenticated topic. */
    @GET("dashboard/{keypass}")
    suspend fun getDashboard(@Path("keypass") keypass: String): DashboardResponse
}
