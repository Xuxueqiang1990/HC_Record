package com.myrecord.hc_record

import android.app.Application
import android.support.v4.content.LocalBroadcastManager

/**
 * @author xuxueqiang_cd@keruyun.com
 * @date 2018/12/12
 */
class RecordApplication : Application() {

    override fun onCreate() {
        super.onCreate()
        mLocalBroadcastManager = LocalBroadcastManager.getInstance(applicationContext)
    }

    companion object {

        lateinit var mLocalBroadcastManager: LocalBroadcastManager
    }
}
