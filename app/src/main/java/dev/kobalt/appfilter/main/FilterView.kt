package dev.kobalt.appfilter.main

import dev.kobalt.appfilter.view.FilterLabelInputView
import dev.kobalt.appfilter.view.FilterOptionInputView
import dev.kobalt.core.extension.dp
import dev.kobalt.core.extension.safeLet
import dev.kobalt.core.extension.toImage
import dev.kobalt.core.view.ScrollView
import dev.kobalt.core.view.StackView

class FilterView : StackView(Orientation.Vertical) {

    val optionStack = StackView(Orientation.Vertical)

    val values
        get() = optionStack.children.mapNotNull { view ->
            when (view) {
                is FilterLabelInputView -> safeLet(
                    view.id, view.inputValue.toString()
                ) { id, value -> id to value }
                is FilterOptionInputView -> safeLet(
                    view.id, view.selectedValue?.first
                ) { id, value -> id to value }
                else -> null
            }
        }.toMap()

    init {
        background = colors.gray.toImage()
        add(ScrollView(ScrollView.Orientation.Vertical).apply {
            add(optionStack, width = matchParent, height = wrapContent, margin = 4.dp)
        }, width = matchParent, height = matchConstraint, weight = 1f)
    }

}