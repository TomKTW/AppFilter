package dev.kobalt.core.resources

import dev.kobalt.core.application.ApplicationBinder
import dev.kobalt.core.extension.Color

object Colors : ApplicationBinder {

    fun valueOf(value: Long): Color = value.toInt()

    val white get() = valueOf(0xFFFFFFFF)
    val black get() = valueOf(0xFF000000)
    val clear get() = valueOf(0x00000000)

    val gray get() = valueOf(0xFF888888)
    val grayDark get() = valueOf(0xFF444444)
    val grayLight get() = valueOf(0xFFAAAAAA)

}