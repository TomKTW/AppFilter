package dev.kobalt.core.application

interface ApplicationBinder {

    val application: Application get() = NativeApplication.instance

}