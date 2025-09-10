package com.sumkim.api.call

import com.google.gson.GsonBuilder
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object ApiClient {
    private const val BASEURL = "https://dapi.kakao.com/"

    fun <T> createService(
        serviceClass: Class<T>
    ): T {
        val client = OkHttpClient.Builder()
            .addInterceptor(ApiInterceptor())

        return Retrofit.Builder()
            .baseUrl(BASEURL)
            .client(client.build())
            .addConverterFactory(GsonConverterFactory.create(GsonBuilder().setLenient().create()))
            .build()
            .create(serviceClass)
    }
}