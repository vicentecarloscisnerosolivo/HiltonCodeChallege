package com.vcco.hiltoncodechallenge.network

import androidx.room.Query
import com.vcco.hiltoncodechallenge.model.ApiResponse
import retrofit2.http.GET
import retrofit2.http.Path

interface SearchIPService {
    @GET("json/{query}")
    suspend fun getIpInfo(
        @Path(value = "query") queryValue: String
    ): ApiResponse
}