package com.mvukosav.moviesapp.domain.interactors.splash

import com.mvukosav.moviesapp.domain.models.User
import com.mvukosav.moviesapp.domain.repositories.AuthRepository
import com.mvukosav.moviesapp.network.Response
import javax.inject.Inject

class LogoutUser @Inject constructor(
    private val authRepository: AuthRepository
){
   operator fun invoke(): Boolean = authRepository.logout()
}