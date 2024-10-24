package com.zdy.wallpaperinstallapp.utils

import android.graphics.Bitmap
import java.util.Locale

fun String.getBitmapFormat() : Bitmap.CompressFormat?{
    return when {
        this.endsWith(".jpg", ignoreCase = true) || this.endsWith(".jpeg", ignoreCase = true) -> Bitmap.CompressFormat.JPEG
        this.endsWith(".png", ignoreCase = true) -> Bitmap.CompressFormat.PNG
        this.endsWith(".webp", ignoreCase = true) -> Bitmap.CompressFormat.WEBP
        else -> null
    }
}

// Расширение для класса String для определения формата изображения
fun String.getImageFormat(): String? {
    val regex = """\.(jpg|jpeg|png|gif|bmp|webp)$""".toRegex(RegexOption.IGNORE_CASE)
    val matchResult = regex.find(this)
    return matchResult?.value?.substring(1)?.lowercase(Locale.getDefault())
}