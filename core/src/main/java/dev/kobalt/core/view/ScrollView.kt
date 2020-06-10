@file:Suppress("unused", "MemberVisibilityCanBePrivate")

package dev.kobalt.core.view

import android.annotation.SuppressLint
import android.widget.FrameLayout
import android.widget.ScrollView
import dev.kobalt.core.application.NativeApplication
import dev.kobalt.core.application.NativeView

open class ScrollView : NativeView() {

    override val nativeView = NativeScrollView()

    val children = mutableListOf<NativeView>()

    fun add(
        view: NativeView,
        width: Int = wrapContent,
        height: Int = wrapContent,
        margin: Int = 0,
        padding: Int = 0
    ) {
        FrameLayout.LayoutParams(width, height).let {
            it.setMargins(margin, margin, margin, margin)
            view.nativeView.setPadding(padding, padding, padding, padding)
            nativeView.addView(view.nativeView, it)
            children.add(view)
        }
    }

    fun remove(view: NativeView) {
        children.remove(view)
        nativeView.removeView(view.nativeView)
    }

    @SuppressLint("ViewConstructor")
    class NativeScrollView : ScrollView(NativeApplication.instance.native)
}
