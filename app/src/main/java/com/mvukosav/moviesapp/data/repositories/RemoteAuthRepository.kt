package com.mvukosav.moviesapp.data.repositories

import com.mvukosav.moviesapp.GetUserQuery
import com.mvukosav.moviesapp.domain.models.User
import com.mvukosav.moviesapp.domain.repositories.AuthRepository
import com.mvukosav.moviesapp.network.Response
import com.mvukosav.moviesapp.network.graphql.GraphQlManager
import javax.inject.Inject

class RemoteAuthRepository @Inject constructor(): AuthRepository {
    override suspend fun loggedInUser(): Response<User> {

        try {
            val response = GraphQlManager.apolloClient().query(GetUserQuery("63af0afc4228af3a6392ea08")).execute()
            val user = response.data?.userInfo ?: return Response.Error(201, "Can not found user")
            return Response.Result(
                User(user._id, user.email, user.refreshToken, user.favoriteMovies)
            )

        }catch (e: Exception){
            return Response.Error(400, e.localizedMessage)
        }
    }
}