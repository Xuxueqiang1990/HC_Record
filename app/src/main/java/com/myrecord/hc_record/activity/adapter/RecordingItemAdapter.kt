package com.myrecord.hc_record.activity.adapter

import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import com.myrecord.hc_record.R
import com.myrecord.hc_record.bean.RecordingItem
import kotlinx.android.synthetic.main.item_recording.view.*

/**
 * @author xuxueqiang_cd@keruyun.com
 * @date 2018/12/12
 */
class RecordingItemAdapter(val mData: MutableList<RecordingItem>) : RecyclerView.Adapter<RecordingItemAdapter.ViewHolder>() {

    private lateinit var mListener: OnRecordingItemClickListener

    interface OnRecordingItemClickListener {
        fun onPlayer(position:Int)
        fun onDelete(position:Int)
    }

    fun setOnItemClickListener(listener: OnRecordingItemClickListener) {
        this.mListener = listener
    }

    override fun onCreateViewHolder(viewGroup: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(viewGroup.context).inflate(R.layout.item_recording, null)
        val LayoutParams = FrameLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT)
        view.layoutParams = LayoutParams
        return ViewHolder(view)
    }

    override fun onBindViewHolder(viewHolder: ViewHolder, position: Int) {
        viewHolder.bind(position)
    }

    override fun getItemCount(): Int = mData?.size

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        fun bind(position: Int) = with(itemView) {
            var item = this@RecordingItemAdapter.mData.get(position)
            this.tv_name.text = item.name
            this.btn_play.setOnClickListener(object : View.OnClickListener {
                override fun onClick(v: View?) {
                    mListener?.onPlayer(position)
                }
            })
            this.btn_delete.setOnClickListener(object : View.OnClickListener {
                override fun onClick(v: View?) {
                    mListener?.onDelete(position)
                }
            })
        }
    }
}