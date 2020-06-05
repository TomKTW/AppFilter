package dev.kobalt.core.view

import android.annotation.SuppressLint
import android.graphics.Typeface
import android.util.TypedValue
import android.widget.TextView
import dev.kobalt.core.application.NativeApplication
import dev.kobalt.core.application.NativeView
import dev.kobalt.core.extension.Color
import dev.kobalt.core.extension.Font
import dev.kobalt.core.extension.Gravity
import dev.kobalt.core.extension.sp

open class LabelView : NativeView() {

    override val nativeView = NativeLabelView()

    var text: CharSequence
        get() = nativeView.text
        set(value) {
            nativeView.text = value
        }

    var color: Color
        get() = nativeView.textColors.defaultColor
        set(value) {
            nativeView.setTextColor(value)
        }

    var font: Font
        get() = nativeView.typeface
        set(value) {
            nativeView.typeface = value
        }

    var bold: Boolean
        get() = font.isBold
        set(value) {
            val oldValue = nativeView.typeface
            nativeView.setTypeface(
                oldValue,
                if (value) (Typeface.BOLD.or(oldValue.style)) else oldValue.style
            )
        }

    var italic: Boolean
        get() = font.isItalic
        set(value) {
            val oldValue = nativeView.typeface
            nativeView.setTypeface(
                oldValue,
                if (value) (Typeface.ITALIC.or(oldValue.style)) else oldValue.style
            )
        }

    var size: Int
        get() = nativeView.textSize.toInt()
        set(value) {
            nativeView.setTextSize(TypedValue.COMPLEX_UNIT_PX, value.toFloat())
        }

    var gravity: Gravity
        get() = nativeView.gravity
        set(value) {
            nativeView.gravity = value
        }


    init {
        color = colors.white
        font = fonts.openSans
        size = 14.sp
        setPadding(0, 0, 0, 0)
    }

    @SuppressLint("ViewConstructor")
    class NativeLabelView : TextView(NativeApplication.instance.native)
}

