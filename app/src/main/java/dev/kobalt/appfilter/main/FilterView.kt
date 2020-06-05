package dev.kobalt.appfilter.main

import dev.kobalt.appfilter.extension.filterSubmit
import dev.kobalt.appfilter.view.FilterLabelInputView
import dev.kobalt.appfilter.view.FilterOptionInputView
import dev.kobalt.core.extension.dp
import dev.kobalt.core.extension.safeLet
import dev.kobalt.core.extension.toImage
import dev.kobalt.core.view.ButtonView
import dev.kobalt.core.view.ScrollView
import dev.kobalt.core.view.StackView

class FilterView : StackView(Orientation.VERTICAL) {

    val optionStack = StackView(Orientation.VERTICAL)

    val submitButton = ButtonView().apply {
        text = strings.filterSubmit
    }

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
        orientation = Orientation.VERTICAL
        add(ScrollView().apply {
            add(optionStack, width = matchParent, height = wrapContent)
        }, width = matchParent, height = matchConstraint, weight = 1f)
        add(submitButton, width = matchParent, height = 56.dp)
    }

}