package com.fabinpaul.currencyconverter.log

import android.util.Log
import com.fabinpaul.currencyconverter.BuildConfig

/**
 * Wrapper for Android Log
 */
object L {

    fun <T> d(clazz: Class<T>, msg: String) {
        if (BuildConfig.DEBUG) {
            Log.d(clazz.simpleName, msg)
        }
    }

    fun d(tag: String, msg: String) {
        if (BuildConfig.DEBUG) {
            Log.d(tag, msg)
        }
    }

    fun e(tag: String, msg: String) {
        if (BuildConfig.DEBUG) {
            Log.e(tag, msg)
        }
    }

    fun <T> e(clazz: Class<T>, msg: String) {
        if (BuildConfig.DEBUG) {
            Log.e(clazz.simpleName, msg)
        }
    }

    fun e(tag: String, msg: String, throwable: Throwable) {
        if (BuildConfig.DEBUG) {
            Log.e(tag, msg, throwable)
        }
    }

    fun <T> e(clazz: Class<T>, msg: String, throwable: Throwable?) {
        if (BuildConfig.DEBUG) {
            Log.e(clazz.simpleName, msg, throwable)
        }
    }
}