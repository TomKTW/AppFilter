@file:Suppress("unused", "MemberVisibilityCanBePrivate")

package dev.kobalt.core.view

import android.annotation.SuppressLint
import android.annotation.TargetApi
import android.os.Build
import android.widget.FrameLayout
import dev.kobalt.core.application.NativeApplication
import dev.kobalt.core.application.NativeView

open class ScrollView(orientation: Orientation) : NativeView() {

    override val nativeView = when (orientation) {
        Orientation.Horizontal -> NativeHorizontalScrollView()
        Orientation.Vertical -> NativeVerticalScrollView()
    }

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
    class NativeVerticalScrollView : android.widget.ScrollView(NativeApplication.instance.native)

    @TargetApi(Build.VERSION_CODES.CUPCAKE)
    @SuppressLint("ViewConstructor")
    class NativeHorizontalScrollView :
        android.widget.HorizontalScrollView(NativeApplication.instance.native)

    enum class Orientation {
        Horizontal, Vertical
    }
}
