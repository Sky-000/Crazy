package com.android.crazy.data.repository

import com.android.crazy.common.network.adapter.exceptionOrNull
import com.android.crazy.common.network.adapter.getOrNull
import com.android.crazy.common.network.adapter.isSuccess
import com.android.crazy.common.network.adapter.toResult
import com.android.crazy.common.network.service.UserService
import com.android.crazy.common.room.dao.UserDao
import com.android.crazy.common.network.result.NetworkResult
import com.android.crazy.data.model.LoginForm
import com.android.crazy.data.model.User
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class UserRepository @Inject constructor(
    private val service: UserService,
    private val dao: UserDao
) {
    suspend fun insertUserRemote(user: User) = flow {
        emit(NetworkResult.Loading)
        emit(service.insert(user).toResult())
    }

    suspend fun insertUserLocal(user: User) = dao.insert(user)

    suspend fun insertUserBatchRemote(users: List<User>) = flow {
        emit(NetworkResult.Loading)
        emit(service.insertBatch(users).toResult())
    }

    suspend fun insertUserBatchLocal(users: List<User>) = dao.insertBatch(users)

    suspend fun updateUserRemote(user: User) = flow {
        emit(NetworkResult.Loading)
        emit(service.update(user).toResult())
    }

    suspend fun updateUserLocal(user: User) = dao.update(user)

    suspend fun updateUserBatchRemote(users: List<User>) = flow {
        emit(NetworkResult.Loading)
        emit(service.updateBatch(users).toResult())
    }

    suspend fun updateUserBatchLocal(users: List<User>) = dao.updateBatch(users)

    suspend fun deleteUserRemote(user: User) = flow {
        emit(NetworkResult.Loading)
        emit(service.delete(user).toResult())
    }

    suspend fun deleteUserLocal(user: User) = dao.delete(user)

    suspend fun deleteUserBatchRemote(users: List<User>) = flow {
        emit(NetworkResult.Loading)
        emit(service.deleteBatch(users).toResult())
    }

    suspend fun deleteUserBatchLocal(users: List<User>) = dao.deleteBatch(users)

    suspend fun getAllUserRemote() = flow {
        emit(NetworkResult.Loading)
        emit(service.getAll().toResult())
    }

    fun getAllUserLocal() = dao.getAll()


    suspend fun getUserRemote(id: Int) = flow {
        emit(NetworkResult.Loading)
        emit(service.getUser(id).toResult())
    }

    fun getUserLocal(id: Int) = dao.getUser(id)

    suspend fun login(loginForm: LoginForm) = flow {
        emit(NetworkResult.Loading)
        emit(service.login(loginForm).toResult())
    }

}