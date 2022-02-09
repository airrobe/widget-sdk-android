package com.airrobe.widgetsdk.airrobewidget.utils

import android.content.Context
import android.view.MotionEvent
import android.view.View
import android.view.animation.AnimationUtils
import com.airrobe.widgetsdk.airrobewidget.R

object AirRobeAppUtils {
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
}