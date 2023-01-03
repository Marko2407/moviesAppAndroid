package com.mvukosav.moviesapp.domain.interactors.home

import com.mvukosav.moviesapp.domain.models.User
import com.mvukosav.moviesapp.domain.repositories.AuthRepository
import javax.inject.Inject

class GetUser @Inject constructor(
    private val authRepository: AuthRepository
){
    operator fun invoke(): User? = authRepository.getUser()
}