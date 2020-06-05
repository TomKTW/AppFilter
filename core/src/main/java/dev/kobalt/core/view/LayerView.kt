package dev.kobalt.core.view

import android.annotation.SuppressLint
import android.widget.FrameLayout
import dev.kobalt.core.application.NativeApplication
import dev.kobalt.core.application.NativeView
import dev.kobalt.core.extension.Gravity

open class LayerView : NativeView() {

    override val nativeView = NativeLayerView()

    val children get() = 0.rangeTo(nativeView.childCount).map { nativeView.getChildAt(it) }

    fun add(
        view: NativeView,
        width: Int = wrapContent,
        height: Int = wrapContent,
        margin: Int = 0,
        padding: Int = 0,
        gravity: Gravity = centerGravity
    ) {
        FrameLayout.LayoutParams(width, height, gravity).let {
            it.setMargins(margin, margin, margin, margin)
            view.nativeView.setPadding(padding, padding, padding, padding)
            nativeView.addView(view.nativeView, it)
        }
    }

    fun remove(view: NativeView) {
        nativeView.removeView(view.nativeView)
    }

    @SuppressLint("ViewConstructor")
    class NativeLayerView : FrameLayout(NativeApplication.instance.native)
}
