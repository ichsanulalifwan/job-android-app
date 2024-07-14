package com.app.dans_android.data.implementation.network.response

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class ErrorResponse(
    @Json(name = "timestamp")
    val timestamp: String = "",
    @Json(name = "status")
    val status: String = "",
    @Json(name = "error")
    val error: String = "",
    @Json(name = "path")
    val path: String = "",
)
