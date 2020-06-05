package dev.kobalt.appfilter.main

import dev.kobalt.appfilter.entity.RemoteEntity
import dev.kobalt.appfilter.view.EntryRecyclerView
import dev.kobalt.core.extension.toImage
import dev.kobalt.core.view.LayerView

class ResultView : LayerView() {

    val entryRecycler = EntryRecyclerView()

    init {
        background = colors.gray.toImage()
        add(entryRecycler, width = matchParent, height = matchParent)
    }

    fun setList(list: List<RemoteEntity.Entry>?) {
        entryRecycler.update(list)
    }

}