@file:Suppress("UNUSED")

package com.android.crazy.utils

import android.util.Log
import com.android.crazy.BuildConfig

/**
 * App 日志类
 */
object Logger {

    private const val DEFAULT_TAG = "ZkTony"

    /**
     * Debug 下开启
     */
    private val isDebug
        get() = BuildConfig.DEBUG

    private const val tag = DEFAULT_TAG

    /**
     * [Log.VERBOSE]
     */
    fun v(tag: String = DEFAULT_TAG, msg: String) {
        if (isDebug) {
            Log.v(tag, msg)
        }
    }

    /**
     * [Log.DEBUG]
     */
    fun d(tag: String = DEFAULT_TAG, msg: String) {
        if (isDebug) {
            Log.d(tag, msg)
        }
    }

    /**
     * [Log.INFO]
     */
    fun i(tag: String = DEFAULT_TAG, msg: String) {
        if (isDebug) {
            Log.i(tag, msg)
        }
    }

    /**
     * [Log.WARN]
     */
    fun w(tag: String = DEFAULT_TAG, msg: String) {
        if (isDebug) {
            Log.w(tag, msg)
        }
    }

    /**
     * [Log.ERROR]
     */
    fun e(tag: String = DEFAULT_TAG, msg: String) {
        if (isDebug) {
            Log.e(tag, msg)
        }
    }

    /**
     * [Log.ERROR]
     */
    fun e(tag: String = DEFAULT_TAG, msg: String = "", throwable: Throwable) {
        if (isDebug) {
            Log.e(tag, msg, throwable)
        }
    }
}