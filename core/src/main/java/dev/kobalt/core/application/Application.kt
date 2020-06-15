package dev.kobalt.core.application

import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.PorterDuff
import android.graphics.Typeface
import com.caverock.androidsvg.SVG
import dev.kobalt.core.components.image.RemoteImageLoader
import dev.kobalt.core.components.job.JobManager
import dev.kobalt.core.extension.Color
import dev.kobalt.core.extension.Font
import dev.kobalt.core.extension.Image
import dev.kobalt.core.extension.toImage
import kotlin.math.ceil

class Application(val native: NativeApplication) {

    val jobManager = JobManager(this)
    val remoteImageLoader = RemoteImageLoader(this)

    fun getSvgImageFromResources(id: Int, color: Color): Image? {
        return SVG.getFromResource(native, id).takeIf { it.documentWidth != -1f }?.let { svg ->
            svg.documentWidth = svg.documentWidth * 2
            svg.documentHeight = svg.documentHeight * 2
            Bitmap.createBitmap(
                ceil(svg.documentWidth).toInt(),
                ceil(svg.documentHeight).toInt(),
                Bitmap.Config.ARGB_8888
            ).also {
                Canvas(it).apply {
                    svg.renderToCanvas(this)
                    drawColor(color, PorterDuff.Mode.SRC_IN)
                }
            }.toImage()
        }
    }

    fun getFontFromAssets(path: String): Font = Typeface.createFromAsset(native.assets, path)

}


