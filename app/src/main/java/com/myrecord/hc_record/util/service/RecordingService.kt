package com.myrecord.hc_record.util.service

import android.app.Service
import android.content.Intent
import android.media.MediaRecorder
import android.os.IBinder
import android.util.Log
import com.myrecord.hc_record.RecordApplication
import com.myrecord.hc_record.broadcast.BroadCastConstants
import com.myrecord.hc_record.util.FileUtil
import java.io.File
import java.io.IOException
import java.text.SimpleDateFormat
import java.util.*

/**
 * @author xuxueqiang_cd@keruyun.com
 * @date 2018/12/12
 */
class RecordingService : Service() {

    private var mRecorder: MediaRecorder? = null

    private var mFilePath = ""

    private var mFileName = ""

    override fun onBind(intent: Intent): IBinder? {
        return null
    }

    override fun onCreate() {
        super.onCreate()
    }

    override fun onStartCommand(intent: Intent, flags: Int, startId: Int): Int {
        startRecording()
        return Service.START_STICKY
    }

    override fun onDestroy() {
        if (mRecorder != null) {
            stopRecording()
        }
        super.onDestroy()
    }

    fun startRecording() {
        mFileName = formartTime(System.currentTimeMillis()) + ".mp4"
        FileUtil.createFile(FileUtil.mBaseFileBath , mFileName)
        mFilePath = FileUtil.mBaseFileBath + File.separator + mFileName
        mRecorder = MediaRecorder()
        mRecorder!!.setAudioSource(MediaRecorder.AudioSource.MIC)
        mRecorder!!.setOutputFormat(MediaRecorder.OutputFormat.MPEG_4)
        mRecorder!!.setOutputFile(mFilePath)
        mRecorder!!.setAudioEncoder(MediaRecorder.AudioEncoder.AAC)
        mRecorder!!.setAudioChannels(1)
        mRecorder!!.setAudioSamplingRate(44100)
        mRecorder!!.setAudioEncodingBitRate(192000)
        try {
            mRecorder!!.prepare()
            mRecorder!!.start()
        } catch (e: IOException) {
            Log.e(LOG_TAG, "prepare() failed")
        }
    }

    fun stopRecording() {
        mRecorder!!.stop()
        mRecorder!!.release()
        RecordApplication.mLocalBroadcastManager.sendBroadcast(getResultIntent())
        mRecorder = null
    }

    fun getResultIntent():Intent{
        var intent = Intent(BroadCastConstants.RECORDING_OVER)
        intent.putExtra(BroadCastConstants.KEY_FILE_NAME , mFileName)
        intent.putExtra(BroadCastConstants.KEY_FILE_PATH , mFilePath)
        return intent
    }

    fun formartTime(time:Long):String{
        val format = SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault())
        return format.format(time)
    }

    companion object {
        private val LOG_TAG = "RecordingService"
    }

}
