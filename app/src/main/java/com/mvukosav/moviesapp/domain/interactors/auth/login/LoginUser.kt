package com.mvukosav.moviesapp.domain.interactors.auth.login

import com.mvukosav.moviesapp.domain.models.AuthData
import com.mvukosav.moviesapp.domain.repositories.AuthRepository
import com.mvukosav.moviesapp.network.Response
import javax.inject.Inject

class SignInUser @Inject constructor(private val authRepository: AuthRepository) {

    suspend operator fun invoke(email: String, password: String): Response<AuthData?> =
        authRepository.signInUser(email, password)
}