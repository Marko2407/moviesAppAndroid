package com.mvukosav.moviesapp.data.repositories

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.mvukosav.moviesapp.GetUserQuery
import com.mvukosav.moviesapp.domain.mappers.UserDataToDomainMapper
import com.mvukosav.moviesapp.domain.models.User
import com.mvukosav.moviesapp.domain.repositories.AuthRepository
import com.mvukosav.moviesapp.network.ErrorCode
import com.mvukosav.moviesapp.network.Response
import com.mvukosav.moviesapp.network.graphql.GraphQlManager
import javax.inject.Inject

class RemoteAuthRepository @Inject constructor(
    private val userDataToDomainMapper: UserDataToDomainMapper,
    context: Context
) : AuthRepository {

    private var userData: User? = null

    override suspend fun loggedInUser(): Response<User> {
        try {
            //get userID from shared
            val response =
                GraphQlManager.apolloClient().query(GetUserQuery("63b4a592d59cfb4cc8dbe8fe"))
                    .execute()
            val user = response.data?.userInfo ?: return Response.Error(
                201,
                ErrorCode.USER_NOT_FOUND.name
            )

            val mappedUser = userDataToDomainMapper.userDataToDomain(user)
            userData =  mappedUser
            return Response.Result(
                mappedUser
            )

        } catch (e: Exception) {
            return Response.Error(400, e.localizedMessage)
        }
    }

    override fun logout(): Boolean{
        //logout user (delete from shared its refreshToken and userId)
        return true
    }

    override suspend fun signInUser(email: String, password: String): Response<User> {
        //sign in user
        //save user to shared preffs.
        return Response.Result(User("", "", "", mutableListOf()))
    }

    override suspend fun createNewUser(email: String, password: String): Response<User> {
        //create new user
        //ako prode sign inaj usera s tim
        return Response.Result(User("", "", "", mutableListOf()))
    }

    override suspend fun getNewAccessToken(): Response<String> {
        //get new access token with userId and refreshToken from SP
        return Response.Result("")
    }

    override fun getUser(): User? = userData

}