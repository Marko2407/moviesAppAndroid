package com.mvukosav.moviesapp.domain.models


data class Movie(
    val movieId: String = "",
    val title: String,
    val description: String,
    val category: List<String>,
    val duration: Int,
    val releaseDate: String,
    val url: String? = "",
    val img: String? = "",
    var isAddedToWatchList: Boolean = false
)
