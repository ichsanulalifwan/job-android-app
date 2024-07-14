package com.app.dans_android.di

import com.app.dans_android.data.api.repository.JobRepository
import com.app.dans_android.data.implementation.network.ApiConfig
import com.app.dans_android.data.implementation.network.JobApiService
import com.app.dans_android.data.implementation.repository.JobRepositoryImpl
import com.app.dans_android.data.qualifier.DefaultDispatcher
import com.app.dans_android.data.qualifier.IoDispatcher
import com.app.dans_android.data.qualifier.MainDispatcher
import com.squareup.moshi.Moshi
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Provides
    @MainDispatcher
    fun provideMainDispatcher(): CoroutineDispatcher = Dispatchers.Main

    @Provides
    @DefaultDispatcher
    fun provideDefaultDispatcher(): CoroutineDispatcher = Dispatchers.Default

    @Provides
    @IoDispatcher
    fun provideIoDispatcher(): CoroutineDispatcher = Dispatchers.IO

    @Provides
    fun provideMoshi(): Moshi = ApiConfig.provideMoshi()

    @Provides
    fun provideOkHttpClient(): OkHttpClient.Builder = ApiConfig.provideHttpClient()

    @Provides
    @Singleton
    fun provideRetrofit(
        okHttpClient: OkHttpClient.Builder,
        moshi: Moshi,
    ): Retrofit {
        return ApiConfig.provideRetrofit(
            okHttp = okHttpClient,
            moshi = moshi,
        )
    }

    @Provides
    @Singleton
    fun provideJobApiService(retrofit: Retrofit): JobApiService {
        return retrofit.create(JobApiService::class.java)
    }

    @Provides
    fun provideJobRepository(
        jobApi: JobApiService,
        @IoDispatcher ioDispatcher: CoroutineDispatcher,
    ): JobRepository {
        return JobRepositoryImpl(
            jobApi = jobApi,
            ioDispatcher = ioDispatcher,
        )
    }
}
