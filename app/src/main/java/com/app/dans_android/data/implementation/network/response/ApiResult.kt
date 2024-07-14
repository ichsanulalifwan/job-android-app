package com.app.dans_android.data.implementation.network.response

sealed class ApiResult<out T> {
    data class Success<out T>(
        val data: T,
    ) : ApiResult<T>()

    data class Error(
        val codeResponse: Int,
        val codeMessage: String,
        val message: String,
    ) : ApiResult<Nothing>()
}
