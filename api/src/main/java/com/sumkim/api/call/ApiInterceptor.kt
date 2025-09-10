package com.sumkim.api.call

import okhttp3.Interceptor
import okhttp3.Response

class ApiInterceptor: Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response {
        val builder = chain.request().newBuilder()
        builder.addHeader("Authorization", AUTHORIZATION)
        return chain.proceed(builder.build())
    }

    companion object {
        private const val AUTHORIZATION = "KakaoAK a248d1c906f0240d2c0a059ca9e4676d"
    }
}