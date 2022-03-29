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
import android.widget.*
import com.airrobe.widgetsdk.airrobewidget.R
import com.airrobe.widgetsdk.airrobewidget.utils.AirRobeAppUtils
import com.airrobe.widgetsdk.airrobewidget.utils.AirRobeSharedPreferenceManager
import com.airrobe.widgetsdk.airrobewidget.widgetInstance

internal class AirRobeLearnMore(context: Context) : Dialog(context) {
    private lateinit var optInSwitch: Switch
    private lateinit var tvFindOutMore: TextView
    private lateinit var rlStep1Container: RelativeLayout
    private lateinit var rlStep2Container: RelativeLayout
    private lateinit var rlToggleContainer: RelativeLayout
    private lateinit var step1Divider: View
    private lateinit var step2Divider: View
    private lateinit var divider: View
    private lateinit var tvTitle: TextView
    private lateinit var tvStep1Title: TextView
    private lateinit var tvStep1Description: TextView
    private lateinit var tvStep2Title: TextView
    private lateinit var tvStep2Description: TextView
    private lateinit var tvQuestion: TextView
    private lateinit var tvAnswer: TextView
    private lateinit var tvReady: TextView
    private lateinit var tvToggleOn: TextView
    private lateinit var ivClose: ImageView
    private lateinit var ivCheck: ImageView
    private lateinit var ivPlant1: ImageView
    private lateinit var ivEmoji: ImageView
    private lateinit var ivPlant2: ImageView
    lateinit var optInSwitchFromOptInWidget: Switch
    var isFromMultiOptIn: Boolean = false

    init {
        setCancelable(true)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        requestWindowFeature(Window.FEATURE_NO_TITLE)
        setContentView(R.layout.airrobe_dialog_learn_more)

        optInSwitch = findViewById(R.id.opt_in_switch)
        tvFindOutMore = findViewById(R.id.tv_find_out_more)
        rlStep1Container = findViewById(R.id.rl_step1_container)
        rlStep2Container = findViewById(R.id.rl_step2_container)
        rlToggleContainer = findViewById(R.id.rl_toggle_container)
        step1Divider = findViewById(R.id.step1_divider)
        step2Divider = findViewById(R.id.step2_divider)
        divider = findViewById(R.id.divider)
        tvTitle = findViewById(R.id.tv_title)
        tvStep1Title = findViewById(R.id.tv_step1_title)
        tvStep1Description = findViewById(R.id.tv_step1_description)
        tvStep2Title = findViewById(R.id.tv_step2_title)
        tvStep2Description = findViewById(R.id.tv_step2_description)
        tvQuestion = findViewById(R.id.tv_question)
        tvAnswer = findViewById(R.id.tv_answer)
        tvReady = findViewById(R.id.tv_ready)
        tvToggleOn = findViewById(R.id.tv_toggle_on)
        ivClose = findViewById(R.id.iv_close)
        ivCheck = findViewById(R.id.iv_check)
        ivPlant1 = findViewById(R.id.iv_plant1)
        ivEmoji = findViewById(R.id.iv_emoji)
        ivPlant2 = findViewById(R.id.iv_plant2)

        initView()
        onClick()
    }

    private fun initView() {
        optInSwitch.isChecked = AirRobeSharedPreferenceManager.getOptedIn(context)
        optInSwitch.setOnCheckedChangeListener { _, isChecked ->
            AirRobeSharedPreferenceManager.setOptedIn(context, isChecked)
            optInSwitchFromOptInWidget.isChecked = isChecked
            if (isFromMultiOptIn) {
                AirRobeSharedPreferenceManager.setOrderOptedIn(context, isChecked)
            }
        }

        tvFindOutMore.movementMethod = LinkMovementMethod.getInstance()
        initColorSet()
    }

