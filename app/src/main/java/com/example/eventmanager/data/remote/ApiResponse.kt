package com.example.eventmanager.data.remote

import com.google.gson.annotations.SerializedName

data class ApiBaseResponse<out T>(
    @SerializedName("status")
    val status: String = "",
    @SerializedName("message")
    val message: String = "",
    @SerializedName("data")
    val response: T? = null
)