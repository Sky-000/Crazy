package com.android.crazy.common.network.service

import com.android.crazy.common.network.adapter.NetworkResponse
import com.android.crazy.common.room.entity.User
import retrofit2.http.*

interface UserService : BaseService {

    @POST("/user")
    suspend fun insert(user: User): NetworkResponse<Boolean>

    @POST("/user/batch")
    suspend fun insertBatch(users: List<User>): NetworkResponse<Boolean>

    @PUT("/user")
    suspend fun update(user: User): NetworkResponse<Boolean>

    @PUT("/user/batch")
    suspend fun updateBatch(users: List<User>): NetworkResponse<Boolean>

    @DELETE("/user")
    suspend fun delete(user: User): NetworkResponse<Boolean>

    @DELETE("/user/batch")
    suspend fun deleteBatch(users: List<User>): NetworkResponse<Boolean>

    @GET("/user/list")
    suspend fun getAll(): NetworkResponse<List<User>>

    @GET("/user")
    suspend fun getUser(@Query("id") id: Int): NetworkResponse<User>
}