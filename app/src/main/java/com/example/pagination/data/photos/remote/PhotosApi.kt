package com.example.pagination.data.photos.remote

import io.reactivex.Single
import retrofit2.http.GET

interface PhotosApi {
    @GET("/photos")
    fun photos(): Single<List<PhotoResponse>>
}