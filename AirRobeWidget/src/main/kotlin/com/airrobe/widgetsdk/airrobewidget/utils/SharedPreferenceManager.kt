package com.airrobe.widgetsdk.airrobewidget.utils

import android.content.Context
import androidx.preference.PreferenceManager

internal object SharedPreferenceManager {
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
}