@file:Suppress("unused")

package dev.kobalt.appfilter.extension

import dev.kobalt.core.resources.Strings

val Strings.applicationName get() = "AppFilter"
val Strings.filterSubmit get() = "Submit"
val Strings.filterOptions get() = "Filter options"
val Strings.details get() = "Details"
fun Strings.detailsRating(value: String) = "Rating: $value"
fun Strings.detailsReviews(count: String) = "$count reviews"
val Strings.detailsWithAds get() = "Contains ads"
val Strings.detailsWithoutAds get() = "No ads"
val Strings.detailsWithIap get() = "Contains IAP"
val Strings.detailsWithoutIap get() = "No IAP"