package com.s8138683.assignment2.data.repository

import com.s8138683.assignment2.data.model.DashboardData
import com.s8138683.assignment2.data.model.toEntityItem
import com.s8138683.assignment2.data.remote.ApiService
import retrofit2.HttpException
import java.io.IOException
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class DashboardRepository @Inject constructor(
    private val apiService: ApiService,
) {

    /**
     * Fetches the dashboard entities for the given keypass.
     */
    suspend fun getDashboard(keypass: String): Result<DashboardData> = try {
        val response = apiService.getDashboard(keypass)
        Result.success(
            DashboardData(
                entities = response.entities.map { it.toEntityItem() },
                entityTotal = response.entityTotal,
            )
        )
    } catch (e: HttpException) {
        Result.failure(Exception("Failed to load dashboard (HTTP ${e.code()})."))
    } catch (e: IOException) {
        Result.failure(Exception("Network error. Please check your internet connection."))
    } catch (e: Exception) {
        Result.failure(e)
    }
}
