package com.mvukosav.moviesapp.domain.models

import com.mvukosav.moviesapp.network.graphql.GraphQlManager

data class AuthData(
    val userId: String,
    val token: String,
    val expiredToken: Int,
    val refreshToken: String
){
    init {
        GraphQlManager.accessToken = token
    }
}