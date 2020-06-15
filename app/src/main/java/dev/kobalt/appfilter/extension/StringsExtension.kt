@file:Suppress("unused")

package dev.kobalt.appfilter.extension

import dev.kobalt.core.resources.Strings

val Strings.applicationName get() = "AppFilter"
val Strings.filterSubmit get() = "Submit"
val Strings.filter get() = "Filter"
val Strings.details get() = "Details"
fun Strings.detailsRating(value: String?) = "Rating: ${value ?: "N/A"}"
fun Strings.detailsReviews(count: String?) = "${count ?: "N/A"} reviews"
val Strings.detailsWithAds get() = "With ads"
val Strings.detailsWithoutAds get() = "Without ads"
val Strings.detailsWithIap get() = "With IAP"
val Strings.detailsWithoutIap get() = "Without IAP"