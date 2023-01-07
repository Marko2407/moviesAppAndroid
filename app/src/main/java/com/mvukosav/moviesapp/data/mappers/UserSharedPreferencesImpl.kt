package com.mvukosav.moviesapp.data.mappers

import android.content.Context
import android.content.SharedPreferences
import com.mvukosav.moviesapp.domain.mappers.UserSharedPreferences
import dagger.hilt.android.internal.Contexts

class UserSharedPreferencesImpl(context: Context) : UserSharedPreferences {

    private var userSharedPreferences: SharedPreferences =
        Contexts.getApplication(context).getSharedPreferences(
            USER_SHARED_PREFERENCES,
            Context.MODE_PRIVATE
        )

    override fun getUserIdSP(): String = userSharedPreferences.getString(USER_ID, "") ?: ""

    override fun getUserAccessTokenSP(): String =
        userSharedPreferences.getString(USER_ACCESS_TOKEN, "") ?: ""

    override fun getUserRefreshTokenSP(): String =
        userSharedPreferences.getString(USER_REFRESH_TOKEN, "") ?: ""

    override fun isUserLoggedInSP(): Boolean =
        userSharedPreferences.getBoolean(IS_USER_LOGGED_IN, IS_USER_LOGGED_IN_DEFAULT)

    override fun setUserStringValueSp(key: String, newValue: String) {
        val sp = userSharedPreferences.edit()
        sp.putString(key, newValue)
        sp.apply()
    }

    override fun setUserBooleanValueSp(key: String, newValue: Boolean) {
        val sp = userSharedPreferences.edit()
        sp.putBoolean(key, newValue)
        sp.apply()
    }

    companion object {
        const val USER_SHARED_PREFERENCES = "USER_SHARED_PREFERENCES"
        const val IS_USER_LOGGED_IN = "IS_USER_LOGGED_IN"
        const val USER_ID = "USER_ID"
        const val USER_ACCESS_TOKEN = "USER_ACCESS_TOKEN"
        const val USER_REFRESH_TOKEN = "USER_REFRESH_TOKEN"
        private const val SP_DEFAULT_VALUE = ""
        private const val IS_USER_LOGGED_IN_DEFAULT = false
    }
}