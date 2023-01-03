package com.mvukosav.moviesapp.domain.models

data class User(
    val userId: String?,
    val email: String?,
    val refreshToken: String?,
    val favoriteMovies: MutableList<Movie>
)