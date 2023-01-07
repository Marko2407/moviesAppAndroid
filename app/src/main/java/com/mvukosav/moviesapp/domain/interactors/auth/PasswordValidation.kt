package com.mvukosav.moviesapp.domain.interactors.auth

import javax.inject.Inject

class PasswordValidation @Inject constructor(){

    operator fun invoke(password: String): Boolean {
        return password.length > 6
    }
}