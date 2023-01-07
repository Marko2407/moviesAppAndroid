package com.mvukosav.moviesapp.data.mappers

import com.mvukosav.moviesapp.CreateUserMutation
import com.mvukosav.moviesapp.GetUserQuery
import com.mvukosav.moviesapp.LoginUserQuery
import com.mvukosav.moviesapp.domain.mappers.MoviesDataToDomainMapper
import com.mvukosav.moviesapp.domain.mappers.UserDataToDomainMapper
import com.mvukosav.moviesapp.domain.models.AuthData
import com.mvukosav.moviesapp.domain.models.User

class UserDataToDomainMapperImpl(private val moviesDataToDomainMapper: MoviesDataToDomainMapper) :
    UserDataToDomainMapper {
    override fun userDataToDomain(user: GetUserQuery.UserInfo): User {
        return User(
            userFullName = user.fullName,
            userId = user._id,
            email = user.email,
            refreshToken = user.refreshToken,
            favoriteMovies = moviesDataToDomainMapper.setUserWatchList(user.favoriteMovies)
        )
    }

    override fun createdUserDataToDomain(user: CreateUserMutation.CreateUser): User {
        return User(
            userFullName = user.fullName,
            userId = user._id,
            email = user.email,
            refreshToken = user.refreshToken,
            favoriteMovies = mutableListOf()
        )
    }

    override fun authDataToDomain(user: LoginUserQuery.Login): AuthData {
        return AuthData(
            userId = user.userId,
            token = user.token,
            expiredToken = user.tokenExpired,
            refreshToken = user.refreshToken
        )
    }
}