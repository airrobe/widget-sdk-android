package com.airrobe.widgetsdk.airrobewidget.widgets

import android.annotation.SuppressLint
import android.app.Dialog
import android.content.Context
import android.content.res.ColorStateList
import android.graphics.Color
import android.graphics.drawable.GradientDrawable
import android.os.Bundle
import android.text.method.LinkMovementMethod
import android.view.*
import androidx.appcompat.widget.SwitchCompat
import androidx.core.graphics.drawable.DrawableCompat
import com.airrobe.widgetsdk.airrobewidget.databinding.AirrobeDialogLearnMoreBinding
import com.airrobe.widgetsdk.airrobewidget.utils.AirRobeAppUtils
import com.airrobe.widgetsdk.airrobewidget.utils.AirRobeSharedPreferenceManager
import com.airrobe.widgetsdk.airrobewidget.widgetInstance

internal class AirRobeLearnMore(context: Context) : Dialog(context) {
    private lateinit var binding: AirrobeDialogLearnMoreBinding
    lateinit var optInSwitch: SwitchCompat
    var isFromMultiOptIn: Boolean = false

    init {
        setCancelable(true)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        requestWindowFeature(Window.FEATURE_NO_TITLE)
        binding = AirrobeDialogLearnMoreBinding.inflate(LayoutInflater.from(context))
        setContentView(binding.root)
        initView()
        onClick()
    }

    private fun initView() {
        binding.optInSwitch.isChecked = AirRobeSharedPreferenceManager.getOptedIn(context)
        binding.optInSwitch.setOnCheckedChangeListener { _, isChecked ->
            AirRobeSharedPreferenceManager.setOptedIn(context, isChecked)
            optInSwitch.isChecked = isChecked
            if (isFromMultiOptIn) {
                AirRobeSharedPreferenceManager.setOrderOptedIn(context, isChecked)
            }
        }

        binding.tvFindOutMore.movementMethod = LinkMovementMethod.getInstance()
        initColorSet()
    }

    private fun initColorSet() {
        val step1Background = binding.rlStep1Container.background as GradientDrawable
        step1Background.setStroke(1, widgetInstance.borderColor)
        val step2Background = binding.rlStep2Container.background as GradientDrawable
        step2Background.setStroke(1, widgetInstance.borderColor)
        val toggleBackground = binding.rlToggleContainer.background as GradientDrawable
        toggleBackground.setStroke(1, widgetInstance.borderColor)

        binding.divider.setBackgroundColor(widgetInstance.separatorColor)
        binding.step1Divider.setBackgroundColor(widgetInstance.separatorColor)
        binding.step2Divider.setBackgroundColor(widgetInstance.separatorColor)

        binding.tvTitle.setTextColor(widgetInstance.textColor)
        binding.tvStep1Title.setTextColor(widgetInstance.textColor)
        binding.tvStep1Description.setTextColor(widgetInstance.textColor)
        binding.tvStep2Title.setTextColor(widgetInstance.textColor)
        binding.tvStep2Description.setTextColor(widgetInstance.textColor)
        binding.tvQuestion.setTextColor(widgetInstance.textColor)
        binding.tvAnswer.setTextColor(widgetInstance.textColor)
        binding.tvReady.setTextColor(widgetInstance.textColor)
        binding.tvToggleOn.setTextColor(widgetInstance.textColor)

        setSwitchColor()

        binding.tvFindOutMore.setLinkTextColor(widgetInstance.linkTextColor)
    }

    private fun setSwitchColor() {
        val states = arrayOf(
            intArrayOf(-android.R.attr.state_checked),
            intArrayOf(android.R.attr.state_checked)
        )
        val thumbColors = intArrayOf(
            Color.WHITE,
            Color.WHITE
        )
        val trackColors = intArrayOf(
            widgetInstance.borderColor,
            Color.BLUE
        )
        DrawableCompat.setTintList(DrawableCompat.wrap(binding.optInSwitch.thumbDrawable), ColorStateList(states, thumbColors))
        DrawableCompat.setTintList(DrawableCompat.wrap(binding.optInSwitch.trackDrawable), ColorStateList(states, trackColors))
    }

    @SuppressLint("ClickableViewAccessibility")
    private fun onClick() {
        binding.ivClose.setOnTouchListener { v, event ->
            if (AirRobeAppUtils.touchAnimator(context, v, event)) {
                dismiss()
            }
            true
        }
    }
}