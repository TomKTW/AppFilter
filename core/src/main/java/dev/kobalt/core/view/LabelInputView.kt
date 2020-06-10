@file:Suppress("unused", "MemberVisibilityCanBePrivate")

package dev.kobalt.core.view

import android.annotation.SuppressLint
import android.content.res.Resources
import android.graphics.PorterDuff
import android.graphics.Typeface
import android.graphics.drawable.Drawable
import android.os.Build
import android.util.TypedValue
import android.view.inputmethod.EditorInfo
import android.widget.EditText
import android.widget.TextView
import dev.kobalt.core.application.NativeApplication
import dev.kobalt.core.application.NativeView
import dev.kobalt.core.extension.Color
import dev.kobalt.core.extension.Font
import dev.kobalt.core.extension.Gravity
import dev.kobalt.core.extension.sp
import java.lang.reflect.Field

open class LabelInputView : NativeView() {

    final override val nativeView = NativeLabelInputView()

    var text: CharSequence
        get() = nativeView.text
        set(value) = nativeView.setText(value)

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
        setCursorDrawableColor(colors.white)
        setPadding(0, 0, 0, 0)

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.CUPCAKE) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
                nativeView.imeOptions =
                    EditorInfo.IME_FLAG_NO_FULLSCREEN or EditorInfo.IME_FLAG_NO_EXTRACT_UI
            } else {
                nativeView.imeOptions = EditorInfo.IME_FLAG_NO_EXTRACT_UI
            }
        }
    }

    @Suppress("DEPRECATION")
    private fun setCursorDrawableColor(color: Int) {
        val editText = nativeView
        try {
            val cursorDrawableResField: Field =
                TextView::class.java.getDeclaredField("mCursorDrawableRes")
            cursorDrawableResField.isAccessible = true
            val cursorDrawableRes: Int = cursorDrawableResField.getInt(editText)
            val editorField: Field = TextView::class.java.getDeclaredField("mEditor")
            editorField.isAccessible = true
            val editor: Any = editorField.get(editText)!!
            val clazz: Class<*> = editor.javaClass
            val res: Resources = editText.context.resources
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
                val drawableForCursorField: Field = clazz.getDeclaredField("mDrawableForCursor")
                drawableForCursorField.isAccessible = true
                val drawable: Drawable = res.getDrawable(cursorDrawableRes)
                drawable.setColorFilter(color, PorterDuff.Mode.SRC_IN)
                drawableForCursorField.set(editor, drawable)
            } else {
                val cursorDrawableField: Field = clazz.getDeclaredField("mCursorDrawable")
                cursorDrawableField.isAccessible = true
                val drawables = arrayOfNulls<Drawable>(2)
                drawables[0] = res.getDrawable(cursorDrawableRes)
                drawables[1] = res.getDrawable(cursorDrawableRes)
                drawables[0]!!.setColorFilter(color, PorterDuff.Mode.SRC_IN)
                drawables[1]!!.setColorFilter(color, PorterDuff.Mode.SRC_IN)
                cursorDrawableField.set(editor, drawables)
            }
        } catch (t: Throwable) {
            t.printStackTrace()
        }
    }

    @SuppressLint("ViewConstructor")
    open class NativeLabelInputView : EditText(NativeApplication.instance.native) {

        final override fun setPadding(left: Int, top: Int, right: Int, bottom: Int) {
            super.setPadding(left, top, right, bottom)
        }

    }
}
