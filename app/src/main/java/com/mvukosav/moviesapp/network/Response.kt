package com.mvukosav.moviesapp.network

sealed class Response<T> {
    data class Result<T>(val result: T) : Response<T>()
    data class Error<T>(val statusCode: Int, val message: String) : Response<T>()
    data class ErrorApi<T>(val error: ErrorCode) : Response<T>()
    class NetworkError<T> : Response<T>()
}