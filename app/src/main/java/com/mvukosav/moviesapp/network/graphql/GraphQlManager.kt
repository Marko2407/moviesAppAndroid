package com.mvukosav.moviesapp.network.graphql

import com.apollographql.apollo3.ApolloClient
import com.apollographql.apollo3.network.okHttpClient
import com.mvukosav.moviesapp.network.interceptors.CommonNetworkInterceptor
import com.mvukosav.moviesapp.network.interceptors.TokenInterceptor
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor

object GraphQlManager {

    private var instance: ApolloClient? = null

    @Volatile
    var accessToken: String? = null

    fun apolloClient(): ApolloClient {
        if (instance != null) {
            return instance!!
        }

        instance = ApolloClient.Builder()
            .serverUrl("https://movies-app-marko2407.koyeb.app/graphql")
            .okHttpClient(returnOkHttpClient())
            .build()

        return instance!!
    }

    fun setToken(accessToken: String?) {
        GraphQlManager.accessToken = accessToken
    }

    private fun returnOkHttpClient(): OkHttpClient {
        return OkHttpClient.Builder()
            .addInterceptor(TokenInterceptor(this))
            .addInterceptor(CommonNetworkInterceptor())
            .addInterceptor(HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BODY))
            .build()
    }
}