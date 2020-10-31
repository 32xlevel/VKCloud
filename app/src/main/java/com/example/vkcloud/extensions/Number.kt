package com.example.vkcloud.extensions

import android.content.res.Resources
import java.text.CharacterIterator
import java.text.StringCharacterIterator
import kotlin.math.abs

fun Int.dpToPx(): Int {
    return (this / Resources.getSystem().displayMetrics.density).toInt()
}

fun Long.humanReadableBytes(): String {
    val absB = if (this == Long.MIN_VALUE) Long.MAX_VALUE else abs(this)
    if (absB < 1024) {
        return "$this B"
    }
    var value = absB
    val ci: CharacterIterator = StringCharacterIterator("KMGTPE")
    var i = 40
    while (i >= 0 && absB > 0xfffccccccccccccL shr i) {
        value = value shr 10
        ci.next()
        i -= 10
    }
    value *= java.lang.Long.signum(this).toLong()
    return java.lang.String.format("%.1f %cB", value / 1024.0, ci.current())
}