package dev.kobalt.appfilter.view

import dev.kobalt.appfilter.entity.RemoteEntity
import dev.kobalt.appfilter.extension.refreshIcon
import dev.kobalt.core.application.NativeView
import dev.kobalt.core.extension.dp
import dev.kobalt.core.extension.withAlpha
import dev.kobalt.core.resources.Colors
import dev.kobalt.core.resources.Images
import dev.kobalt.core.view.*
import java.util.*

class EntryRecyclerView : RecyclerView() {

    var onItemTap: ((RemoteEntity.Entry) -> Unit)?
        get() = adapter.onItemTap
        set(value) {
            adapter.onItemTap = value
        }

    private var adapter: Adapter
        get() = nativeView.adapter as Adapter
        set(value) {
            nativeView.adapter = value
        }

    init {
        adapter = Adapter()
    }

    fun update(list: List<RemoteEntity.Entry>?) {
        adapter.list = list
        adapter.notifyDataSetChanged()
    }

    class Adapter : RecyclerView.Adapter<Holder>() {

        var onItemTap: ((RemoteEntity.Entry) -> Unit)? = null

        var list: List<RemoteEntity.Entry>? = null

        private fun getItem(holder: Holder): RemoteEntity.Entry? {
            return list?.getOrNull(holder.position)
        }

        override fun onCreateViewHolder(position: Int): Holder {
            return ItemHolder().apply {
                view.onTap = {
                    getItem(this)?.let {
                        onItemTap?.invoke(it)
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
                    getItem(holder).let {
                        view.titleLabel.text = it?.name.orEmpty()
                        view.developerLabel.text = it?.developer.orEmpty()
                        view.categoryLabel.text = it?.category.orEmpty()
                        view.contentRatingLabel.text = it?.contentRating.orEmpty()
                        val ratingValue = String.format(
                            Locale.ENGLISH,
                            "%,d",
                            it?.ratingTotal?.toIntOrNull() ?: 0
                        )
                        view.ratingLabel.text =
                            "Rating: ${it?.ratingAverage}, $ratingValue reviews"
                        view.statsLabel.text = "${(if (it?.ads.equals(
                                "yes",
                                ignoreCase = true
                            )
                        ) "Contains ads" else "No ads")}, ${(if (it?.iap.equals(
                                "yes",
                                ignoreCase = true
                            )
                        ) "Contains IAP" else "No IAP")}"
                        view.thumbnailImage.load(
                            it?.image,
                            placeholder = Images.refreshIcon(Colors.white)
                        )
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

        val titleLabel = LabelView()
        val developerLabel = LabelView()
        val categoryLabel = LabelView()
        val contentRatingLabel = LabelView()
        val ratingLabel = LabelView()
        val statsLabel = LabelView()
        val thumbnailImage = ImageView()

        init {
            background = images.tapState(colors.black.withAlpha(0.5f), 0)
            add(
                StackView(StackView.Orientation.HORIZONTAL)
                    .apply {
                        add(
                            thumbnailImage,
                            width = 48.dp,
                            height = 48.dp,
                            margin = 8.dp,
                            gravity = topGravity
                        )
                        add(
                            StackView(StackView.Orientation.VERTICAL)
                                .apply {
                                    add(titleLabel, width = matchParent, height = wrapContent)
                                    add(developerLabel, width = matchParent, height = wrapContent)
                                    add(categoryLabel, width = matchParent, height = wrapContent)
                                    add(
                                        contentRatingLabel,
                                        width = matchParent,
                                        height = wrapContent
                                    )
                                    add(ratingLabel, width = matchParent, height = wrapContent)
                                    add(statsLabel, width = matchParent, height = wrapContent)
                                },
                            width = matchConstraint,
                            height = wrapContent,
                            weight = 1f,
                            margin = 8.dp
                        )
                    }, width = matchParent, height = wrapContent
            )
        }
    }
}