package com.s8138683.assignment2.data.model

import com.google.gson.annotations.SerializedName
import com.google.gson.JsonObject

data class DashboardResponse(
    @SerializedName("entities") val entities: List<JsonObject>,
    @SerializedName("entityTotal") val entityTotal: Int,
)
