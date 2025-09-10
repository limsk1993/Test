package com.sumkim.api.request

import retrofit2.http.Query

data class GetV3SearchBookRequest(
    @Query("query")
    val query: String,

    @Query("sort")
    val sort: String? = null,

    @Query("page")
    val page: Int? = null,

    @Query("size")
    val size: Int? = null,

    @Query("target")
    val target: String? = null,
)
