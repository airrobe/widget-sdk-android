package com.airrobe.widgetsdk.airrobedemo.utils

import android.app.Activity
import android.view.WindowManager
import androidx.core.content.ContextCompat
import com.airrobe.widgetsdk.airrobedemo.R

object StatusBarTranslucent {
    fun setStatusBarTranslucent(activity: Activity, makeTranslucent: Boolean) {
        if (makeTranslucent) {
            activity.window.addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS)
        } else {
            activity.window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS)
        }
    }

    fun setStatusBarColor(activity: Activity) {
        val window = activity.window
        window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS)
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS)
        window.statusBarColor = ContextCompat.getColor(activity, R.color.status_bar_color)
    }
}
