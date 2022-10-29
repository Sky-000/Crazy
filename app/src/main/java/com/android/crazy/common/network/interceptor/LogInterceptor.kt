package com.android.crazy.common.network.interceptor

import com.android.crazy.BuildConfig
import com.android.crazy.utils.Logger
import okhttp3.logging.HttpLoggingInterceptor

val logInterceptor: HttpLoggingInterceptor by lazy {
    HttpLoggingInterceptor { Logger.d(msg = it) }.setLevel(if (BuildConfig.DEBUG) HttpLoggingInterceptor.Level.BODY else HttpLoggingInterceptor.Level.BASIC)
}