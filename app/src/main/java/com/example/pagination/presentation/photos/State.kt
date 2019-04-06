package com.example.pagination.presentation.photos

import com.example.pagination.domain.photos.Photo

data class State(
    val emptyProgress: Boolean = false,
    val emptyView: Boolean = false,
    val refreshProgress: Boolean = false,
    val pageProgress: Boolean = false,
    val showData: Boolean = false,
    val data: List<Photo> = listOf(),
    val showPageError: Boolean = false,
    val showEmptyError: Boolean = false,
    val error: Throwable? = null
)