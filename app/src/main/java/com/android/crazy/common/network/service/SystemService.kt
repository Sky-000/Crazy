package com.android.crazy.common.network.service

import com.android.crazy.common.network.adapter.NetworkResponse
import com.android.crazy.data.model.Version
import retrofit2.http.GET
import retrofit2.http.Query

interface SystemService : BaseService {

    @GET("/version")
    suspend fun getVersionInfo(@Query("id") id: Long): NetworkResponse<Version>
}