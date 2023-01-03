package com.mvukosav.moviesapp.domain.repositories

import com.mvukosav.moviesapp.domain.models.User
import com.mvukosav.moviesapp.network.Response

interface AuthRepository {

    suspend fun loggedInUser(): Response<User>

    suspend fun logout(): Response<Boolean>

    suspend fun signInUser(email: String, password: String): Response<User>

    suspend fun createNewUser(email: String, password: String): Response<User>

    suspend fun getNewAccessToken(): Response<String>
}