package com.airrobe.widgetsdk.airrobewidget.utils

import android.content.Context
import android.content.Context.MODE_PRIVATE
import com.airrobe.widgetsdk.airrobewidget.service.models.AirRobeWidgetVariant
import com.google.gson.Gson
import com.google.gson.JsonSyntaxException

internal object AirRobeSharedPreferenceManager {
    private const val PREFERENCES_FILENAME = "airrobe_shared_preference"
    private enum class Keys(val raw: String) {
        OptedInKey("OptedInKey"),
        OrderOptedInKey("OrderOptedInKey"),
        TargetSplitTestVariantKey("TargetSplitTestVariantKey")
    }

    fun setOptedIn(context: Context, value: Boolean) {
        val editor = context.getSharedPreferences(PREFERENCES_FILENAME, MODE_PRIVATE).edit()
        editor.putBoolean(Keys.OptedInKey.raw, value)
        editor.apply()
    }

    fun getOptedIn(context: Context): Boolean {
        return context.getSharedPreferences(PREFERENCES_FILENAME, MODE_PRIVATE).getBoolean(Keys.OptedInKey.raw, false)
    }

    fun setOrderOptedIn(context: Context, value: Boolean) {
        val editor = context.getSharedPreferences(PREFERENCES_FILENAME, MODE_PRIVATE).edit()
        editor.putBoolean(Keys.OrderOptedInKey.raw, value)
        editor.apply()
    }

    fun getOrderOptedIn(context: Context): Boolean {
        return context.getSharedPreferences(PREFERENCES_FILENAME, MODE_PRIVATE).getBoolean(Keys.OrderOptedInKey.raw, false)
    }

    fun setTargetSplitTestVariant(context: Context, value: AirRobeWidgetVariant?) {
        val editor = context.getSharedPreferences(PREFERENCES_FILENAME, MODE_PRIVATE).edit()
        val json = Gson().toJson(value)
        editor.putString(Keys.TargetSplitTestVariantKey.raw, json)
        editor.apply()
    }

    fun getTargetSplitTestVariant(context: Context): AirRobeWidgetVariant? {
        return try {
            val json = context.getSharedPreferences(PREFERENCES_FILENAME, MODE_PRIVATE)
                .getString(Keys.TargetSplitTestVariantKey.raw, "")
            Gson().fromJson(json, AirRobeWidgetVariant::class.java)
        } catch (exception: JsonSyntaxException) {
            null
        }
    }
}