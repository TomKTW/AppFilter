package dev.kobalt.appfilter.view

import dev.kobalt.core.application.NativeView
import dev.kobalt.core.extension.dp
import dev.kobalt.core.utility.Popup
import dev.kobalt.core.view.LabelView
import dev.kobalt.core.view.LayerView
import dev.kobalt.core.view.RecyclerView
import dev.kobalt.core.view.StackView
import kotlin.properties.Delegates

class FilterOptionInputView : StackView(Orientation.Vertical) {

    var id: String? = null

    private val popup = Popup()

    private val label = LabelView()

    private val content = StringListRecyclerView().apply {
        onTapItem = {
            selectedValue = it
            inputValue = it.second
            popup.dismiss()
        }
    }

    private val input = LabelView().apply {
        background = images.layersOf(
            images.rectangle(colors.grayDark, 0.dp),
            images.tapState(colors.black, 0.dp)
        )
        onTap = {
            popup.display(
                content = content, anchor = this, width = matchParent, height = 128.dp
            )
        }
    }

    var labelText: CharSequence
        get() = label.text
        set(value) {
            label.text = value
        }

    var inputValues by Delegates.observable<List<Pair<String, String>>>(emptyList()) { _, _, newValue ->
        content.update(newValue)
    }

    var selectedValue by Delegates.observable<Pair<String, String>?>(null) { _, _, newValue ->
        inputValue = newValue?.second.orEmpty()
    }

    private var inputValue: CharSequence
        get() = input.text
        set(value) {
            input.text = value
        }

    init {
        nativeView.onDetachedFromWindow = { popup.dismiss() }
        gravity = centerGravity
        add(label, width = matchParent, height = wrapContent, margin = 4.dp)
        add(
            input,
            width = matchParent,
            height = wrapContent,
            margin = 4.dp,
            padding = 4.dp
        )
    }

    class StringListRecyclerView : RecyclerView() {

        private var adapter: Adapter
            get() = nativeView.adapter as Adapter
            set(value) {
                nativeView.adapter = value
            }

        var onTapItem: ((Pair<String, String>) -> Unit)?
            get() = adapter.onTapItem
            set(value) {
                adapter.onTapItem = value
            }

        init {
            nativeView.adapter = Adapter()
        }

        fun update(list: List<Pair<String, String>>?) {
            adapter.apply {
                this.list = list
                notifyDataSetChanged()
            }
        }

        class Adapter : RecyclerView.Adapter<Holder>() {

            var onTapItem: ((Pair<String, String>) -> Unit)? = null

            var list: List<Pair<String, String>>? = null

            private fun getItem(holder: Holder): Pair<String, String>? {
                return list?.getOrNull(holder.position)
            }

            override fun onCreateViewHolder(position: Int): Holder {
                return ItemHolder()
                    .apply {
                        view.onTap = {
                            getItem(this)?.let {
                                onTapItem?.invoke(it)
                            }
                        }
                    }
            }

            override fun getItem(p0: Int): Any? {
                return list?.getOrNull(p0)
            }

            override fun getCount(): Int {
                return list?.size ?: 0
            }

            override fun onBindViewHolder(holder: Holder, position: Int) {
                when (holder) {
                    is ItemHolder -> (holder.view as? ItemView)?.let { view ->
                        getItem(holder)?.let {
                            view.titleLabel.text = it.second
                        }
                    }
                }
            }

        }

        open class Holder(itemView: NativeView) : RecyclerView.Holder(itemView)

        class ItemHolder : Holder(
            ItemView()
        )

        class ItemView : LayerView() {

            val titleLabel = LabelView().apply {
                color = colors.white
            }

            init {
                background = images.layersOf(
                    images.rectangle(colors.grayDark, 0.dp),
                    images.tapState(colors.black, 0.dp)
                )
                add(StackView(Orientation.Vertical).apply {
                    add(titleLabel, width = matchParent, height = wrapContent, margin = 8.dp)
                }, width = matchParent, height = wrapContent)
            }

        }
    }
}