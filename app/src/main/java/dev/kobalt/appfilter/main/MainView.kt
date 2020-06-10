@file:Suppress("unused")

package dev.kobalt.appfilter.main

import dev.kobalt.appfilter.entity.RemoteEntity
import dev.kobalt.appfilter.extension.*
import dev.kobalt.appfilter.view.FilterLabelInputView
import dev.kobalt.appfilter.view.FilterOptionInputView
import dev.kobalt.appfilter.view.ToolbarView
import dev.kobalt.core.extension.digitalGroup
import dev.kobalt.core.extension.dp
import dev.kobalt.core.resources.Colors
import dev.kobalt.core.resources.Images
import dev.kobalt.core.utility.WeakReferenceCache
import dev.kobalt.core.view.NavigationView
import dev.kobalt.core.view.StackView

class MainView : StackView(orientation = Orientation.VERTICAL) {

    private var toolbar: ToolbarView = ToolbarView().apply {
        filterButton.onTap = {
            contentNavigation.currentLayout = filterLayout.value
        }
        backButton.onTap = {
            this@MainView.onBackPressed()
        }
        launchButton.onTap = {
            detailsEntry?.link?.let { openUrl(it) }
        }
    }

    private val contentNavigation = NavigationView().apply {
        onViewChange = {
            when (it) {
                is ResultView -> toolbar.apply {
                    titleLabel.text = strings.applicationName
                    filterButton.isVisible = true
                    backButton.isVisible = false
                    launchButton.isVisible = false
                }
                is LoadingView -> toolbar.apply {
                    titleLabel.text = strings.applicationName
                    filterButton.isVisible = false
                    backButton.isVisible = false
                    launchButton.isVisible = false
                }
                is FilterView -> toolbar.apply {
                    titleLabel.text = strings.filterOptions
                    filterButton.isVisible = false
                    backButton.isVisible = true
                    launchButton.isVisible = false
                }
                is DetailsView -> toolbar.apply {
                    titleLabel.text = strings.details
                    filterButton.isVisible = false
                    backButton.isVisible = true
                    launchButton.isVisible = true
                }
            }
        }
    }

    private var filterLayout = WeakReferenceCache {
        FilterView().apply {
            submitButton.onTap = {
                hideKeyboard()
                contentNavigation.currentLayout = loadingView.value
                application.jobManager.fetch(values) { apply(it) }
            }
            entity?.let { entity ->
                if (entity.filters.isNotEmpty()) {
                    optionStack.children.toList()
                        .forEach { optionStack.remove(it) }
                    entity.filters.forEach { filter ->
                        optionStack.add(
                            when (filter) {
                                is RemoteEntity.Filter.Input -> FilterLabelInputView().apply {
                                    id = filter.name
                                    labelText = filter.text
                                    inputValue = filter.value
                                }
                                is RemoteEntity.Filter.Select -> FilterOptionInputView().apply {
                                    id = filter.name
                                    labelText = filter.text
                                    inputValues = filter.values.toList()
                                    selectedValue = inputValues.find { it.first == filter.value }
                                        ?: inputValues.firstOrNull()
                                }
                            }, width = matchParent, height = wrapContent
                        )
                    }
                }
            }
        }
    }

    private val detailsLayout = WeakReferenceCache {
        DetailsView()
    }

    private val loadingView = WeakReferenceCache {
        LoadingView()
    }

    private fun getListLayout(): ResultView = ResultView().apply {
        setList(entity?.entries)
        entryRecycler.onItemTap = {
            detailsEntry = it
            contentNavigation.currentLayout = detailsLayout.value.apply {
                val rating = strings.detailsRating(it.ratingAverage)
                val reviewCount = it.ratingTotal.toIntOrNull() ?: 0
                val reviews = strings.detailsReviews(reviewCount.digitalGroup())
                val containsAds = it.ads.equals("yes", ignoreCase = true)
                val ads = if (containsAds) strings.detailsWithAds else strings.detailsWithoutAds
                val containsIap = it.iap.equals("yes", ignoreCase = true)
                val iap = if (containsIap) strings.detailsWithIap else strings.detailsWithoutIap
                val placeholder = Images.refreshIcon(Colors.white)
                titleLabel.text = it.name
                developerLabel.text = it.developer
                categoryLabel.text = it.category
                contentRatingLabel.text = it.contentRating
                ratingLabel.text = "$rating, $reviews"
                statsLabel.text = "$ads, $iap"
                thumbnailImage.load(it.image, placeholder)
                detailsLabel.text = it.description
            }
        }
    }

    private var detailsEntry: RemoteEntity.Entry? = null

    private var entity: RemoteEntity? = null

    private fun apply(entity: RemoteEntity) {
        this.entity = entity
        contentNavigation.currentLayout = getListLayout()
    }

    override fun onBackPressed() {
        when (contentNavigation.currentLayout) {
            is FilterView -> {
                hideKeyboard()
                contentNavigation.currentLayout = getListLayout()
            }
            is DetailsView -> {
                contentNavigation.currentLayout = getListLayout()
            }
            else -> super.onBackPressed()
        }
    }


    init {
        add(view = toolbar, width = matchParent, height = 56.dp)
        add(
            view = contentNavigation,
            width = matchParent,
            height = matchConstraint,
            weight = 1.0f
        )
        contentNavigation.currentLayout = loadingView.value
        application.jobManager.load(emptyMap()) { apply(it) }
    }

}

