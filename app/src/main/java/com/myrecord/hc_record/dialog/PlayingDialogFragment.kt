package com.myrecord.hc_record.dialog

import android.content.Context
import android.media.MediaPlayer
import android.os.Bundle
import android.os.Handler
import android.os.Message
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import com.myrecord.hc_record.R
import com.myrecord.hc_record.bean.RecordingItem
import com.myrecord.hc_record.util.Logger
import kotlinx.android.synthetic.main.fragment_playback.*
import java.io.File
import java.io.FileInputStream
import java.io.IOException
import java.util.concurrent.TimeUnit

/**
 * @author xuxueqiang_cd@keruyun.com
 * @date 2018/12/12
 */
class PlayingDialogFragment : BasicDialogFragment(), View.OnClickListener {

    private var mItem: RecordingItem? = null

    val mHandler = object : Handler() {
        override fun handleMessage(msg: Message) {
            super.handleMessage(msg)
            if (msg.what == 1) {
                tv_current_time.setText(String.format("%02d:%02d", msg.arg1, msg.arg2))
            }
        }
    }

    private var mMediaPlayer: MediaPlayer? = null

    private var mIsPlaying = false

    private lateinit var mParentView: View

    internal var mTotalTime: Long = 0

    private val mRunnable = object : Runnable {
        override fun run() {
            if (mMediaPlayer != null) {
                val mCurrentPosition = mMediaPlayer!!.getCurrentPosition()
                val minutes = TimeUnit.MILLISECONDS.toMinutes(mCurrentPosition.toLong())
                val seconds = TimeUnit.MILLISECONDS.toSeconds(mCurrentPosition.toLong()) - TimeUnit.MINUTES.toSeconds(minutes)
                var message = Message()
                message.arg1 = minutes.toInt()
                message.arg2 = seconds.toInt()
                message.what = 1
                mHandler.sendMessage(message)
                mHandler.postDelayed(this, 1000) // 循环实现定时器
            }
        }
    }

    override fun onAttach(context: Context?) {
        super.onAttach(context)
        mItem = arguments.getParcelable(KEY_RECORD_ITEM)
    }

    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        mParentView = View.inflate(activity, R.layout.fragment_playback, null)
        setupWindow()
        return mParentView
    }

    override fun onViewCreated(view: View?, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initView()
    }

    private fun initView() {
        btn_play.setOnClickListener(this)
        iv_close.setOnClickListener(this)
    }

    override fun onClick(v: View) {
        when (v.id) {
            R.id.btn_play -> playing(mIsPlaying)
            R.id.iv_close -> dismissAllowingStateLoss()
        }
    }

    override fun onPause() {
        super.onPause()
        if (mMediaPlayer != null) {
            stopPlaying()
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        if (mMediaPlayer != null) {
            stopPlaying()
            mItem = null
        }
    }

    private fun playing(isPlaying: Boolean) {
        if (!isPlaying) {
            mIsPlaying = true
            if (mMediaPlayer == null) {
                startPlaying()
            } else {
                resumePlaying()
            }
        } else {
            mIsPlaying = false
            pausePlaying()
        }
    }

    private fun startPlaying() {
        btn_play.text = getString(R.string.btn_pause)
        mMediaPlayer = MediaPlayer()
        try {
            val file = File(mItem!!.path)
            val inputStream = FileInputStream(file)
            mMediaPlayer!!.setDataSource(inputStream.fd)
            inputStream.close()
            mMediaPlayer!!.prepare()
            mTotalTime = mMediaPlayer!!.duration.toLong()
            mMediaPlayer!!.setOnPreparedListener {
                mMediaPlayer!!.start()
            }
        } catch (e: IOException) {
            Logger.e(LOG_TAG, "prepare() failed")
        }
        mMediaPlayer!!.setOnCompletionListener {
            stopPlaying()
        }
        updateTime()
        activity.window.addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON)
    }

    private fun pausePlaying() {
        btn_play.text = getString(R.string.btn_pause)
        mHandler!!.removeCallbacks(mRunnable)
        mMediaPlayer!!.pause()
    }

    private fun resumePlaying() {
        btn_play.text = getString(R.string.btn_playing)
        mHandler!!.removeCallbacks(mRunnable)
        mMediaPlayer!!.start()
        updateTime()
    }

    private fun stopPlaying() {
        btn_play.text = getString(R.string.btn_playing)
        mHandler!!.removeCallbacks(mRunnable)
        mMediaPlayer!!.stop()
        mMediaPlayer!!.reset()
        mMediaPlayer!!.release()
        mMediaPlayer = null
        mIsPlaying = false
        activity.window.clearFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON)
    }

    private fun updateTime() {
        mHandler!!.postDelayed(mRunnable, 1000)
    }

    companion object {
        private val LOG_TAG = "PlayingDialogFragment"
        val KEY_RECORD_ITEM = "recording_item"
    }

}
