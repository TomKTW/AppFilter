package dev.kobalt.core.extension

fun String.toClass(): Class<*> = Class.forName(this)