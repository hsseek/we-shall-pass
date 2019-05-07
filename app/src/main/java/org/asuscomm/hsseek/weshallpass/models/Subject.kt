package org.asuscomm.hsseek.weshallpass.models

import android.os.Parcel
import android.os.Parcelable

/**
 * @param duration The duration of the subject in minute
*/
class Subject(val title: String, val duration: Int) : Parcelable {

    constructor(parcel: Parcel) : this(parcel.readString() ?: "", parcel.readInt()) {
        toCount = parcel.readByte() != 0.toByte()
    }

    var isIncluded = true
    var toCount = true

    override fun writeToParcel(dest: Parcel?, flags: Int) {
        dest?.apply {
            writeString(title)
            writeInt(duration)
            dest.writeByte((if (toCount) 1 else 0).toByte())
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
