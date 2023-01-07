package com.mvukosav.moviesapp.data.repositories

import com.mvukosav.moviesapp.CreateUserMutation
import com.mvukosav.moviesapp.GetUserQuery
import com.mvukosav.moviesapp.LoginUserQuery
import com.mvukosav.moviesapp.NewAccessTokenMutation
import com.mvukosav.moviesapp.data.mappers.UserSharedPreferencesImpl.Companion.IS_USER_LOGGED_IN
import com.mvukosav.moviesapp.data.mappers.UserSharedPreferencesImpl.Companion.USER_ACCESS_TOKEN
import com.mvukosav.moviesapp.data.mappers.UserSharedPreferencesImpl.Companion.USER_ID
import com.mvukosav.moviesapp.data.mappers.UserSharedPreferencesImpl.Companion.USER_REFRESH_TOKEN
import com.mvukosav.moviesapp.domain.mappers.UserDataToDomainMapper
import com.mvukosav.moviesapp.domain.mappers.UserSharedPreferences
import com.mvukosav.moviesapp.domain.models.AuthData
import com.mvukosav.moviesapp.domain.models.User
import com.mvukosav.moviesapp.domain.repositories.AuthRepository
import com.mvukosav.moviesapp.network.Response
import com.mvukosav.moviesapp.network.graphql.GraphQlManager
import com.mvukosav.moviesapp.type.UserInput
import com.mvukosav.moviesapp.utils.ErrorManager
import javax.inject.Inject

class RemoteAuthRepository @Inject constructor(
    private val userDataToDomainMapper: UserDataToDomainMapper,
    private val userSharedPreferences: UserSharedPreferences
) : AuthRepository {
    private var userData: User? = null

    override suspend fun loggedInUser(): Response<User?> {
        return try {
            val userIdSP = userSharedPreferences.getUserIdSP()
            if (userIdSP.isEmpty()) return Response.Result(null)

            val response =
                GraphQlManager.apolloClient().query(GetUserQuery(userIdSP))
                    .execute()
            val user = response.data?.userInfo
            when {
                ErrorManager.isErrorApi(response.errors) -> ErrorManager.parserApiError(response.errors)
                else -> {
                    if (user != null) {
                        val mappedUser = userDataToDomainMapper.userDataToDomain(user)
                        userData = mappedUser
                        return Response.Result(
                            mappedUser
                        )
                    } else {
                        Response.Result(null)
                    }
                }
            }

        } catch (e: Exception) {
            return ErrorManager.errorHandler(e)
        }
    }

    override fun logout(): Boolean {
        userSharedPreferences.setUserBooleanValueSp(IS_USER_LOGGED_IN, false)
        userSharedPreferences.setUserStringValueSp(USER_ID, "")
        userSharedPreferences.setUserStringValueSp(USER_ACCESS_TOKEN, "")
        userSharedPreferences.setUserStringValueSp(USER_REFRESH_TOKEN, "")
        return true
    }

    override suspend fun signInUser(email: String, password: String): Response<AuthData?> {
        return try {
            val response =
                GraphQlManager.apolloClient().query(LoginUserQuery(email, password))
                    .execute()
            val user = response.data?.login
            when {
                ErrorManager.isErrorApi(response.errors) -> ErrorManager.parserApiError(response.errors)
                else -> {
                    if (user != null) {
                        userSharedPreferences.setUserStringValueSp(USER_ID, user.userId)
                        userSharedPreferences.setUserStringValueSp(USER_ACCESS_TOKEN, user.token)
                        userSharedPreferences.setUserStringValueSp(
                            USER_REFRESH_TOKEN,
                            user.refreshToken
                        )
                        userSharedPreferences.setUserBooleanValueSp(IS_USER_LOGGED_IN, true)
                        Response.Result(userDataToDomainMapper.authDataToDomain(user))
                    } else {
                        Response.Result(null)
                    }
                }
            }
        } catch (e: Exception) {
            return ErrorManager.errorHandler(e)
        }
    }

    override suspend fun createNewUser(
        fullName: String,
        email: String,
        password: String
    ): Response<User?> {
        return try {
            val response =
                GraphQlManager.apolloClient()
                    .mutation(CreateUserMutation(UserInput(fullName, email, password)))
                    .execute()

            val createdUser = response.data?.createUser
            when {
                ErrorManager.isErrorApi(response.errors) -> ErrorManager.parserApiError(response.errors)
                else -> {
                    if (createdUser != null) {
                        // mapped user info
                        //set refresh token and user id

                        userSharedPreferences.setUserStringValueSp(USER_ID, createdUser._id)
                        createdUser.refreshToken?.let {
                            userSharedPreferences.setUserStringValueSp(
                                USER_REFRESH_TOKEN,
                                it
                            )
                        }
                        Response.Result(
                            userDataToDomainMapper.createdUserDataToDomain(createdUser)
                        )
                    } else {
                        Response.Result(null)
                    }
                }
            }
        } catch (e: Exception) {
            return ErrorManager.errorHandler(e)
        }
    }

    override suspend fun getNewAccessToken(): Response<String?> {
        //get new access token with userId and refreshToken from SP
        return try {

            val userIdSp = userSharedPreferences.getUserIdSP()
            val userRefreshTokenSp = userSharedPreferences.getUserIdSP()

            //if its null logout user
            if (userIdSp.isEmpty() || userRefreshTokenSp.isEmpty()) return Response.Result(null)

            val response =
                GraphQlManager.apolloClient()
                    .mutation(
                        NewAccessTokenMutation(
                            refreshToken = userRefreshTokenSp,
                            userId = userIdSp
                        )
                    )
                    .execute()

            val newAccessToken = response.data?.newAccessToken?.token
            when {
                ErrorManager.isErrorApi(response.errors) -> ErrorManager.parserApiError(response.errors)
                else -> {
                    if (newAccessToken != null) {
                        Response.Result(newAccessToken)
                    } else {
                        Response.Result(null)
                    }
                }
            }
        } catch (e: Exception) {
            return ErrorManager.errorHandler(e)
        }
    }

    override fun getUser(): User? = userData

}