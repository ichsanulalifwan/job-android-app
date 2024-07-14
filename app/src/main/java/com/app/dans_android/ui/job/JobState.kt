package com.app.dans_android.ui.job

import com.app.dans_android.data.api.model.Job

data class JobState(
    val isLoading: Boolean = false,
    val isLoadingPaginate: Boolean = false,
    val isLoadMore: Boolean = false,
    val isUISuccessShowing: Boolean = false,
    val jobList: List<Job> = emptyList(),
    val jobDetail: Job? = null,
    val searchDescription: String = "",
    val searchLocation: String = "",
    val isFullTime: Boolean = false,
    val isError: Boolean = false,
    val errorMessage: String = "",
    val currentPage: Int = 1,
    val nextPage: Int = 1,
)