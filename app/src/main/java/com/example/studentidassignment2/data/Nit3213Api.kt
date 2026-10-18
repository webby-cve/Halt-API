package com.example.studentidassignment2.data

import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path

interface Nit3213Api {
    @POST("{location}/auth")
    suspend fun login(
        @Path("location") location: String,
        @Body request: LoginRequest
    ): Response<LoginResponse>

    @GET("dashboard/{keypass}")
    suspend fun getDashboard(
        @Path("keypass") keypass: String
    ): Response<DashboardResponse>
}

fun createNit3213Api(): Nit3213Api {
    return Retrofit.Builder()
        .baseUrl("https://nit3213apinew.onrender.com/")
        .addConverterFactory(GsonConverterFactory.create())
        .build()
        .create(Nit3213Api::class.java)
}
