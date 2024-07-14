package com.app.dans_android.data.job.implementation.network

import com.app.dans_android.data.job.implementation.network.response.DataResponse
import com.app.dans_android.data.job.implementation.response.JobResponse
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface JobApiService {

    @GET("recruitment/positions.json")
    suspend fun getJobList(
        @Query("description") description: String?,
        @Query("location") location: String?,
        @Query("full_time") fullTime: Boolean?,
        @Query("page") page: Int?,
    ): Response<DataResponse<List<JobResponse>>>

    @GET("recruitment/positions/{id}")
    suspend fun getJobDetail(
        @Path("id") jobId: String,
    ): Response<DataResponse<JobResponse>>
}
