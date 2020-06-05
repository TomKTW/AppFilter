package dev.kobalt.core.extension

import org.json.JSONArray
import org.json.JSONObject

typealias Color = Int
typealias ColorImage = android.graphics.drawable.ColorDrawable
typealias Gravity = Int
typealias Font = android.graphics.Typeface
typealias Image = android.graphics.drawable.Drawable
typealias Bitmap = android.graphics.Bitmap
typealias BitmapImage = android.graphics.drawable.BitmapDrawable
typealias StateImage = android.graphics.drawable.StateListDrawable
typealias LayerImage = android.graphics.drawable.LayerDrawable
typealias JsonObject = JSONObject
typealias JsonArray = JSONArray

fun JsonObject.toMap(): Map<String, Any> {
    val map: MutableMap<String, Any> =
        HashMap()
    val keys = keys()
    while (keys.hasNext()) {
        val key = keys.next()
        var value = this[key]
        if (value is JsonArray) {
            value = value.toList()
        } else if (value is JsonObject) {
            value = value.toMap()
        }
        map[key] = value
    }
    return map
}

fun JsonArray.toList(): List<Any> {
    val list: MutableList<Any> = ArrayList()
    for (i in 0 until length()) {
        var value = this[i]
        if (value is JSONArray) {
            value = value.toList()
        } else if (value is JsonObject) {
            value = value.toMap()
        }
        list.add(value)
    }
    return list
}


inline fun <T1 : Any, T2 : Any, R : Any> safeLet(p1: T1?, p2: T2?, block: (T1, T2) -> R?): R? {
    return if (p1 != null && p2 != null) block(p1, p2) else null
}