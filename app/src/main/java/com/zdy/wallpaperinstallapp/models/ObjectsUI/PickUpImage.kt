package com.zdy.wallpaperinstallapp.models.ObjectsUI

import android.graphics.Bitmap
import android.os.Build
import android.os.Parcel
import android.os.Parcelable
import androidx.annotation.RequiresApi

data class PickUpImage(
    var bitmap: Bitmap?,
    val url: String,
    val description: String?,
    var isLiked: Boolean = false,
    val localID: Int? = null

) : Parcelable{
    constructor(parcel: Parcel) : this(
        parcel.readParcelableCompat<Bitmap>(),
        parcel.readString()!!,
        parcel.readString(),
        parcel.readInt() != 0
    )

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeParcelable(null, flags)
        parcel.writeString(url)
        parcel.writeString(description)
        parcel.writeInt(if(isLiked) 1 else 0)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<PickUpImage> {
        override fun createFromParcel(parcel: Parcel): PickUpImage {
            return PickUpImage(parcel)
        }

        override fun newArray(size: Int): Array<PickUpImage?> {
            return arrayOfNulls(size)
        }
    }
}

inline fun <reified T : Parcelable> Parcel.readParcelableCompat(): T? {
    return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
        readParcelable(T::class.java.classLoader, T::class.java)
    } else {
        @Suppress("DEPRECATION")
        readParcelable(T::class.java.classLoader)
    }
}