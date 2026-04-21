package com.vcco.hiltoncodechallenge.network

import com.vcco.hiltoncodechallenge.model.ApiResponse
import retrofit2.http.GET

interface SearchIPService {
    @GET("json")
    suspend fun getIpInfo(): ApiResponse
}