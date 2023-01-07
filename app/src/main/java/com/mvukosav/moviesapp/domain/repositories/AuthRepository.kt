package com.mvukosav.moviesapp.domain.repositories

import com.mvukosav.moviesapp.domain.models.AuthData
import com.mvukosav.moviesapp.domain.models.User
import com.mvukosav.moviesapp.network.Response

interface AuthRepository {

    suspend fun loggedInUser(): Response<User?>

    fun logout(): Boolean

    suspend fun signInUser(email: String, password: String): Response<AuthData?>

    suspend fun createNewUser(fullName: String, email: String, password: String): Response<User?>

    suspend fun getNewAccessToken(): Response<String?>

    fun getUser(): User?
}