package com.app.dans_android.data.api.repository

import com.app.dans_android.data.api.model.Job
import com.app.dans_android.data.implementation.network.ApiResponse

interface JobRepository {

    suspend fun getJobList(
        description: String? = "",
        location: String? = "",
        fullTime: Boolean? = false,
        page: Int? = 1,
    ): ApiResponse<List<Job>>

    suspend fun getJobDetail(jobId: String): ApiResponse<Job>
}
