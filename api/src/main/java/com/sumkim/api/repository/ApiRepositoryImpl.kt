package com.sumkim.api.repository

import com.sumkim.api.call.ApiClient
import com.sumkim.api.call.ApiInterface
import com.sumkim.api.call.requestApi
import com.sumkim.api.request.GetV3SearchBookRequest

class ApiRepositoryImpl: ApiRepository {
    override suspend fun getV3SearchBook(
        getV3SearchBookRequest: GetV3SearchBookRequest
    ) = requestApi(
        ApiClient.createService(ApiInterface::class.java).getV3SearchBook(
            getV3SearchBookRequest.query,
            getV3SearchBookRequest.sort,
            getV3SearchBookRequest.page,
            getV3SearchBookRequest.size,
            getV3SearchBookRequest.target
        )
    )
}