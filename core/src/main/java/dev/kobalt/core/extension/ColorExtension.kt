package dev.kobalt.core.extension

import android.graphics.drawable.ColorDrawable
import kotlin.math.roundToInt

fun Color.withAlpha(value: Float): Color = android.graphics.Color.argb(
    (value * 255).roundToInt(),
    android.graphics.Color.red(this),
    android.graphics.Color.green(this),
    android.graphics.Color.blue(this)
)

fun Color.toImage(): ColorImage = ColorDrawable(this)
