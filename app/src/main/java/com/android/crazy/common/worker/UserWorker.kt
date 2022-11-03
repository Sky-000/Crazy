package com.android.crazy.common.worker

import android.content.Context
import androidx.hilt.work.HiltWorker
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import com.android.crazy.common.network.result.NetworkResult
import com.android.crazy.data.repository.UserRepository
import com.android.crazy.utils.Logger
import dagger.assisted.Assisted
import dagger.assisted.AssistedInject
import javax.inject.Inject

@HiltWorker
class UserWorker @AssistedInject constructor(
    @Assisted appContext: Context,
    @Assisted workerParams: WorkerParameters,
) : CoroutineWorker(appContext, workerParams) {

    @Inject
    lateinit var userRepository: UserRepository

    override suspend fun doWork(): Result {
        try {
            userRepository.getUserRemote(1).collect {
                when (it) {
                    is NetworkResult.Loading -> {
                        Logger.d("UserWorker", "Loading")
                    }
                    is NetworkResult.Success -> {
                        Logger.d("UserWorker", "doWork: ${it.data}")
                    }
                    is NetworkResult.Error -> {
                        Logger.d("UserWorker", "doWork: ${it.throwable}")
                    }
                }
            }
            return Result.success()
        } catch (e: Exception) {
            return Result.failure()
        }
    }

}