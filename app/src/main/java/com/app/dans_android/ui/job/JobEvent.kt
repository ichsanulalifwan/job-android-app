package com.app.dans_android.ui.job

sealed class JobEvent {
    data object DefaultJob : JobEvent()
    data class SearchJob(
        val searchDescription: String = "",
        val searchLocation: String = "",
        val isFullTime: Boolean = false,
    ) : JobEvent()

    data object LoadMore : JobEvent()
    data class JobDetail(val jobId: String = "") : JobEvent()
    data object ShowFilter : JobEvent()
}
