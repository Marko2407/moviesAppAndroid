package com.mvukosav.moviesapp.domain.mappers

interface UserSharedPreferences {

    fun getUserIdSP(): String

    fun getUserAccessTokenSP(): String

    fun getUserRefreshTokenSP(): String

    fun isUserLoggedInSP(): Boolean

    fun setUserStringValueSp(key: String, newValue: String)

    fun setUserBooleanValueSp(key: String, newValue: Boolean)
}