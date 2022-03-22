package com.airrobe.widgetsdk.airrobewidget.utils

import android.content.Context
import android.content.Context.MODE_PRIVATE

internal object AirRobeSharedPreferenceManager {
    private const val OptedInKey = "OptedInKey"
    private const val PREFERENCES_FILENAME = "airrobe_shared_preference"
    fun setOptedIn(context: Context, value: Boolean) {

        val sp = context.getSharedPreferences(PREFERENCES_FILENAME, MODE_PRIVATE)
        val editor = sp.edit()
        editor.putBoolean(OptedInKey, value)
        editor.apply()
    }

    fun getOptedIn(context: Context): Boolean {
        return context.getSharedPreferences(PREFERENCES_FILENAME, MODE_PRIVATE).getBoolean(OptedInKey, false)
    }

    private const val OrderOptedInKey = "OrderOptedInKey"
    fun setOrderOptedIn(context: Context, value: Boolean) {
        val sp = context.getSharedPreferences(PREFERENCES_FILENAME, MODE_PRIVATE)
        val editor = sp.edit()
        editor.putBoolean(OrderOptedInKey, value)
        editor.apply()
    }

    fun getOrderOptedIn(context: Context): Boolean {
        return context.getSharedPreferences(PREFERENCES_FILENAME, MODE_PRIVATE).getBoolean(OrderOptedInKey, false)
    }
}