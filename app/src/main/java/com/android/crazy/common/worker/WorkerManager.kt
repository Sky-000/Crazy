package com.android.crazy.common.worker

import androidx.work.*
import com.android.crazy.ui.CrazyApplication
import com.android.crazy.utils.Constants
import java.util.concurrent.TimeUnit

/**
 * @author: 刘贺贺
 * @date: 2022-09-27 15:00
 */
class WorkerManager {

    private val userRequest by lazy {
        PeriodicWorkRequestBuilder<UserWorker>(60, TimeUnit.MINUTES)
            .setConstraints(
                Constraints.Builder()
                    .setRequiredNetworkType(NetworkType.CONNECTED)
                    .build()
            ).build()
    }

    fun createWorker() {
        WorkManager.getInstance(CrazyApplication.appContext).enqueueUniquePeriodicWork(
            Constants.BACKGROUND_WORKER_NAME_USER,
            ExistingPeriodicWorkPolicy.REPLACE,
            userRequest
        )
    }

    companion object {
        @JvmStatic
        val instance: WorkerManager by lazy(mode = LazyThreadSafetyMode.SYNCHRONIZED) {
            WorkerManager()
        }
    }
}