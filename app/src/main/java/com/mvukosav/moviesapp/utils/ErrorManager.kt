package com.mvukosav.moviesapp.utils

import com.apollographql.apollo.exception.ApolloNetworkException
import com.mvukosav.moviesapp.network.ErrorCode
import com.mvukosav.moviesapp.network.Response
import java.io.IOException

object ErrorManager {

    fun isErrorApi(errorList: List<com.apollographql.apollo3.api.Error>?): Boolean {
        return errorList != null
    }

    fun <T> errorHandler(e: Throwable): Response<T> {
        return if (isNetworkError(e)) {
            Response.NetworkError()
        } else {
            Response.Error(-1, ErrorCode.UNKNOWN_ERROR.name)
        }
    }

    fun <T> parserApiError(errorList: List<com.apollographql.apollo3.api.Error>?): Response.ErrorApi<T> {
        errorList?.find {
            it.message == "Error fetching new REH Token"
        }?.let {
            return Response.ErrorApi(ErrorCode.AUTHORIZATION_ERROR)
        }
        return Response.ErrorApi(ErrorCode.UNKNOWN_ERROR)
    }

    private fun isNetworkError(e: Throwable): Boolean {
        return e is IOException || e is ApolloNetworkException
    }
}