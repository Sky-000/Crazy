package com.android.crazy.data.repository

import com.android.crazy.common.network.adapter.exceptionOrNull
import com.android.crazy.common.network.adapter.getOrNull
import com.android.crazy.common.network.adapter.isSuccess
import com.android.crazy.common.network.result.NetworkResult
import com.android.crazy.common.network.service.UserService
import com.android.crazy.common.room.dao.UserDao
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
        emit(NetworkResult.Loading(true))
        val result = service.insert(user)
        if (result.isSuccess) {
            emit(NetworkResult.Success(result.getOrNull()))
        } else {
            emit(NetworkResult.Failure(result.exceptionOrNull()?.message ?: "未知错误"))
        }
    }

    suspend fun insertUserLocal(user: User) = dao.insert(user)

    suspend fun insertUserBatchRemote(users: List<User>) = flow {
        emit(NetworkResult.Loading(true))
        val result = service.insertBatch(users)
        if (result.isSuccess) {
            emit(NetworkResult.Success(result.getOrNull()))
        } else {
            emit(NetworkResult.Failure(result.exceptionOrNull()?.message ?: "未知错误"))
        }
    }

    suspend fun insertUserBatchLocal(users: List<User>) = dao.insertBatch(users)

    suspend fun updateUserRemote(user: User) = flow {
        emit(NetworkResult.Loading(true))
        val result = service.update(user)
        if (result.isSuccess) {
            emit(NetworkResult.Success(result.getOrNull()))
        } else {
            emit(NetworkResult.Failure(result.exceptionOrNull()?.message ?: "未知错误"))
        }
    }

    suspend fun updateUserLocal(user: User) = dao.update(user)

    suspend fun updateUserBatchRemote(users: List<User>) = flow {
        emit(NetworkResult.Loading(true))
        val result = service.updateBatch(users)
        if (result.isSuccess) {
            emit(NetworkResult.Success(result.getOrNull()))
        } else {
            emit(NetworkResult.Failure(result.exceptionOrNull()?.message ?: "未知错误"))
        }
    }

    suspend fun updateUserBatchLocal(users: List<User>) = dao.updateBatch(users)

    suspend fun deleteUserRemote(user: User) = flow {
        emit(NetworkResult.Loading(true))
        val result = service.delete(user)
        if (result.isSuccess) {
            emit(NetworkResult.Success(result.getOrNull()))
        } else {
            emit(NetworkResult.Failure(result.exceptionOrNull()?.message ?: "未知错误"))
        }
    }

    suspend fun deleteUserLocal(user: User) = dao.delete(user)

    suspend fun deleteUserBatchRemote(users: List<User>) = flow {
        emit(NetworkResult.Loading(true))
        val result = service.deleteBatch(users)
        if (result.isSuccess) {
            emit(NetworkResult.Success(result.getOrNull()))
        } else {
            emit(NetworkResult.Failure(result.exceptionOrNull()?.message ?: "未知错误"))
        }
    }

    suspend fun deleteUserBatchLocal(users: List<User>) = dao.deleteBatch(users)

    suspend fun getAllUserRemote() = flow {
        emit(NetworkResult.Loading(true))
        val result = service.getAll()
        if (result.isSuccess) {
            emit(NetworkResult.Success(result.getOrNull()))
        } else {
            emit(NetworkResult.Failure(result.exceptionOrNull()?.message ?: "未知错误"))
        }
    }

    fun getAllUserLocal() = dao.getAll()


    suspend fun getUserRemote(id: Int) = flow {
        emit(NetworkResult.Loading(true))
        val result = service.getUser(id)
        if (result.isSuccess) {
            emit(NetworkResult.Success(result.getOrNull()))
        } else {
            emit(NetworkResult.Failure(result.exceptionOrNull()?.message ?: "未知错误"))
        }
    }

    fun getUserLocal(id: Int) = dao.getUser(id)

    suspend fun login(email: String, password: String) = flow {
        emit(NetworkResult.Loading(true))
        delay(3000L)
        val result = service.login(LoginForm(email, password))
        if (result.isSuccess) {
            emit(NetworkResult.Success(result.getOrNull()))
        } else {
            emit(NetworkResult.Failure(result.exceptionOrNull()?.message ?: "未知错误"))
        }
    }

}