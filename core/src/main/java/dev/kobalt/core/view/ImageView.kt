package dev.kobalt.core.view

import android.annotation.SuppressLint
import android.graphics.drawable.Drawable
import android.widget.ImageView
import dev.kobalt.core.application.NativeApplication
import dev.kobalt.core.application.NativeView
import dev.kobalt.core.extension.Image

open class ImageView : NativeView() {

    override val nativeView = NativeImageView()

    var image: Drawable?
        get() = nativeView.drawable
        set(value) {
            nativeView.setImageDrawable(value)
        }

    fun load(url: String?, placeholder: Image? = null) {
        application.remoteImageLoader.load(url, nativeView, placeholder)
    }

    @SuppressLint("ViewConstructor")
    class NativeImageView : ImageView(NativeApplication.instance.native)

}