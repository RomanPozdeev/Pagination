package com.example.pagination.domain.photos

data class Photo(
    val albumId: Long,
    val id: Long,
    val title: String,
    val url: String,
    val thumbnailUrl: String
)