package dev.kobalt.core.resources

import android.graphics.drawable.InsetDrawable
import android.graphics.drawable.ShapeDrawable
import android.graphics.drawable.shapes.RectShape
import dev.kobalt.core.application.ApplicationBinder
import dev.kobalt.core.extension.*

object Images : ApplicationBinder {

    fun rectangle(color: Color, inset: Int): Image =
        InsetDrawable(ShapeDrawable(RectShape()).apply { paint.color = color }, inset)

    fun fromResources(id: Int, color: Color): Image? =
        application.getSvgImageFromResources(id, color)

    fun tapState(color: Color, inset: Int) = StateImage().apply {
        addNormalState(null)
        addPressedState(rectangle(color, inset))
    }

    fun layersOf(vararg image: Image): LayerImage = LayerImage(image)

}