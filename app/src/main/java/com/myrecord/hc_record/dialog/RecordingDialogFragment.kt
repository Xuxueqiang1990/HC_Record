package com.myrecord.hc_record.dialog

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.os.SystemClock
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import com.myrecord.hc_record.R
import com.myrecord.hc_record.broadcast.BroadCastConstants
import com.myrecord.hc_record.util.service.RecordingService
import kotlinx.android.synthetic.main.fragment_recording.*

/**
 * @author xuxueqiang_cd@keruyun.com
 * @date 2018/12/12
 */
class RecordingDialogFragment : BasicDialogFragment() , View.OnClickListener{

    private lateinit var mParentView: View

    private var mIsWaitRecord = true

    override fun onAttach(context: Context?) {
        registerMyReceiver(BroadCastConstants.RECORDING_OVER)
        super.onAttach(context)
    }

    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?, savedInstanceState: Bundle?): View {
        mParentView = View.inflate(activity, R.layout.fragment_recording, null)
        setupWindow()
        return mParentView
    }

    override fun onViewCreated(view: View?, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initView()
    }

    private fun initView(){
        iv_close.setOnClickListener(this)
        btn_start_record.setOnClickListener(this)
    }


    override fun onClick(v: View) {
        when(v.id) {
            R.id.iv_close -> close()
            R.id.btn_start_record -> onRecord()
        }
    }

    fun close(){
        var intent = Intent(activity , RecordingService::class.java)
        activity.stopService(intent)
        dismissAllowingStateLoss()
    }

    fun onRecord(){
        var intent = Intent(activity , RecordingService::class.java)
        if (mIsWaitRecord){
            btn_start_record.text = getString(R.string.btn_record_over)
            ct_time.setBase(SystemClock.elapsedRealtime())
            ct_time.start()
            activity.startService(intent)
            mIsWaitRecord = false
        } else {
            ct_time.stop()
            ct_time.setBase(SystemClock.elapsedRealtime());
            Toast.makeText(context , getString(R.string.record_save_success) , Toast.LENGTH_SHORT).show()
            activity.stopService(intent)
            btn_start_record.text = getString(R.string.btn_record_start)
            mIsWaitRecord = true
        }
    }

    override fun onReceive(intent: Intent) {
        if (intent.action.equals(BroadCastConstants.RECORDING_OVER)){
            mIsWaitRecord = true
        }
        super.onReceive(intent)
    }
}
