package com.example.pagination.data.photos.remote

import com.example.pagination.BuildConfig
import com.example.pagination.common.SchedulersProvider
import dagger.Lazy
import dagger.Module
import dagger.Provides
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton

@Module
class PhotosApiModule {
    @Provides
    @Singleton
    fun provideApi(httpClient: Lazy<OkHttpClient>, schedulers: SchedulersProvider): PhotosApi {
        return Retrofit.Builder()
            .baseUrl(BuildConfig.BASE_URL)
            .callFactory { httpClient.get().newCall(it) }
            .addCallAdapterFactory(RxJava2CallAdapterFactory.createWithScheduler(schedulers.io()))
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(PhotosApi::class.java)
    }

    @Provides
    @Singleton
    fun providePagedApi(api: PhotosApi): PagedApi {
        return PagedApiImpl(BuildConfig.PAGE_SIZE, api)
    }

}