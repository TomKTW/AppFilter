package dev.kobalt.core.components.image

import android.graphics.Bitmap
import android.widget.ImageView
import com.nostra13.universalimageloader.core.DisplayImageOptions
import com.nostra13.universalimageloader.core.ImageLoader
import com.nostra13.universalimageloader.core.assist.ImageScaleType
import dev.kobalt.core.application.Application
import dev.kobalt.core.extension.Image

class RemoteImageLoader(val application: Application) {

    fun load(url: String?, view: ImageView, placeholder: Image? = null) {
        ImageLoader.getInstance().cancelDisplayTask(view)
        view.setImageDrawable(null)
        ImageLoader.getInstance().displayImage(
            url, view, DisplayImageOptions.Builder()
                .bitmapConfig(Bitmap.Config.RGB_565)
                .cacheOnDisk(true)
                .cacheInMemory(false)
                .imageScaleType(ImageScaleType.IN_SAMPLE_POWER_OF_2)
                .showImageOnLoading(placeholder)
                .build()
        )
    }

}
