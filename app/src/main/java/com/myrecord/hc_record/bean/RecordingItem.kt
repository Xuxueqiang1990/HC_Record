package com.myrecord.hc_record.bean

import android.os.Parcel
import android.os.Parcelable

/**
 * @author xuxueqiang_cd@keruyun.com
 * @date 2018/12/12
 */
class RecordingItem(var name: String, var path: String) : Parcelable {
    constructor(parcel: Parcel) : this(
            parcel.readString(),
            parcel.readString()) {
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(name)
        parcel.writeString(path)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<RecordingItem> {
        override fun createFromParcel(parcel: Parcel): RecordingItem {
            return RecordingItem(parcel)
        }

        override fun newArray(size: Int): Array<RecordingItem?> {
            return arrayOfNulls(size)
        }
    }
}
