package com.mvukosav.moviesapp.domain.models

data class User(
    val userId: String?,
    val email: String?,
    val refreshToken: String?,
    val favoriteMovies: MutableList<String>,
    val userFullName: String? = "",
    var userInitials: String? = ""
) { init {
    createUserInitials()
}

    private fun createUserInitials() {
        if (userFullName != null) {
            val userName = userFullName.split(" ")
            val firstLetter = userName.first().first().toString()
            val secondLetter = userName.last().first().toString()
            userInitials = (firstLetter + secondLetter).uppercase()
        }
    }
}