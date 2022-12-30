package com.mvukosav.moviesapp.network.interceptors

import com.mvukosav.moviesapp.network.graphql.GraphQlManager
import okhttp3.Interceptor
import okhttp3.Response

class TokenInterceptor(
    private val graphQlManager: GraphQlManager
) : Interceptor {

    override fun intercept(chain: Interceptor.Chain): Response {
        val accessToken = graphQlManager.accessToken
        return chain.executeRequestWithAuthHeader(accessToken)
    }

    private fun Interceptor.Chain.executeRequestWithAuthHeader(accessToken: String?): Response {
        val requestBuilder = request().newBuilder()
        accessToken?.isNotBlank()?.let {
            requestBuilder.addHeader(AUTHORIZATION_HEADER_NAME, "$TOKEN_TYPE $accessToken")
        }
        return proceed(requestBuilder.build())
    }

    companion object {
        private const val AUTHORIZATION_HEADER_NAME = "Authorization"
        private const val TOKEN_TYPE = "Bearer"
    }
}