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
    val rating: Double? = 0.0,
    var isAddedToWatchList: Boolean = false
)
