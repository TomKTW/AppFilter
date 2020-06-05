package dev.kobalt.core.extension

import android.app.Activity
import android.content.pm.PackageManager
import android.os.Bundle

val Activity.metaData: Bundle?
    get() = packageManager.getActivityInfo(
        componentName, PackageManager.GET_META_DATA
    ).metaData


