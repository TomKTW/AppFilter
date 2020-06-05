package dev.kobalt.core.utility

import android.os.Build
import android.widget.PopupWindow
import dev.kobalt.core.application.NativeApplication
import dev.kobalt.core.application.NativeView

class Popup {

    var nativePopup: PopupWindow? = null

    fun display(content: NativeView, anchor: NativeView, width: Int, height: Int) {
        nativePopup?.dismiss()
        nativePopup = PopupWindow(NativeApplication.instance.native).apply {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.CUPCAKE) isOutsideTouchable = true
            contentView = content.nativeView
            this.width = width
            this.height = height
            setOnDismissListener {
                nativePopup?.contentView = null
                nativePopup = null
            }
            showAsDropDown(anchor.nativeView, 0, 0)
        }
    }

    fun dismiss() {
        nativePopup?.dismiss()
    }

}