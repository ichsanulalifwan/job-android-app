package com.app.dans_android.data.response

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class JobListResponse(

	@Json(name="Response")
	val response: List<JobListItemResponse?>? = null
)
