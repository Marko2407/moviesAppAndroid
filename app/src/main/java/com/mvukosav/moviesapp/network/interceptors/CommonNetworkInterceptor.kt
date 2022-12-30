package com.mvukosav.moviesapp.network.interceptors

import okhttp3.Interceptor
import okhttp3.Response

class CommonNetworkInterceptor : Interceptor {

    override fun intercept(chain: Interceptor.Chain): Response {
        val request = chain.request()
        val build = request.newBuilder()
            .addHeader(ACCEPT_HEADER, "application/json")
            .addHeader(CONTENT_TYPE_HEADER, "application/json")
            .build()

        return chain.proceed(build)
    }

    companion object {
        private const val ACCEPT_HEADER = "Accept"
        private const val CONTENT_TYPE_HEADER = "Content-Type"
    }
}