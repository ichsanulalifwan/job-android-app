package com.app.dans_android.data.job.implementation.network

sealed interface ApiResponse<out T> {
    data object Loading : ApiResponse<Nothing>

    open class Error(open val message: String) : ApiResponse<Nothing>

    data class Success<T>(
        val data: T,
        val meta: Map<String, Any?> = mapOf(),
    ) : ApiResponse<T>
}
