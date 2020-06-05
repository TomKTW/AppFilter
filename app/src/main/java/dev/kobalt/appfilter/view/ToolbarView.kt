package dev.kobalt.appfilter.view

import dev.kobalt.appfilter.extension.*
import dev.kobalt.core.extension.dp
import dev.kobalt.core.extension.sp
import dev.kobalt.core.extension.toImage
import dev.kobalt.core.extension.withAlpha
import dev.kobalt.core.view.ImageView
import dev.kobalt.core.view.LabelView
import dev.kobalt.core.view.StackView

class ToolbarView : StackView(orientation = Orientation.HORIZONTAL) {

    val titleLabel = LabelView().apply {
        text = strings.applicationName
        color = colors.white
        bold = true
        size = 18.sp
        gravity = centerVerticalGravity
    }

    val backButton = ImageView().apply {
        background = images.tapState(colors.black.withAlpha(0.5f), 4.dp)
        image = images.backIcon(colors.white)
    }

    val filterButton = ImageView().apply {
        background = images.tapState(colors.black.withAlpha(0.5f), 4.dp)
        image = images.filterIcon(colors.white)
    }

    val launchButton = ImageView().apply {
        background = images.tapState(colors.black.withAlpha(0.5f), 4.dp)
        image = images.launchIcon(colors.white)
    }

    init {
        background = colors.green.toImage()
        add(backButton, width = 56.dp, height = matchParent, padding = 16.dp)
        add(
            titleLabel,
            width = matchConstraint,
            height = matchParent,
            margin = 16.dp,
            weight = 1f
        )
        add(launchButton, width = 56.dp, height = matchParent, padding = 16.dp)
        add(filterButton, width = 56.dp, height = matchParent, padding = 16.dp)
    }

}