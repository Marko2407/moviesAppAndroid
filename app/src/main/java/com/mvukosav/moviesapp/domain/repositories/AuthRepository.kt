package com.mvukosav.moviesapp.domain.repositories

import androidx.lifecycle.LiveData
import com.mvukosav.moviesapp.domain.models.User
import com.mvukosav.moviesapp.network.Response

interface AuthRepository {

    suspend fun loggedInUser(): Response<User>

    fun logout(): Boolean

    suspend fun signInUser(email: String, password: String): Response<User>

    suspend fun createNewUser(email: String, password: String): Response<User>

    suspend fun getNewAccessToken(): Response<String>

    fun getUser(): User?
}