    private fun initColorSet() {
        val step1Background = rlStep1Container.background as GradientDrawable
        step1Background.setStroke(1,
            if (widgetInstance.borderColor == 0)
                context.resources.getColor(R.color.airrobe_widget_default_border_color)
            else
                widgetInstance.borderColor
        )
        val step2Background = rlStep2Container.background as GradientDrawable
        step2Background.setStroke(1,
            if (widgetInstance.borderColor == 0)
                context.resources.getColor(R.color.airrobe_widget_default_border_color)
            else
                widgetInstance.borderColor
        )
        val toggleBackground = rlToggleContainer.background as GradientDrawable
        toggleBackground.setStroke(1,
            if (widgetInstance.borderColor == 0)
                context.resources.getColor(R.color.airrobe_widget_default_border_color)
            else
                widgetInstance.borderColor
        )

        divider.setBackgroundColor(
            if (widgetInstance.separatorColor == 0)
                context.resources.getColor(R.color.airrobe_widget_default_separator_color)
            else
                widgetInstance.separatorColor
        )
        step1Divider.setBackgroundColor(
            if (widgetInstance.separatorColor == 0)
                context.resources.getColor(R.color.airrobe_widget_default_separator_color)
            else
                widgetInstance.separatorColor
        )
        step2Divider.setBackgroundColor(
            if (widgetInstance.separatorColor == 0)
                context.resources.getColor(R.color.airrobe_widget_default_separator_color)
            else
                widgetInstance.separatorColor
        )

        tvTitle.setTextColor(
            if (widgetInstance.textColor == 0)
                context.resources.getColor(R.color.airrobe_widget_default_text_color)
            else
                widgetInstance.textColor
        )
        tvStep1Title.setTextColor(
            if (widgetInstance.textColor == 0)
                context.resources.getColor(R.color.airrobe_widget_default_text_color)
            else
                widgetInstance.textColor
        )
        tvStep1Description.setTextColor(
            if (widgetInstance.textColor == 0)
                context.resources.getColor(R.color.airrobe_widget_default_text_color)
            else
                widgetInstance.textColor
        )
        tvStep2Title.setTextColor(
            if (widgetInstance.textColor == 0)
                context.resources.getColor(R.color.airrobe_widget_default_text_color)
            else
                widgetInstance.textColor
        )
        tvStep2Description.setTextColor(
            if (widgetInstance.textColor == 0)
                context.resources.getColor(R.color.airrobe_widget_default_text_color)
            else
                widgetInstance.textColor
        )
        tvQuestion.setTextColor(
            if (widgetInstance.textColor == 0)
                context.resources.getColor(R.color.airrobe_widget_default_text_color)
            else
                widgetInstance.textColor
        )
        tvAnswer.setTextColor(
            if (widgetInstance.textColor == 0)
                context.resources.getColor(R.color.airrobe_widget_default_text_color)
            else
                widgetInstance.textColor
        )
        tvReady.setTextColor(
            if (widgetInstance.textColor == 0)
                context.resources.getColor(R.color.airrobe_widget_default_text_color)
            else
                widgetInstance.textColor
        )
        tvToggleOn.setTextColor(
            if (widgetInstance.textColor == 0)
                context.resources.getColor(R.color.airrobe_widget_default_text_color)
            else
                widgetInstance.textColor
        )

        ivClose.setColorFilter(
            if (widgetInstance.textColor == 0)
                context.resources.getColor(R.color.airrobe_widget_default_text_color)
            else
                widgetInstance.textColor
        )
        ivCheck.setColorFilter(
            if (widgetInstance.textColor == 0)
                context.resources.getColor(R.color.airrobe_widget_default_text_color)
            else
                widgetInstance.textColor
        )
        ivPlant1.setColorFilter(
            if (widgetInstance.textColor == 0)
                context.resources.getColor(R.color.airrobe_widget_default_text_color)
            else
                widgetInstance.textColor
        )
        ivEmoji.setColorFilter(
            if (widgetInstance.textColor == 0)
                context.resources.getColor(R.color.airrobe_widget_default_text_color)
            else
                widgetInstance.textColor
        )
        ivPlant2.setColorFilter(
            if (widgetInstance.textColor == 0)
                context.resources.getColor(R.color.airrobe_widget_default_text_color)
            else
                widgetInstance.textColor
        )

        tvFindOutMore.setLinkTextColor(
            if (widgetInstance.linkTextColor == 0)
                context.resources.getColor(R.color.airrobe_widget_default_link_text_color)
            else
                widgetInstance.linkTextColor
        )
        setSwitchColor()
    }

    private fun setSwitchColor() {
        val states = arrayOf(
            intArrayOf(-android.R.attr.state_checked),
            intArrayOf(android.R.attr.state_checked)
        )
        val thumbColors = intArrayOf(
            Color.WHITE,
            if (widgetInstance.switchColor == 0) context.resources.getColor(R.color.airrobe_widget_default_switch_color) else widgetInstance.switchColor,
        )
        val trackColors = intArrayOf(
            if (widgetInstance.borderColor == 0) context.resources.getColor(R.color.airrobe_widget_default_border_color) else widgetInstance.borderColor,
            if (widgetInstance.switchColor == 0) context.resources.getColor(R.color.airrobe_widget_default_switch_color) else widgetInstance.switchColor,
        )
        optInSwitch.thumbDrawable.setTintList(ColorStateList(states, thumbColors))
        optInSwitch.trackDrawable.setTintList(ColorStateList(states, trackColors))
    }

    @SuppressLint("ClickableViewAccessibility")
    private fun onClick() {
        ivClose.setOnTouchListener { v, event ->
            if (AirRobeAppUtils.touchAnimator(context, v, event)) {
                dismiss()
            }
            true
        }
    }
}