package com.mvukosav.moviesapp.domain.interactors.auth

import javax.inject.Inject

class InputValidation @Inject constructor() {

    operator fun invoke(
        fullName: String,
        email: String,
        password: String,
        confirmPassword: String
    ): Boolean {
        if (fullName.isEmpty()
            || email.isEmpty()
            || password.isEmpty()
            || confirmPassword.isEmpty()
        ) return false

        if (!email.contains("@")) return false

        if (password != confirmPassword) return false

        return true
    }
}