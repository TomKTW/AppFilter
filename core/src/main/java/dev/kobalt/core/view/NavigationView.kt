package dev.kobalt.core.view

import dev.kobalt.core.application.NativeView
import kotlin.properties.Delegates

class NavigationView : LayerView() {

    var onViewChange: ((NativeView) -> Unit)? = null

    var currentLayout: NativeView by Delegates.observable<NativeView>(BlankView()) { _, oldValue, newValue ->
        onViewChange?.invoke(newValue)
        remove(oldValue)
        add(view = newValue, width = matchParent, height = matchParent)
    }

}