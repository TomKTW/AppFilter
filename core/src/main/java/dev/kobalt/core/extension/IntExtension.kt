package dev.kobalt.core.extension

import android.util.TypedValue
import dev.kobalt.core.application.NativeApplication

private fun Int.toDimensionUnit(typedValue: Int): Float = TypedValue.applyDimension(
    typedValue, this.toFloat(), NativeApplication.instance.native.resources.displayMetrics
)

val Int.dp: Int get() = toDimensionUnit(TypedValue.COMPLEX_UNIT_DIP).toInt()

val Int.sp: Int get() = toDimensionUnit(TypedValue.COMPLEX_UNIT_SP).toInt()
