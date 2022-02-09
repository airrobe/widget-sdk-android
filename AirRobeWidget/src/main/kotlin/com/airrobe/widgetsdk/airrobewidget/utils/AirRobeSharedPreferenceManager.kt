package com.airrobe.widgetsdk.airrobewidget.utils

import android.content.Context
import androidx.preference.PreferenceManager

internal object AirRobeSharedPreferenceManager {
    private const val OptedInKey = "OptedInKey"
    fun setOptedIn(context: Context, value: Boolean) {
        val sp = PreferenceManager.getDefaultSharedPreferences(context)
        val editor = sp.edit()
        editor.putBoolean(OptedInKey, value)
        editor.apply()
    }

    fun getOptedIn(context: Context): Boolean {
        return PreferenceManager.getDefaultSharedPreferences(context).getBoolean(OptedInKey, false)
    }

    private const val OrderOptedInKey = "OrderOptedInKey"
    fun setOrderOptedIn(context: Context, value: Boolean) {
        val sp = PreferenceManager.getDefaultSharedPreferences(context)
        val editor = sp.edit()
        editor.putBoolean(OrderOptedInKey, value)
        editor.apply()
    }

    fun getOrderOptedIn(context: Context): Boolean {
        return PreferenceManager.getDefaultSharedPreferences(context).getBoolean(OrderOptedInKey, false)
    }
}