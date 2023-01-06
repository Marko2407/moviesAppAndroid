package com.mvukosav.moviesapp.data.mappers

import com.mvukosav.moviesapp.GetUserQuery
import com.mvukosav.moviesapp.domain.mappers.MoviesDataToDomainMapper
import com.mvukosav.moviesapp.domain.mappers.UserDataToDomainMapper
import com.mvukosav.moviesapp.domain.models.User

class UserDataToDomainMapperImpl(private val moviesDataToDomainMapper: MoviesDataToDomainMapper) : UserDataToDomainMapper {
    override fun userDataToDomain(user: GetUserQuery.UserInfo): User {
        return User(
            userFullName = user.fullName,
            userId = user._id,
            email = user.email,
            refreshToken = user.refreshToken,
            favoriteMovies = moviesDataToDomainMapper.setUserWatchList(user.favoriteMovies)
        )
    }
}