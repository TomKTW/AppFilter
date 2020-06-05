package dev.kobalt.appfilter.main

import dev.kobalt.appfilter.entity.RemoteEntity
import dev.kobalt.appfilter.extension.*
import dev.kobalt.appfilter.view.FilterLabelInputView
import dev.kobalt.appfilter.view.FilterOptionInputView
import dev.kobalt.appfilter.view.ToolbarView
import dev.kobalt.core.application.NativeView
import dev.kobalt.core.extension.dp
import dev.kobalt.core.resources.Colors
import dev.kobalt.core.resources.Images
import dev.kobalt.core.view.BlankView
import dev.kobalt.core.view.StackView
import java.util.*
import kotlin.properties.Delegates

class MainView : StackView(orientation = Orientation.VERTICAL) {

    private var toolbar: ToolbarView = ToolbarView().apply {
        filterButton.onTap = { currentLayout = formLayout }
        backButton.onTap = {
            this@MainView.onBackPressed()
        }
        launchButton.onTap = {
            detailsEntry?.link?.let { openUrl(it) }
        }
    }

    fun Int.digitalGroup(): String {
        return String.format(Locale.ENGLISH, "%,d", this)
    }

    private val formLayout = FilterView().apply {
        submitButton.onTap = {
            hideKeyboard()
            currentLayout = loadLayout
            application.jobManager.fetch(values) { apply(it) }
        }
    }

    private val listLayout = ResultView().apply {
        entryRecycler.onItemTap = {
            detailsEntry = it
            currentLayout = detailsLayout
        }
    }

    private val detailsLayout = DetailsView()

    private val loadLayout = LoadingView()

    private var currentLayout: NativeView by Delegates.observable<NativeView>(BlankView()) { _, oldValue, newValue ->
        when (newValue) {
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
        remove(oldValue)
        add(view = newValue, width = matchParent, height = matchConstraint, weight = 1.0f)
    }

    private var detailsEntry: RemoteEntity.Entry? by Delegates.observable<RemoteEntity.Entry?>(null) { _, _, it ->
        detailsLayout.apply {
            val rating = strings.detailsRating(it?.ratingAverage ?: "-")
            val reviewCount = it?.ratingTotal?.toIntOrNull() ?: 0
            val reviews = strings.detailsReviews(reviewCount.digitalGroup())
            val containsAds = it?.ads.equals("yes", ignoreCase = true)
            val ads = if (containsAds) strings.detailsWithAds else strings.detailsWithoutAds
            val containsIap = it?.iap.equals("yes", ignoreCase = true)
            val iap = if (containsIap) strings.detailsWithIap else strings.detailsWithoutIap
            val placeholder = Images.refreshIcon(Colors.white)
            titleLabel.text = it?.name.orEmpty()
            developerLabel.text = it?.developer.orEmpty()
            categoryLabel.text = it?.category.orEmpty()
            contentRatingLabel.text = it?.contentRating.orEmpty()
            ratingLabel.text = "$rating, $reviews"
            statsLabel.text = "$ads, $iap"
            thumbnailImage.load(it?.image, placeholder)
            detailsLabel.text = it?.description.orEmpty()
            resetScroll()
        }
    }

    fun apply(entity: RemoteEntity) {
        currentLayout = listLayout
        listLayout.setList(entity.entries)

        if (entity.filters.isNotEmpty()) {
            formLayout.optionStack.children.toList().forEach { formLayout.optionStack.remove(it) }
            entity.filters.forEach { filter ->
                formLayout.optionStack.add(
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

    override fun onBackPressed() {
        when (currentLayout) {
            formLayout -> {
                hideKeyboard()
                currentLayout = listLayout
            }
            detailsLayout -> {
                currentLayout = listLayout
            }
            else -> super.onBackPressed()
        }
    }


    init {
        add(view = toolbar, width = matchParent, height = 56.dp)
        currentLayout = loadLayout
        application.jobManager.load(formLayout.values) { apply(it) }
    }

}

