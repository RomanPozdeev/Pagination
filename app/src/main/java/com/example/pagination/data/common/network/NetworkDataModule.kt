package com.example.pagination.data.common.network

import com.example.pagination.BuildConfig
import dagger.Binds
import dagger.Module
import dagger.Provides
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import javax.inject.Singleton

@Module(
    includes = [
        NetworkDataModule.Bind::class]
)
class NetworkDataModule {
    @Provides
    @Singleton
    fun provideHttpLoggingInterceptor(): HttpLoggingInterceptor {
        return HttpLoggingInterceptor().apply {
            level = if (BuildConfig.DEBUG) {
                HttpLoggingInterceptor.Level.BODY
            } else {
                HttpLoggingInterceptor.Level.NONE
            }
        }
    }

    @Provides
    @Singleton
    fun provideOkHttpClient(
        loggingInterceptor: HttpLoggingInterceptor,
        noNetworkInterceptor: NoNetworkInterceptor
    ): OkHttpClient {
        return OkHttpClient().newBuilder()
            .addNetworkInterceptor(noNetworkInterceptor)
            .addInterceptor(loggingInterceptor)
            .build()
    }

    @Module
    interface Bind {
        @Binds
        fun bind(impl: NoNetworkInterceptorImpl): NoNetworkInterceptor
    }
}