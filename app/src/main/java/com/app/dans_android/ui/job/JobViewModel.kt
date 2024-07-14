package com.app.dans_android.ui.job

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.app.dans_android.data.job.api.model.Job
import com.app.dans_android.data.job.api.repository.JobRepository
import com.app.dans_android.data.job.implementation.network.ApiResponse
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class JobViewModel @Inject constructor(
    private val jobRepository: JobRepository,
) : ViewModel() {

    private val _state = MutableStateFlow(JobState())
    val state get() = _state.asStateFlow()

    fun onEvent(event: JobEvent) {
        when (event) {
            is JobEvent.DefaultJob -> {
                loadingData()
                getJobList()
            }

            is JobEvent.SearchJob -> {
                loadingData()
                clearJobs()
                getJobList(
                    description = event.searchDescription,
                    location = event.searchLocation,
                    isFullTime = event.isFullTime,
                    page = _state.value.currentPage,
                )
            }

            is JobEvent.LoadMore -> {
                if (_state.value.isLoadMore) {
                    loadingPaginate()
                    getJobList(
                        description = _state.value.searchDescription,
                        location = _state.value.searchLocation,
                        isFullTime = _state.value.isFullTime,
                        page = _state.value.nextPage,
                    )
                }
            }

            is JobEvent.JobDetail -> {
                loadingData()
                getJobDetail(jobId = event.jobId)
            }
        }
    }

    private fun loadingData() {
        _state.update {
            it.copy(
                isLoading = true,
                isLoadingPaginate = false,
                isUISuccessShowing = false,
            )
        }
    }

    private fun loadingPaginate() {
        _state.update {
            it.copy(isLoadingPaginate = true)
        }
    }

    private fun getJobList(
        description: String = "",
        location: String = "",
        isFullTime: Boolean = false,
        page: Int = 1,
    ) = viewModelScope.launch {
        val response = jobRepository.getJobList(
            description = description,
            location = location,
            fullTime = isFullTime,
            page = page,
        )

        handleJobListResponse(
            response = response,
            description = description,
            location = location,
            isFullTime = isFullTime,
            page = page,
        )
    }

    private fun handleJobListResponse(
        response: ApiResponse<List<Job>>,
        description: String = "",
        location: String = "",
        isFullTime: Boolean = false,
        page: Int,
    ) {
        when (response) {
            is ApiResponse.Loading -> {
                _state.update {
                    it.copy(isLoading = true)
                }
            }

            is ApiResponse.Success -> {
                val totalPage = 2
                val nextPage = page + 1
                val isLoadMore = page < totalPage
                _state.update {
                    it.copy(
                        isLoading = false,
                        isLoadingPaginate = false,
                        isUISuccessShowing = true,
                        isLoadMore = isLoadMore,
                        currentPage = page,
                        nextPage = nextPage,
                        searchDescription = description,
                        searchLocation = location,
                        isFullTime = isFullTime,
                        jobList = if (page == 1) {
                            response.data
                        } else {
                            (state.value.jobList + (response.data)).distinct()
                        }
                    )
                }
            }

            is ApiResponse.Error -> {
                _state.update {
                    it.copy(
                        isError = true,
                        errorMessage = response.message,
                    )
                }
            }
        }
    }

    private fun getJobDetail(jobId: String = "") = viewModelScope.launch {
        val response = jobRepository.getJobDetail(jobId = jobId)

        handleJobDetailResponse(response = response)
    }

    private fun handleJobDetailResponse(
        response: ApiResponse<Job>,
    ) {
        when (response) {
            is ApiResponse.Loading -> {
                _state.update {
                    it.copy(isLoading = true)
                }
            }

            is ApiResponse.Success -> {
                _state.update {
                    it.copy(
                        isLoading = false,
                        jobDetail = response.data,
                    )
                }
            }

            is ApiResponse.Error -> {
                _state.update {
                    it.copy(
                        isError = true,
                        errorMessage = response.message,
                    )
                }
            }
        }
    }

    private fun clearJobs() {
        _state.update {
            it.copy(
                jobList = emptyList(),
                currentPage = 1,
            )
        }
    }
}
