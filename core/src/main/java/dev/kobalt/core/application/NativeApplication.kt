package dev.kobalt.core.application

import com.nostra13.universalimageloader.core.ImageLoader
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration

class NativeApplication : android.app.Application() {

    companion object {
        lateinit var instance: Application
            private set
    }

    override fun onCreate() {
        super.onCreate()
        ImageLoader.getInstance().init(ImageLoaderConfiguration.Builder(this).build())
        instance = Application(this)
    }

}