package dev.kobalt.core.view

import android.annotation.SuppressLint
import android.widget.LinearLayout
import dev.kobalt.core.application.NativeApplication
import dev.kobalt.core.application.NativeView
import dev.kobalt.core.extension.Gravity
import java.lang.reflect.Field


open class StackView(orientation: Orientation) : NativeView() {

    override val nativeView = NativeStackView()

    val children = mutableListOf<NativeView>()

    var orientation: Orientation
        get() = when (nativeView.orientation) {
            LinearLayout.HORIZONTAL -> Orientation.HORIZONTAL
            LinearLayout.VERTICAL -> Orientation.VERTICAL
            else -> throw Exception("Invalid orientation state.")
        }
        set(value) {
            nativeView.orientation = when (value) {
                Orientation.HORIZONTAL -> LinearLayout.HORIZONTAL
                Orientation.VERTICAL -> LinearLayout.VERTICAL
            }
        }

    var gravity: Gravity
        get() {
            return try {
                val staticField: Field = LinearLayout::class.java.getDeclaredField("mGravity")
                staticField.isAccessible = true
                staticField.getInt(nativeView)
            } catch (e: Exception) {
                throw Exception("Inaccessible gravity value.")
            }
        }
        set(value) {
            nativeView.gravity = value
        }

    fun add(
        view: NativeView,
        width: Int = wrapContent,
        height: Int = wrapContent,
        margin: Int = 0,
        padding: Int = 0,
        weight: Float = 0f,
        gravity: Gravity = centerGravity
    ) {
        LinearLayout.LayoutParams(width, height, weight).let {
            it.setMargins(margin, margin, margin, margin)
            it.gravity = gravity
            view.nativeView.setPadding(padding, padding, padding, padding)
            nativeView.addView(view.nativeView, it)
            children.add(view)
        }
    }

    fun remove(view: NativeView) {
        children.remove(view)
        nativeView.removeView(view.nativeView)
    }

    init {
        this.orientation = orientation
    }

    enum class Orientation {
        HORIZONTAL, VERTICAL
    }

    @SuppressLint("ViewConstructor")
    open class NativeStackView : LinearLayout(NativeApplication.instance.native) {

        var onDetachedFromWindow: (() -> Unit)? = null

        override fun onDetachedFromWindow() {
            super.onDetachedFromWindow()
            onDetachedFromWindow?.invoke()
        }

    }

}