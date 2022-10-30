package com.android.crazy.common.network.service

import com.android.crazy.common.network.adapter.NetworkResponse
import com.android.crazy.data.model.User
import retrofit2.http.*

interface UserService : BaseService {

    @POST("/user")
    suspend fun insert(@Body user: User): NetworkResponse<Boolean>

    @POST("/user/batch")
    suspend fun insertBatch(@Body users: List<User>): NetworkResponse<Boolean>

    @PUT("/user")
    suspend fun update(@Body user: User): NetworkResponse<Boolean>

    @PUT("/user/batch")
    suspend fun updateBatch(@Body users: List<User>): NetworkResponse<Boolean>

    @DELETE("/user")
    suspend fun delete(@Body user: User): NetworkResponse<Boolean>

    @DELETE("/user/batch")
    suspend fun deleteBatch(@Body users: List<User>): NetworkResponse<Boolean>

    @GET("/user/list")
    suspend fun getAll(): NetworkResponse<List<User>>

    @GET("/user")
    suspend fun getUser(@Query("id") id: Int): NetworkResponse<User>

    @POST("/user/login")
    suspend fun login(@Body user: User): NetworkResponse<User>
}