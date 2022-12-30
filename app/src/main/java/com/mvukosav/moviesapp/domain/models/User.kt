package com.mvukosav.moviesapp.domain.models

import com.mvukosav.moviesapp.GetUserQuery

data class User(
    val userId: String?,
    val email: String?,
    val refreshToken: String?,
    val favoriteMovies: List<GetUserQuery.FavoriteMovie?>?
)