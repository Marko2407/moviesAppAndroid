package com.mvukosav.moviesapp.domain.mappers

import com.mvukosav.moviesapp.CreateUserMutation
import com.mvukosav.moviesapp.GetUserQuery
import com.mvukosav.moviesapp.LoginUserQuery
import com.mvukosav.moviesapp.domain.models.AuthData
import com.mvukosav.moviesapp.domain.models.User

interface UserDataToDomainMapper {

    fun userDataToDomain(user: GetUserQuery.UserInfo): User

    fun createdUserDataToDomain(user: CreateUserMutation.CreateUser): User

    fun authDataToDomain(user: LoginUserQuery.Login): AuthData
}