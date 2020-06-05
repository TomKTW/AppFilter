package dev.kobalt.core.view

import android.view.View
import dev.kobalt.core.application.NativeView

class BlankView : NativeView() {
    override val nativeView: View
        get() = View(application.native)
}