package dev.kobalt.appfilter.main

import dev.kobalt.core.extension.dp
import dev.kobalt.core.extension.toImage
import dev.kobalt.core.view.*

class DetailsView : LayerView() {

    val titleLabel = LabelView()
    val developerLabel = LabelView()
    val categoryLabel = LabelView()
    val contentRatingLabel = LabelView()
    val ratingLabel = LabelView()
    val statsLabel = LabelView()
    val thumbnailImage = ImageView()
    val detailsLabel = LabelView()

    private val headerStack = StackView(StackView.Orientation.HORIZONTAL)
        .apply {
            add(thumbnailImage, width = 48.dp, height = 48.dp, margin = 8.dp, gravity = topGravity)
            add(
                StackView(StackView.Orientation.VERTICAL).apply {
                    add(titleLabel, width = matchParent, height = wrapContent)
                    add(developerLabel, width = matchParent, height = wrapContent)
                    add(categoryLabel, width = matchParent, height = wrapContent)
                    add(contentRatingLabel, width = matchParent, height = wrapContent)
                    add(ratingLabel, width = matchParent, height = wrapContent)
                    add(statsLabel, width = matchParent, height = wrapContent)
                },
                width = matchConstraint, height = wrapContent, weight = 1f, margin = 8.dp
            )
        }

    private val contentStack = StackView(StackView.Orientation.VERTICAL)
        .apply {
            add(
                headerStack,
                width = matchParent,
                height = wrapContent
            )
            add(
                detailsLabel,
                width = matchParent,
                height = wrapContent,
                margin = 8.dp
            )
        }

    private val contentScroll = ScrollView().apply {
        add(contentStack, width = matchParent, height = wrapContent)
    }

    init {
        background = colors.gray.toImage()
        add(contentScroll, width = matchParent, height = matchParent)
    }
}