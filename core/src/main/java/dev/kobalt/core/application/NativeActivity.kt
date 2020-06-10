@file:Suppress("unused")

package dev.kobalt.core.application

import android.app.Activity
import android.os.Bundle
import dev.kobalt.core.extension.metaData
import dev.kobalt.core.extension.toClass
import dev.kobalt.core.utility.ViewCache
import dev.kobalt.core.view.BlankView

class NativeActivity : Activity() {

    private val app: Application get() = NativeApplication.instance

    private val rootViewKey = "dev.kobalt.core.android.activity.rootview"

    private val rootViewClass get() = metaData?.getString(rootViewKey)?.toClass()

    private var savedState = false

    private var _rootView: NativeView? = null

    private var rootView: NativeView?
        get() = _rootView
        set(value) {
            setContentView(value?.nativeView)
            _rootView = value
        }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        if (ViewCache.cachedView == null) {
            ViewCache.cachedView = ((rootViewClass?.newInstance() as? NativeView) ?: BlankView())
        }
        rootView = ViewCache.cachedView

    }

    override fun onSaveInstanceState(outState: Bundle) {
        savedState = true
        super.onSaveInstanceState(outState)
    }

    override fun onDestroy() {
        rootView = BlankView()
        if (isFinishing) {
            ViewCache.cachedView = null
        }
        super.onDestroy()
    }

    override fun onBackPressed() {
        rootView?.onBackPressed() ?: super.onBackPressed()
    }

}


