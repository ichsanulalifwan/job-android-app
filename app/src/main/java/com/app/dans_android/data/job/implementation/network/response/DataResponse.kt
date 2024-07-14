package com.app.dans_android.data.job.implementation.network.response

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class DataResponse<T>(
    @Json(name = "data")
    val data: T,
)
