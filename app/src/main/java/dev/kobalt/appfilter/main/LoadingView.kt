package dev.kobalt.appfilter.main

import dev.kobalt.appfilter.extension.refreshIcon
import dev.kobalt.core.extension.dp
import dev.kobalt.core.extension.toImage
import dev.kobalt.core.view.ImageView
import dev.kobalt.core.view.LayerView

class LoadingView : LayerView() {

    init {
        background = colors.gray.toImage()
        add(ImageView().apply {
            image = images.refreshIcon(colors.white)
        }, width = 24.dp, height = 24.dp, gravity = centerGravity)
    }

}