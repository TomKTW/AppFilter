@file:Suppress("unused", "MemberVisibilityCanBePrivate")

package dev.kobalt.core.resources

import dev.kobalt.core.application.ApplicationBinder
import dev.kobalt.core.extension.Font

object Fonts : ApplicationBinder {

    val openSans: Font get() = fromAsset("font_opensans.ttf")

    fun fromAsset(path: String): Font = application.getFontFromAssets(path)

}