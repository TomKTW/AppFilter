package dev.kobalt.core.view

import android.annotation.SuppressLint
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.ListView
import dev.kobalt.core.application.NativeApplication
import dev.kobalt.core.application.NativeView
import dev.kobalt.core.extension.dp
import dev.kobalt.core.extension.toImage
import dev.kobalt.core.extension.withAlpha
import dev.kobalt.core.utility.AdapterUtil

open class RecyclerView : NativeView() {

    final override val nativeView = NativeRecyclerView()

    init {
        nativeView.apply {
            isHorizontalFadingEdgeEnabled = false
            isVerticalFadingEdgeEnabled = false
            setFadingEdgeLength(0)
            divider = colors.white.withAlpha(0.5f).toImage()
            dividerHeight = 1.dp
        }
    }

    @SuppressLint("ViewConstructor")
    open class NativeRecyclerView : ListView(NativeApplication.instance.native) {

        private var scrollIndex: Int = -1
        private var scrollTop: Int = 0

        private fun saveScrollPosition() {
            scrollIndex = firstVisiblePosition
            scrollTop = getChildAt(0)?.let { it.top - paddingTop } ?: 0
        }

        private fun loadScrollPosition() {
            post { setSelectionFromTop(scrollIndex, scrollTop) }
        }

        override fun onDetachedFromWindow() {
            saveScrollPosition()
            super.onDetachedFromWindow()
        }

        override fun onAttachedToWindow() {
            super.onAttachedToWindow()
            loadScrollPosition()
        }

        override fun getPositionForView(view: View): Int {
            return AdapterUtil.getPositionForView(view, this)
        }

    }

    abstract class Adapter<T : Holder> : BaseAdapter() {

        abstract fun onCreateViewHolder(position: Int): T

        abstract fun onBindViewHolder(holder: T, position: Int)

        override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {
            val holder = convertView?.let {
                @Suppress("UNCHECKED_CAST")
                it.tag as T
            } ?: run {
                val holder = onCreateViewHolder(position)
                holder.parent = parent as? NativeRecyclerView
                holder.view.nativeView.tag = holder
                return@run holder
            }
            holder.internalPosition = position
            onBindViewHolder(holder, position)
            return holder.view.nativeView
        }

        override fun getItemId(p0: Int): Long {
            return p0.toLong()
        }

    }

    abstract class Holder(val view: NativeView) {

        var parent: NativeRecyclerView? = null

        var internalPosition: Int = -1

        val position: Int
            get() = internalPosition.takeIf { it != -1 }
                ?: parent?.getPositionForView(view.nativeView)
                ?: -1

    }

}
