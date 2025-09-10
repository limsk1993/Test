package com.sumkim.api.repository

import com.sumkim.api.call.ApiResult
import com.sumkim.api.request.GetV3SearchBookRequest
import com.sumkim.api.response.GetV3SearchBookResponse

interface ApiRepository {
    suspend fun getV3SearchBook(
        getV3SearchBookRequest: GetV3SearchBookRequest,
    ): ApiResult<GetV3SearchBookResponse>
}