package com.zdy.wallpaperinstallapp.utils.extensions

import android.os.Build
import android.os.Parcel
import android.os.Parcelable

inline fun <reified T : Parcelable> Parcel.readParcelableCompat(): T? {
    return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
        readParcelable(T::class.java.classLoader, T::class.java)
    } else {
        @Suppress("DEPRECATION")
        readParcelable(T::class.java.classLoader)
    }
}