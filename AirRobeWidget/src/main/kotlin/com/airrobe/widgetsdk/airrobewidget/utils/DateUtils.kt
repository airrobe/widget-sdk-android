package com.airrobe.widgetsdk.airrobewidget.utils

import java.text.DateFormat
import java.text.SimpleDateFormat
import java.util.*

internal object DateUtils {
    private const val ISO_8601_24H_FULL_FORMAT = "yyyy-MM-dd'T'HH:mm:ss.SSS'Z'"

    @JvmName("toIsoStringNullable")
    fun Date?.toIsoString(): String? {
        return this?.toIsoString()
    }

    fun Date.toIsoString(): String {
        val dateFormat: DateFormat = SimpleDateFormat(ISO_8601_24H_FULL_FORMAT, Locale.getDefault())
        dateFormat.timeZone = TimeZone.getTimeZone("gmt")
        return dateFormat.format(this)
    }
}