package com.mvukosav.moviesapp.domain.models

data class MoviesByCategories(
    val category: String = "",
    val listOfMovies: MutableList<Movie> = mutableListOf()
)
