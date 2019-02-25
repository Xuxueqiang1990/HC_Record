package com.myrecord.hc_record.activity

import android.content.Intent
import android.content.IntentFilter
import android.os.Bundle
import android.os.PersistableBundle
import android.support.v4.app.FragmentActivity

import com.myrecord.hc_record.RecordApplication
import com.myrecord.hc_record.broadcast.BaseBroadCast

/**
 * @author xuxueqiang_cd@keruyun.com
 * @date 2018/12/12
 */
open class BaseActivity : FragmentActivity(), BaseBroadCast.ReceiveBoard {

    private var mBroadCast: BaseBroadCast? = null

    private var mFilter: IntentFilter? = null

    override fun onCreate(savedInstanceState: Bundle?, persistentState: PersistableBundle?) {
        super.onCreate(savedInstanceState, persistentState)
    }

    protected fun registerMyReceiver(vararg actions: String) {
        if (mBroadCast == null) {
            mBroadCast = BaseBroadCast(this)
        }
        if (mFilter == null) {
            mFilter = IntentFilter()
        }
        // 循环添加
        for (action in actions) {
            mFilter!!.addAction(action)
        }
        RecordApplication.mLocalBroadcastManager.registerReceiver(mBroadCast, mFilter!!)
    }

    protected fun unregisterMyReceiver() {
        if (mBroadCast != null) {
            RecordApplication.mLocalBroadcastManager.unregisterReceiver(mBroadCast)
        }
        if (mFilter != null) {
            mFilter = null
        }
    }

    override fun onReceive(intent: Intent) {

    }

    override fun onDestroy() {
        unregisterMyReceiver()
        super.onDestroy()
    }
}
