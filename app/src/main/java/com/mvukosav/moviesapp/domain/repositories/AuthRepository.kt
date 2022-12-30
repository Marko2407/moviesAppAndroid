package com.mvukosav.moviesapp.domain.repositories

import com.mvukosav.moviesapp.domain.models.User
import com.mvukosav.moviesapp.network.Response

interface AuthRepository {

    suspend fun loggedInUser(): Response<User>
}