package com.mvukosav.moviesapp.domain.interactors.auth.registration

import com.mvukosav.moviesapp.domain.models.User
import com.mvukosav.moviesapp.domain.repositories.AuthRepository
import com.mvukosav.moviesapp.network.Response
import javax.inject.Inject

class CreateUser @Inject constructor(private val authRepository: AuthRepository) {

    suspend operator fun invoke(
        fullName: String,
        email: String,
        password: String
    ): Response<User?> = authRepository.createNewUser(fullName, email, password)
}