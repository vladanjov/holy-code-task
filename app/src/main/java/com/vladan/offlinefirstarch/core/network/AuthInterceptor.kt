package com.vladan.offlinefirstarch.core.network

import com.vladan.offlinefirstarch.BuildConfig
import okhttp3.Interceptor
import okhttp3.Response

class AuthInterceptor : Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response {
        val request = chain.request().newBuilder()
            .addHeader("Authorization", "Bearer ${BuildConfig.FSQ_API_KEY}")
            .addHeader("Accept", "application/json")
            .addHeader("X-Places-Api-Version", "2025-06-17")
            .build()
        return chain.proceed(request)
    }
}
