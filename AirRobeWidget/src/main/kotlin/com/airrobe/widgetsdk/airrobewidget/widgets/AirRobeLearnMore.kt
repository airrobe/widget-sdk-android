package com.airrobe.widgetsdk.airrobewidget.widgets

import android.annotation.SuppressLint
import android.app.Dialog
import android.content.Context
import android.content.res.ColorStateList
import android.graphics.Color
import android.graphics.drawable.GradientDrawable
import android.os.Build
import android.os.Bundle
import android.text.method.LinkMovementMethod
import android.view.*
import androidx.appcompat.widget.SwitchCompat
import androidx.core.graphics.drawable.DrawableCompat
import com.airrobe.widgetsdk.airrobewidget.R
import com.airrobe.widgetsdk.airrobewidget.databinding.AirrobeDialogLearnMoreBinding
import com.airrobe.widgetsdk.airrobewidget.utils.AirRobeAppUtils
import com.airrobe.widgetsdk.airrobewidget.utils.AirRobeSharedPreferenceManager
import com.airrobe.widgetsdk.airrobewidget.widgetInstance

@Suppress("DEPRECATION")
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
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            val step1Background = binding.rlStep1Container.background as GradientDrawable
            step1Background.setStroke(1,
                if (widgetInstance.borderColor == 0)
                    context.getColor(R.color.airrobe_widget_default_border_color)
                else
                    widgetInstance.borderColor
            )
            val step2Background = binding.rlStep2Container.background as GradientDrawable
            step2Background.setStroke(1,
                if (widgetInstance.borderColor == 0)
                    context.getColor(R.color.airrobe_widget_default_border_color)
                else
                    widgetInstance.borderColor
            )
            val toggleBackground = binding.rlToggleContainer.background as GradientDrawable
            toggleBackground.setStroke(1,
                if (widgetInstance.borderColor == 0)
                    context.getColor(R.color.airrobe_widget_default_border_color)
                else
                    widgetInstance.borderColor
            )

            binding.divider.setBackgroundColor(
                if (widgetInstance.separatorColor == 0)
                    context.getColor(R.color.airrobe_widget_default_separator_color)
                else
                    widgetInstance.separatorColor
            )
            binding.step1Divider.setBackgroundColor(
                if (widgetInstance.separatorColor == 0)
                    context.getColor(R.color.airrobe_widget_default_separator_color)
                else
                    widgetInstance.separatorColor
            )
            binding.step2Divider.setBackgroundColor(
                if (widgetInstance.separatorColor == 0)
                    context.getColor(R.color.airrobe_widget_default_separator_color)
                else
                    widgetInstance.separatorColor
            )

            binding.tvTitle.setTextColor(
                if (widgetInstance.textColor == 0)
                    context.getColor(R.color.airrobe_widget_default_text_color)
                else
                    widgetInstance.textColor
            )
            binding.tvStep1Title.setTextColor(
                if (widgetInstance.textColor == 0)
                    context.getColor(R.color.airrobe_widget_default_text_color)
                else
                    widgetInstance.textColor
            )
            binding.tvStep1Description.setTextColor(
                if (widgetInstance.textColor == 0)
                    context.getColor(R.color.airrobe_widget_default_text_color)
                else
                    widgetInstance.textColor
            )
            binding.tvStep2Title.setTextColor(
                if (widgetInstance.textColor == 0)
                    context.getColor(R.color.airrobe_widget_default_text_color)
                else
                    widgetInstance.textColor
            )
            binding.tvStep2Description.setTextColor(
                if (widgetInstance.textColor == 0)
                    context.getColor(R.color.airrobe_widget_default_text_color)
                else
                    widgetInstance.textColor
            )
            binding.tvQuestion.setTextColor(
                if (widgetInstance.textColor == 0)
                    context.getColor(R.color.airrobe_widget_default_text_color)
                else
                    widgetInstance.textColor
            )
            binding.tvAnswer.setTextColor(
                if (widgetInstance.textColor == 0)
                    context.getColor(R.color.airrobe_widget_default_text_color)
                else
                    widgetInstance.textColor
            )
            binding.tvReady.setTextColor(
                if (widgetInstance.textColor == 0)
                    context.getColor(R.color.airrobe_widget_default_text_color)
                else
                    widgetInstance.textColor
            )
            binding.tvToggleOn.setTextColor(
                if (widgetInstance.textColor == 0)
                    context.getColor(R.color.airrobe_widget_default_text_color)
                else
                    widgetInstance.textColor
            )

            binding.ivClose.setColorFilter(
                if (widgetInstance.textColor == 0)
                    context.getColor(R.color.airrobe_widget_default_text_color)
                else
                    widgetInstance.textColor
            )
            binding.ivCheck.setColorFilter(
                if (widgetInstance.textColor == 0)
                    context.getColor(R.color.airrobe_widget_default_text_color)
                else
                    widgetInstance.textColor
            )
            binding.ivPlant1.setColorFilter(
                if (widgetInstance.textColor == 0)
                    context.getColor(R.color.airrobe_widget_default_text_color)
                else
                    widgetInstance.textColor
            )
            binding.ivEmoji.setColorFilter(
                if (widgetInstance.textColor == 0)
                    context.getColor(R.color.airrobe_widget_default_text_color)
                else
                    widgetInstance.textColor
            )
            binding.ivPlant2.setColorFilter(
                if (widgetInstance.textColor == 0)
                    context.getColor(R.color.airrobe_widget_default_text_color)
                else
                    widgetInstance.textColor
            )

            binding.tvFindOutMore.setLinkTextColor(
                if (widgetInstance.linkTextColor == 0)
                    context.getColor(R.color.airrobe_widget_default_link_text_color)
                else
                    widgetInstance.linkTextColor
            )
        } else {
            val step1Background = binding.rlStep1Container.background as GradientDrawable
            step1Background.setStroke(1,
                if (widgetInstance.borderColor == 0)
                    context.resources.getColor(R.color.airrobe_widget_default_border_color)
                else
                    widgetInstance.borderColor
            )
            val step2Background = binding.rlStep2Container.background as GradientDrawable
            step2Background.setStroke(1,
                if (widgetInstance.borderColor == 0)
                    context.resources.getColor(R.color.airrobe_widget_default_border_color)
                else
                    widgetInstance.borderColor
            )
            val toggleBackground = binding.rlToggleContainer.background as GradientDrawable
            toggleBackground.setStroke(1,
                if (widgetInstance.borderColor == 0)
                    context.resources.getColor(R.color.airrobe_widget_default_border_color)
                else
                    widgetInstance.borderColor
            )

            binding.divider.setBackgroundColor(
                if (widgetInstance.separatorColor == 0)
                    context.resources.getColor(R.color.airrobe_widget_default_separator_color)
                else
                    widgetInstance.separatorColor
            )
            binding.step1Divider.setBackgroundColor(
                if (widgetInstance.separatorColor == 0)
                    context.resources.getColor(R.color.airrobe_widget_default_separator_color)
                else
                    widgetInstance.separatorColor
            )
            binding.step2Divider.setBackgroundColor(
                if (widgetInstance.separatorColor == 0)
                    context.resources.getColor(R.color.airrobe_widget_default_separator_color)
                else
                    widgetInstance.separatorColor
            )

            binding.tvTitle.setTextColor(
                if (widgetInstance.textColor == 0)
                    context.resources.getColor(R.color.airrobe_widget_default_text_color)
                else
                    widgetInstance.textColor
            )
            binding.tvStep1Title.setTextColor(
                if (widgetInstance.textColor == 0)
                    context.resources.getColor(R.color.airrobe_widget_default_text_color)
                else
                    widgetInstance.textColor
            )
            binding.tvStep1Description.setTextColor(
                if (widgetInstance.textColor == 0)
                    context.resources.getColor(R.color.airrobe_widget_default_text_color)
                else
                    widgetInstance.textColor
            )
            binding.tvStep2Title.setTextColor(
                if (widgetInstance.textColor == 0)
                    context.resources.getColor(R.color.airrobe_widget_default_text_color)
                else
                    widgetInstance.textColor
            )
            binding.tvStep2Description.setTextColor(
                if (widgetInstance.textColor == 0)
                    context.resources.getColor(R.color.airrobe_widget_default_text_color)
                else
                    widgetInstance.textColor
            )
            binding.tvQuestion.setTextColor(
                if (widgetInstance.textColor == 0)
                    context.resources.getColor(R.color.airrobe_widget_default_text_color)
                else
                    widgetInstance.textColor
            )
            binding.tvAnswer.setTextColor(
                if (widgetInstance.textColor == 0)
                    context.resources.getColor(R.color.airrobe_widget_default_text_color)
                else
                    widgetInstance.textColor
            )
            binding.tvReady.setTextColor(
                if (widgetInstance.textColor == 0)
                    context.resources.getColor(R.color.airrobe_widget_default_text_color)
                else
                    widgetInstance.textColor
            )
            binding.tvToggleOn.setTextColor(
                if (widgetInstance.textColor == 0)
                    context.resources.getColor(R.color.airrobe_widget_default_text_color)
                else
                    widgetInstance.textColor
            )

            binding.ivClose.setColorFilter(
                if (widgetInstance.textColor == 0)
                    context.resources.getColor(R.color.airrobe_widget_default_text_color)
                else
                    widgetInstance.textColor
            )
            binding.ivCheck.setColorFilter(
                if (widgetInstance.textColor == 0)
                    context.resources.getColor(R.color.airrobe_widget_default_text_color)
                else
                    widgetInstance.textColor
            )
            binding.ivPlant1.setColorFilter(
                if (widgetInstance.textColor == 0)
                    context.resources.getColor(R.color.airrobe_widget_default_text_color)
                else
                    widgetInstance.textColor
            )
            binding.ivEmoji.setColorFilter(
                if (widgetInstance.textColor == 0)
                    context.resources.getColor(R.color.airrobe_widget_default_text_color)
                else
                    widgetInstance.textColor
            )
            binding.ivPlant2.setColorFilter(
                if (widgetInstance.textColor == 0)
                    context.resources.getColor(R.color.airrobe_widget_default_text_color)
                else
                    widgetInstance.textColor
            )

            binding.tvFindOutMore.setLinkTextColor(
                if (widgetInstance.linkTextColor == 0)
                    context.resources.getColor(R.color.airrobe_widget_default_link_text_color)
                else
                    widgetInstance.linkTextColor
            )
        }
        setSwitchColor()
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
        val trackColors = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M)
            intArrayOf(
                if (widgetInstance.borderColor == 0) context.getColor(R.color.airrobe_widget_default_border_color) else widgetInstance.borderColor,
                if (widgetInstance.switchColor == 0) context.getColor(R.color.airrobe_widget_default_switch_color) else widgetInstance.switchColor,
            ) else
            intArrayOf(
                if (widgetInstance.borderColor == 0) context.resources.getColor(R.color.airrobe_widget_default_border_color) else widgetInstance.borderColor,
                if (widgetInstance.switchColor == 0) context.resources.getColor(R.color.airrobe_widget_default_switch_color) else widgetInstance.switchColor,
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