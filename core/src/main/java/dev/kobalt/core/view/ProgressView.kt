package dev.kobalt.core.view

import android.annotation.SuppressLint
import android.view.View
import android.widget.ProgressBar
import dev.kobalt.core.application.NativeApplication
import dev.kobalt.core.application.NativeView

open class ProgressView : NativeView() {

    override val nativeView: View = NativeProgressView()

    @SuppressLint("ViewConstructor")
    class NativeProgressView : ProgressBar(NativeApplication.instance.native)

}
