package com.s8138683.assignment2.data.model

import com.google.gson.annotations.SerializedName

data class LoginResponse(
    @SerializedName("keypass") val keypass: String,
)
