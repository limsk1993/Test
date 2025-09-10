package com.sumkim.api.call

import com.sumkim.api.response.GetV3SearchBookResponse
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query

interface ApiInterface {
    @GET("v3/search/book")
    fun getV3SearchBook(
        @Query("query")
        query: String,
        @Query("sort")
        sort: String?,
        @Query("page")
        page: Int?,
        @Query("size")
        size: Int?,
        @Query("target")
        target: String?,
    ): Call<GetV3SearchBookResponse>
}