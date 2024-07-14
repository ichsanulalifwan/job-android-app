package com.app.dans_android.data.implementation.network.ext

import com.app.dans_android.data.implementation.network.response.ApiResult
import com.app.dans_android.data.implementation.network.response.DataResponse
import com.app.dans_android.data.implementation.network.response.ErrorResponse
import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import retrofit2.HttpException
import retrofit2.Response
import java.io.IOException
import java.net.UnknownHostException

suspend fun <T : Any> result(
    remoteApi: suspend () -> Response<DataResponse<T>>,
): ApiResult<T> = try {
    val response = remoteApi()
    handleResponse(response)
} catch (e: HttpException) {
    ApiResult.Error(e.code(), e.message.orEmpty(), e.message.orEmpty())
} catch (e: IOException) {
    ApiResult.Error(500, e.message.orEmpty(), e.message.orEmpty())
} catch (e: UnknownHostException) {
    ApiResult.Error(500, e.message.orEmpty(), e.message.orEmpty())
} catch (e: Exception) {
    ApiResult.Error(400, e.message.orEmpty(), e.message.orEmpty())
}

private fun <T : Any> handleResponse(response: Response<DataResponse<T>>): ApiResult<T> {
    val body = response.body()

    return if (response.isSuccessful && body != null) {
        ApiResult.Success(data = body.data)
    } else {
        val error = response.errorBody()?.source()?.let {
            Moshi.Builder()
                .add(KotlinJsonAdapterFactory()).build()
                .adapter(ErrorResponse::class.java)
                .fromJson(it)
        }

        ApiResult.Error(
            codeResponse = response.code(),
            codeMessage = error?.status.orEmpty(),
            message = error?.error.orEmpty(),
        )
    }
}
