package com.app.dans_android.data.implementation.network

import com.app.dans_android.BuildConfig
import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import java.util.concurrent.TimeUnit

object ApiConfig {

    private const val RETROFIT_TIMEOUT = 30L

    /**
     * this function for setup generator json convert with moshi
     */
    fun provideMoshi(): Moshi = Moshi.Builder()
        .add(KotlinJsonAdapterFactory())
        .build()

    /**
     * this function for setup http client like connection, read, write timeout
     * and hit remote config
     *
     * @return OkHttpClient.Builder for setup to retrofit
     */
    fun provideHttpClient(): OkHttpClient.Builder = OkHttpClient.Builder()
            .connectTimeout(RETROFIT_TIMEOUT, TimeUnit.SECONDS)
            .readTimeout(RETROFIT_TIMEOUT, TimeUnit.SECONDS)
            .writeTimeout(RETROFIT_TIMEOUT, TimeUnit.SECONDS)
            .cache(null)

    /**
     * this function for API Instance
     *
     * @param okHttp [provideHttpClient] http client
     * @param moshi [provideMoshi] generator JSON
     *
     * @return [Retrofit]
     */
    fun provideRetrofit(
        okHttp: OkHttpClient.Builder,
        moshi: Moshi,
    ): Retrofit {
        val okHttpBuild = okHttp
            .retryOnConnectionFailure(true)
            .followRedirects(true)
            .followSslRedirects(true)
            .loggingInterceptor()
            .build()
        return Retrofit.Builder()
            .baseUrl(BuildConfig.API_ENDPOINT)
            .addConverterFactory(MoshiConverterFactory.create(moshi))
            .client(okHttpBuild)
            .build()
    }

    /**
     * this function for handling logging
     */
    private fun OkHttpClient.Builder.loggingInterceptor() = apply {
        if (BuildConfig.DEBUG) {
            this.addInterceptor(
                HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BODY)
            )
        }
    }
}