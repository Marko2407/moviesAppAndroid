package com.mvukosav.moviesapp.domain.mappers

import com.mvukosav.moviesapp.GetUserQuery
import com.mvukosav.moviesapp.domain.models.User

interface UserDataToDomainMapper {

    fun userDataToDomain(user: GetUserQuery.UserInfo): User
}