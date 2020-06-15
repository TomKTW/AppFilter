package dev.kobalt.core.application

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Context
import android.content.Intent
import android.graphics.drawable.Drawable
import android.net.Uri
import android.os.Build
import android.view.View
import android.view.inputmethod.InputMethodManager
import dev.kobalt.core.extension.Gravity
import dev.kobalt.core.extension.safeLet
import dev.kobalt.core.resources.Colors
import dev.kobalt.core.resources.Fonts
import dev.kobalt.core.resources.Images
import dev.kobalt.core.resources.Strings

abstract class NativeView {

    val application: Application get() = NativeApplication.instance

    abstract val nativeView: View

    protected val colors: Colors get() = Colors

    protected val images: Images get() = Images

    protected val fonts: Fonts get() = Fonts

    protected val strings: Strings get() = Strings

    private val nativeActivity: Activity?
        get() = nativeView.rootView.context as? Activity

    @Suppress("DEPRECATION")
    var background: Drawable?
        get() = nativeView.background
        set(value) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
                nativeView.background = value
            } else {
                nativeView.setBackgroundDrawable(value)
            }
        }

    fun hideKeyboard() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.CUPCAKE) {
            safeLet(
                (application.native.getSystemService(Context.INPUT_METHOD_SERVICE) as? InputMethodManager),
                nativeActivity?.currentFocus?.windowToken
            ) { inputMethodManager, windowToken ->
                inputMethodManager.hideSoftInputFromWindow(
                    windowToken,
                    InputMethodManager.HIDE_NOT_ALWAYS
                )
            }
        }
    }

    fun openUrl(url: String) {
        val intent = Intent(Intent.ACTION_VIEW, Uri.parse(url)).also {
            it.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        }
        application.native.startActivity(intent)

    }

    open fun onBackPressed() {

    }

    protected fun finish() {
        nativeActivity?.finish()
    }

    private var _onTap: (() -> Unit)? = null

    var onTap: (() -> Unit)?
        get() = _onTap
        set(value) {
            _onTap = value
            value?.let { listener ->
                nativeView.setOnClickListener { listener.invoke() }
            } ?: kotlin.run {
                nativeView.setOnClickListener(null)
            }
        }

    var isStatusBarVisible: Boolean
        get() = false
        set(value) {
            if (Build.VERSION.SDK_INT < 16) {
                if (!value) {
                    nativeActivity?.window?.setFlags(
                        android.view.WindowManager.LayoutParams.FLAG_FULLSCREEN,
                        android.view.WindowManager.LayoutParams.FLAG_FULLSCREEN
                    )
                } else {
                    nativeActivity?.window?.clearFlags(
                        android.view.WindowManager.LayoutParams.FLAG_FULLSCREEN
                    )
                }
            } else {
                val decorView: View = nativeActivity?.window!!.decorView
                val uiOptions =
                    if (!value) View.SYSTEM_UI_FLAG_FULLSCREEN else View.SYSTEM_UI_FLAG_VISIBLE
                decorView.systemUiVisibility = uiOptions

            }

        }

    fun setPadding(left: Int = 0, top: Int = 0, right: Int = 0, bottom: Int = 0) {
        nativeView.setPadding(left, top, right, bottom)
    }

    var isVisible: Boolean
        get() = nativeView.visibility == View.VISIBLE
        set(value) {
            nativeView.visibility = if (value) View.VISIBLE else View.GONE
        }

    var isInvisible: Boolean
        get() = nativeView.visibility == View.INVISIBLE
        set(value) {
            nativeView.visibility = if (value) View.INVISIBLE else View.VISIBLE
        }

    val wrapContent
        get() = android.view.ViewGroup.LayoutParams.WRAP_CONTENT

    val matchParent
        get() = android.view.ViewGroup.LayoutParams.MATCH_PARENT

    val matchConstraint
        get() = 0

    val topGravity: Gravity
        get() = android.view.Gravity.TOP

    val bottomGravity: Gravity
        get() = android.view.Gravity.TOP

    val leftGravity: Gravity
        @SuppressLint("RtlHardcoded")
        get() = android.view.Gravity.LEFT

    val rightGravity: Gravity
        @SuppressLint("RtlHardcoded")
        get() = android.view.Gravity.RIGHT

    val centerGravity: Gravity
        get() = android.view.Gravity.CENTER

    val centerHorizontalGravity: Gravity
        get() = android.view.Gravity.CENTER_HORIZONTAL

    val centerVerticalGravity: Gravity
        get() = android.view.Gravity.CENTER_VERTICAL
}