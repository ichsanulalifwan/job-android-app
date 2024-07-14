package com.app.dans_android.data.implementation.repository

import com.app.dans_android.data.api.model.Job
import com.app.dans_android.data.api.repository.JobRepository
import com.app.dans_android.data.implementation.mapper.toJob
import com.app.dans_android.data.implementation.network.ApiResponse
import com.app.dans_android.data.implementation.network.JobApiService
import com.app.dans_android.data.implementation.network.ext.result
import com.app.dans_android.data.implementation.network.response.ApiResult
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.withContext

internal class JobRepositoryImpl(
    private val jobApi: JobApiService,
    private val ioDispatcher: CoroutineDispatcher,
) : JobRepository {

    override suspend fun getJobList(
        description: String?,
        location: String?,
        fullTime: Boolean?,
        page: Int?,
    ): ApiResponse<List<Job>> {
        return withContext(ioDispatcher) {
            val apiResult = result {
                jobApi.getJobList(
                    description = description,
                    location = location,
                    fullTime = fullTime,
                    page = page,
                )
            }

            when (apiResult) {
                is ApiResult.Success -> {
                    val jobResult = apiResult.data.map { it.toJob() }
                    ApiResponse.Success(data = jobResult)
                }

                is ApiResult.Error -> {
                    ApiResponse.Error(apiResult.message)
                }
            }
        }
    }

    override suspend fun getJobDetail(jobId: String): ApiResponse<Job> {
        return withContext(ioDispatcher) {
            val apiResult = result {
                jobApi.getJobDetail(jobId = jobId)
            }

            when (apiResult) {
                is ApiResult.Success -> {
                    val jobResult = apiResult.data.toJob()
                    ApiResponse.Success(data = jobResult)
                }

                is ApiResult.Error -> {
                    ApiResponse.Error(apiResult.message)
                }
            }
        }
    }
}
