package com.myrecord.hc_record.util

import android.text.TextUtils
import android.util.Log

object Logger {

    private val TAG = "record"

    fun d(msg: String) {
        Log.d(TAG, msg)
    }

    fun v(msg: String) {
        Log.v(TAG, msg)
    }

    fun w(msg: String) {
        Log.w(TAG, msg)
    }

    fun e(msg: String) {
        Log.e(TAG, msg)
    }

    fun i(msg: String) {
        Log.i(TAG, msg)
    }

    fun d(tag: String, msg: String) {
        if (TextUtils.isEmpty(tag)) {
            Log.d(TAG, msg)
        } else {
            Log.d(tag, msg)
        }
    }

    fun v(tag: String, msg: String) {
        if (TextUtils.isEmpty(tag)) {
            Log.v(TAG, msg)
        } else {
            Log.v(tag, msg)
        }
    }

    fun w(tag: String, msg: String) {
        if (TextUtils.isEmpty(tag)) {
            Log.w(TAG, msg)
        } else {
            Log.w(tag, msg)
        }
    }

    fun e(tag: String, msg: String) {
        if (TextUtils.isEmpty(tag)) {
            Log.e(TAG, msg)
        } else {
            Log.e(tag, msg)
        }
    }

    fun i(tag: String, msg: String) {
        if (TextUtils.isEmpty(tag)) {
            Log.i(TAG, msg)
        } else {
            Log.i(tag, msg)
        }
    }
}
