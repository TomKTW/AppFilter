package dev.kobalt.core.utility

import java.lang.ref.WeakReference

class WeakReferenceCache<T>(
    private val initialize: () -> T
) {

    private var weakReference: WeakReference<T>? = null

    val value: T
        get() = weakReference?.get() ?: initialize.invoke().let {
            weakReference = WeakReference(it); it
        }

}