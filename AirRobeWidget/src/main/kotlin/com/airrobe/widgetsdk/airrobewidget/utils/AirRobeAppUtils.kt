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
import com.airrobe.widgetsdk.airrobewidget.config.AirRobeEventData
import com.airrobe.widgetsdk.airrobewidget.config.EventName
import com.airrobe.widgetsdk.airrobewidget.config.PageName
import com.airrobe.widgetsdk.airrobewidget.eventListenerInstance
import com.airrobe.widgetsdk.airrobewidget.service.api_controllers.AirRobeTelemetryEventController
import com.airrobe.widgetsdk.airrobewidget.sessionId
import com.airrobe.widgetsdk.airrobewidget.widgetInstance

internal object AirRobeAppUtils {
    @SuppressLint("HardwareIds")
    fun getDeviceId(context: Context): String {
        return Settings.Secure.getString(context.contentResolver, Settings.Secure.ANDROID_ID)
    }

    fun telemetryEvent(
        context: Context,
        eventName: String,
        pageName: String,
        brand: String? = null,
        material: String? = null,
        category: String? = null,
        department: String? = null
    ) {
        if (widgetInstance.configuration == null) {
            return
        }
        val telemetryEventController = AirRobeTelemetryEventController()
        telemetryEventController.start(
            context,
            widgetInstance.configuration!!,
            eventName,
            pageName,
            brand,
            material,
            category,
            department
        )
    }

    fun dispatchEvent(context: Context, eventName: String, pageName: String) {
        if (widgetInstance.configuration == null || EventName.getByValue(eventName) == null) {
            return
        }
        val eventData = AirRobeEventData(
            widgetInstance.configuration!!.appId,
            getDeviceId(context),
            sessionId,
            EventName.getByValue(eventName)!!,
            "Android",
            context.getString(R.string.airrobe_widget_version),
            "default",
            PageName.getByValue(pageName) ?: PageName.Other
        )
        eventListenerInstance?.onEventEmitted(eventData)
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