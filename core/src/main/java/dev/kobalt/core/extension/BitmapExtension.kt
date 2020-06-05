package dev.kobalt.core.extension

import android.graphics.drawable.BitmapDrawable
import android.os.Build
import dev.kobalt.core.application.NativeApplication

fun Bitmap.toImage(): BitmapImage = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.DONUT) {
    BitmapDrawable(NativeApplication.instance.native.resources, this)
} else {
    @Suppress("DEPRECATION") BitmapDrawable(this)
}

