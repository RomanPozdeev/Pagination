package com.example.pagination.data.photos.remote

import com.google.gson.annotations.SerializedName

data class PhotoResponse(
    @SerializedName("albumId")
    val albumId: Long,
    @SerializedName("id")
    val id: Long,
    @SerializedName("title")
    val title: String,
    @SerializedName("url")
    val url: String,
    @SerializedName("thumbnailUrl")
    val thumbnailUrl: String
)