package org.asuscomm.hsseek.weshallpass.models

import android.os.Parcel
import android.os.Parcelable

class Subject(val title: String, val duration: Int) : Parcelable {

    constructor(parcel: Parcel) : this(parcel.readString() ?: "", parcel.readInt())

    var isIncluded = true

    override fun writeToParcel(dest: Parcel?, flags: Int) {
        dest?.apply {
            writeString(title)
            writeInt(duration)
        }
    }

    override fun describeContents(): Int {
        return 0
    }


    companion object {
        @JvmField
        val CREATOR = object: Parcelable.Creator<Subject> {
            override fun createFromParcel(source: Parcel): Subject = Subject(source)
            override fun newArray(size: Int): Array<Subject?> = arrayOfNulls(size)
        }
    }
}
