package com.airrobe.widgetsdk.airrobewidget.utils

import android.annotation.SuppressLint
import android.content.Context
import android.os.Build
import android.provider.Settings
import android.text.Html
import android.text.Spanned
import android.view.MotionEvent
import android.view.View
import android.view.animation.AnimationUtils
import com.airrobe.widgetsdk.airrobewidget.R
import com.airrobe.widgetsdk.airrobewidget.config.AirRobeWidgetConfig
import com.airrobe.widgetsdk.airrobewidget.service.api_controllers.AirRobeTelemetryEventController

internal object AirRobeAppUtils {
    @SuppressLint("HardwareIds")
    fun getDeviceId(context: Context): String {
        return Settings.Secure.getString(context.contentResolver, Settings.Secure.ANDROID_ID)
    }

    fun touchAnimator(context: Context, v: View, event: MotionEvent): Boolean {
        when (event.action) {
            MotionEvent.ACTION_DOWN -> {
                val pressedAnim =
                    AnimationUtils.loadAnimation(context, R.anim.alpha_animation_pressed)
                v.startAnimation(pressedAnim)
                return false
            }
            MotionEvent.ACTION_CANCEL -> {
                val releasedAnim =
                    AnimationUtils.loadAnimation(context, R.anim.alpha_animation_released)
                v.startAnimation(releasedAnim)
                return false
            }
            MotionEvent.ACTION_UP -> {
                val releasedAnim =
                    AnimationUtils.loadAnimation(context, R.anim.alpha_animation_released)
                v.startAnimation(releasedAnim)
                return true
            }
        }
        return false
    }

    fun getColor(context: Context, color: Int): Int {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            return context.getColor(color)
        }
        return context.resources.getColor(color)
    }

    fun fromHtml(text: String): Spanned {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            return Html.fromHtml(text, Html.FROM_HTML_MODE_COMPACT)
        }
        return Html.fromHtml(text)
    }
}