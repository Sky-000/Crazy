package com.android.crazy.di

import com.android.crazy.common.network.adapter.ErrorHandler
import com.android.crazy.common.network.adapter.NetworkResponseAdapterFactory
import com.android.crazy.common.network.converter.GsonConverterFactory
import com.android.crazy.common.network.interceptor.logInterceptor
import com.android.crazy.common.network.service.*
import com.android.crazy.utils.Constants
import com.android.crazy.utils.Logger
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import javax.inject.Singleton

@InstallIn(SingletonComponent::class)
@Module
object RetrofitModule {

    @Singleton
    @Provides
    fun getOkHttpClient(): OkHttpClient {
        return OkHttpClient.Builder()
            .addInterceptor(logInterceptor)
            .build()
    }

    @Singleton
    @Provides
    fun getRetrofit(okHttpClient: OkHttpClient): Retrofit {
        return Retrofit.Builder()
            .baseUrl(Constants.BASE_URL)
            .client(okHttpClient)
            .addCallAdapterFactory(NetworkResponseAdapterFactory(object : ErrorHandler {
                override fun bizError(code: Int, msg: String) {
                    Logger.d(msg = "bizError: code:$code - msg: $msg")
                }

                override fun otherError(throwable: Throwable) {
                    Logger.e(msg = throwable.message.toString(), throwable = throwable)
                }
            }))
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }

    @Singleton
    @Provides
    fun provideSystemService(retrofit: Retrofit): SystemService {
        return retrofit.create(SystemService::class.java)
    }

    @Singleton
    @Provides
    fun provideUserService(retrofit: Retrofit): UserService {
        return retrofit.create(UserService::class.java)
    }

}