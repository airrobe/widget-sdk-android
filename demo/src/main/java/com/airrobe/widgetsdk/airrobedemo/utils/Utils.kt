package com.airrobe.widgetsdk.airrobedemo.utils

import android.content.Context
import android.text.TextUtils
import android.util.Patterns
import android.view.MotionEvent
import android.view.View
import android.view.animation.AnimationUtils
import android.widget.Toast
import com.airrobe.widgetsdk.airrobedemo.R

object Utils {
    fun isValidEmail(email: CharSequence): Boolean {
        return !TextUtils.isEmpty(email) && Patterns.EMAIL_ADDRESS.matcher(email).matches()
    }

    fun Context.toast(message: CharSequence) = Toast.makeText(this, message, Toast.LENGTH_SHORT).show()

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