package com.myrecord.hc_record.dialog

import android.content.Intent
import android.content.IntentFilter
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.os.PersistableBundle
import android.support.v4.app.DialogFragment
import android.support.v4.app.FragmentManager
import android.view.*
import com.myrecord.hc_record.RecordApplication
import com.myrecord.hc_record.broadcast.BaseBroadCast
import com.myrecord.hc_record.util.DensityUtil

/**
 * @author xuxueqiang_cd@keruyun.com
 * @date 2018/12/12
 */
open class BasicDialogFragment : DialogFragment() , BaseBroadCast.ReceiveBoard{
    private var mBroadCast: BaseBroadCast? = null

    private var mFilter: IntentFilter? = null

    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return super.onCreateView(inflater, container, savedInstanceState)
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


    protected fun setupWindow() {
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialog.setCanceledOnTouchOutside(false)// 设置点击屏幕Dialog不消失
        val window = dialog.window
        if (window != null) {
            //设置宽高
            val attributes = window.attributes
            attributes.width = DensityUtil.dip2px(activity, 460f)
            attributes.height = WindowManager.LayoutParams.WRAP_CONTENT
            window.attributes = attributes
            window.clearFlags(WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE or WindowManager.LayoutParams.FLAG_ALT_FOCUSABLE_IM)
            window.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE or WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN)
            window.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        }
    }

    override fun show(manager: FragmentManager, tag: String) {
        if (manager != null && !manager.isDestroyed) {
            val ft = manager.beginTransaction()
            ft.add(this, tag)
            ft.commitAllowingStateLoss()
        }
    }

    override fun onDestroyView() {
        unregisterMyReceiver()
        super.onDestroyView()
    }

    override fun onReceive(intent: Intent) {

    }
}
