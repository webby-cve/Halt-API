package com.example.studentidassignment2.data

import com.google.gson.annotations.SerializedName
import java.io.Serializable

sealed class Resource<out T> {
    data class Success<T>(val data: T) : Resource<T>()
    data class Error(val message: String) : Resource<Nothing>()
}

data class LoginResponse(
    @SerializedName("keypass") val keypass: String = ""
)

data class LoginRequest(
    @SerializedName("username") val username: String,
    @SerializedName("password") val password: String
)

data class DashboardResponse(
    @SerializedName("entities") val entities: List<DashboardEntity> = emptyList(),
    @SerializedName("entityTotal") val entityTotal: Int = 0
)

data class DashboardEntity(
    @SerializedName("property1") val property1: String? = null,
    @SerializedName("property2") val property2: String? = null,
    @SerializedName("property3") val property3: String? = null,
    @SerializedName("property4") val property4: String? = null,
    @SerializedName("description") val description: String = ""
) : Serializable {
    fun summary(): String {
        val values = listOfNotNull(property1, property2, property3, property4)
            .filter { it.isNotBlank() }
        return if (values.isNotEmpty()) values.joinToString(" • ") else "No summary available"
    }

    fun displayText(): String {
        val items = linkedMapOf(
            "property1" to property1,
            "property2" to property2,
            "property3" to property3,
            "property4" to property4,
            "description" to description
        ).filterValues { !it.isNullOrBlank() }

        return items.entries.joinToString(separator = "\n") { (key, value) -> "$key: $value" }
    }
}
