package dev.kobalt.appfilter.extension

import dev.kobalt.appfilter.R
import dev.kobalt.core.extension.Color
import dev.kobalt.core.resources.Images

fun Images.backIcon(color: Color) = fromResources(R.raw.icon_back_24dp_black, color)
fun Images.filterIcon(color: Color) = fromResources(R.raw.icon_filter_24dp_black, color)
fun Images.refreshIcon(color: Color) = fromResources(R.raw.icon_refresh_24dp_black, color)
fun Images.launchIcon(color: Color) = fromResources(R.raw.icon_launch_24dp_black, color)