package com.app.dans_android.data.job.implementation.response

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class JobResponse(

    @Json(name="id")
    val id: String? = null,

    @Json(name="company_logo")
    val companyLogo: String? = null,

    @Json(name="how_to_apply")
    val howToApply: String? = null,

    @Json(name="description")
    val description: String? = null,

    @Json(name="company")
    val company: String? = null,

    @Json(name="company_url")
    val companyUrl: String? = null,

    @Json(name="location")
    val location: String? = null,

    @Json(name="type")
    val type: String? = null,

    @Json(name="title")
    val title: String? = null,

    @Json(name="url")
    val url: String? = null,
)
