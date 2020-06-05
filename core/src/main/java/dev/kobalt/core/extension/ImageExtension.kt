package dev.kobalt.core.extension

import android.util.StateSet

fun StateImage.addPressedState(image: Image?) =
    addState(intArrayOf(android.R.attr.state_pressed), image)

fun StateImage.addNormalState(image: Image?) = addState(StateSet.WILD_CARD, image)
