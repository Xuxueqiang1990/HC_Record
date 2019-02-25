package com.myrecord.hc_record.activity

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.support.v4.app.ActivityCompat
import android.support.v7.widget.LinearLayoutManager
import android.view.View
import android.widget.Toast
import com.myrecord.hc_record.R
import com.myrecord.hc_record.activity.adapter.RecordingItemAdapter
import com.myrecord.hc_record.bean.RecordingItem
import com.myrecord.hc_record.broadcast.BaseBroadCast
import com.myrecord.hc_record.broadcast.BroadCastConstants
import com.myrecord.hc_record.dialog.PlayingDialogFragment
import com.myrecord.hc_record.dialog.RecordingDialogFragment
import com.myrecord.hc_record.util.FileUtil
import kotlinx.android.synthetic.main.activity_record.*


class RecordActivity : BaseActivity() ,View.OnClickListener , BaseBroadCast.ReceiveBoard{

    private var mRecrodingItems = ArrayList<RecordingItem>()

    private lateinit var mAdapter : RecordingItemAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        registerMyReceiver(BroadCastConstants.RECORDING_OVER)
        setContentView(R.layout.activity_record)
        initView()
        checkPermission()
    }

    private fun getAllFiles(){
        var datas = FileUtil.getMp4FileList(FileUtil.mBaseFileBath)
        if (datas != null){
            mRecrodingItems.addAll(datas)
            mAdapter.notifyDataSetChanged()
        }
    }

    private fun initView(){
        btn_record.setOnClickListener(this)
        setupContentView()
    }

    private fun setupContentView() {
        val linearLayoutManager = LinearLayoutManager(this)
        linearLayoutManager.orientation = LinearLayoutManager.VERTICAL
        linearLayoutManager.isSmoothScrollbarEnabled = true
        rv_content.setHasFixedSize(true)
        rv_content.layoutManager = linearLayoutManager
        mAdapter = RecordingItemAdapter(mRecrodingItems)
        mAdapter!!.setOnItemClickListener(object : RecordingItemAdapter.OnRecordingItemClickListener {
            override fun onPlayer(position: Int) {
                playing(mRecrodingItems.get(position))
            }

            override fun onDelete(position: Int) {
                FileUtil.delete(mRecrodingItems.get(position).path)
                mAdapter.notifyItemRemoved(position)
                mRecrodingItems.removeAt(position)
            }
        })
        rv_content.adapter = mAdapter
    }

    override fun onClick(v: View) {
        when(v.id) {
            R.id.btn_record -> record()
        }
    }

    fun record(){
        val dialog = RecordingDialogFragment()
        dialog.show(supportFragmentManager, "RecordingDialogFragment")
    }
    
    fun playing(item:RecordingItem){
        val dialog = PlayingDialogFragment()
        var data = Bundle()
        data.putParcelable(PlayingDialogFragment.KEY_RECORD_ITEM , item)
        dialog.arguments = data
        dialog.show(supportFragmentManager, "PlayingDialogFragment")
    }

    private fun createDirPath() {
        FileUtil.createDir(this)
        tv_path.text = String.format(getString(R.string.dir_path) , FileUtil.mBaseFileBath)
    }


    fun checkPermission() {
        if (Build.VERSION.SDK_INT >= 23) {
            val permissions = ArrayList<String>()
            if (ActivityCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_DENIED) {
                permissions.add(Manifest.permission.READ_EXTERNAL_STORAGE)
            }
            if (ActivityCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_DENIED) {
                permissions.add(Manifest.permission.WRITE_EXTERNAL_STORAGE)
            }
            if (ActivityCompat.checkSelfPermission(this, Manifest.permission.RECORD_AUDIO) == PackageManager.PERMISSION_DENIED) {
                permissions.add(Manifest.permission.RECORD_AUDIO)
            }
            if (permissions.size > 0) {
                val permissionStr = arrayOfNulls<String>(permissions.size)
                for (i in permissions.indices) {
                    permissionStr[i] = permissions[i]
                }
                requestPermissions(permissionStr, 100)
            } else {
                createDirPath()
                getAllFiles()
            }
        }
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<String>, grantResults: IntArray) {
        when (requestCode) {
            100 -> {
                if (grantResults.size > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    createDirPath()
                    getAllFiles()
                    Toast.makeText(this, getString(R.string.permission_success), Toast.LENGTH_SHORT).show()
                } else {
                    Toast.makeText(this, getString(R.string.permission_fail), Toast.LENGTH_SHORT).show()
                }
                return
            }
        }
    }

    override fun onReceive(intent: Intent) {
        if (intent.getAction() == BroadCastConstants.RECORDING_OVER){
            var filename = intent.getStringExtra(BroadCastConstants.KEY_FILE_NAME)
            var filepath = intent.getStringExtra(BroadCastConstants.KEY_FILE_PATH)
            mRecrodingItems.add(0 , RecordingItem(filename , filepath))
            mAdapter.notifyItemInserted(0)
        }
        super.onReceive(intent)
    }

}
