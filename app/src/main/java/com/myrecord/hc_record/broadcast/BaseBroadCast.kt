package com.myrecord.hc_record.broadcast

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent

/**
 * @author xuxueqiang_cd@keruyun.com
 * @date 2018/12/12
 */
class BaseBroadCast(private val mReceiveBoard: ReceiveBoard) : BroadcastReceiver() {

    override fun onReceive(context: Context, intent: Intent) {
        mReceiveBoard.onReceive(intent)
    }

    interface ReceiveBoard {
        fun onReceive(intent: Intent)
    }
}
