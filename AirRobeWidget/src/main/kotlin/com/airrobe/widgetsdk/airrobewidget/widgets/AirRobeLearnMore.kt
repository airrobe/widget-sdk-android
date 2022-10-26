package com.airrobe.widgetsdk.airrobewidget.widgets

import android.annotation.SuppressLint
import android.app.Dialog
import android.content.Context
import android.content.res.ColorStateList
import android.graphics.drawable.GradientDrawable
import android.graphics.drawable.LayerDrawable
import android.graphics.drawable.StateListDrawable
import android.os.Bundle
import android.text.method.LinkMovementMethod
import android.view.*
import android.widget.*
import com.airrobe.widgetsdk.airrobewidget.R
import com.airrobe.widgetsdk.airrobewidget.config.AirRobeVariants
import com.airrobe.widgetsdk.airrobewidget.config.EventName
import com.airrobe.widgetsdk.airrobewidget.config.PageName
import com.airrobe.widgetsdk.airrobewidget.config.TelemetryEventName
import com.airrobe.widgetsdk.airrobewidget.utils.AirRobeAppUtils
import com.airrobe.widgetsdk.airrobewidget.utils.AirRobeSharedPreferenceManager
import com.airrobe.widgetsdk.airrobewidget.widgetInstance

internal class AirRobeLearnMore(context: Context) : Dialog(context) {

    // views for "default" variant
    private lateinit var tvStep1Description: TextView

    private lateinit var tvQuestion: TextView
    private lateinit var tvAnswer: TextView
    private lateinit var tvReady: TextView
    private lateinit var tvToggleOn: TextView
    private lateinit var ivPlant1: ImageView
    private lateinit var ivPlant2: ImageView

    private lateinit var rlStep1Container: RelativeLayout
    private lateinit var rlStep2Container: RelativeLayout
    private lateinit var rlToggleContainer: RelativeLayout
    private lateinit var step1Divider: View
    private lateinit var step2Divider: View

    // views for "enhanced" variant
    private lateinit var tvSubTitle: TextView
    private lateinit var tvHowItWorksTitle: TextView
    private lateinit var tvStep3Title: TextView
    private lateinit var tvStep3Description: TextView

    private lateinit var llOptInContainer: LinearLayout
    private lateinit var tvOptInTitle: TextView
    private lateinit var tvOptInDescription: TextView

    private lateinit var tvClose: TextView

    // views for both variants
    private lateinit var tvTitle: TextView
    private lateinit var ivClose: ImageView
    private lateinit var tvStep1Title: TextView
    private lateinit var tvStep2Title: TextView
    private lateinit var tvStep2Description: TextView
    private lateinit var divider: View
    private lateinit var tvFindOutMore: TextView
    private lateinit var optInSwitch: Switch

    // Others
    lateinit var optInSwitchFromOptInWidget: Switch
    var isFromMultiOptIn: Boolean = false

    private val testVariant = widgetInstance.shopModel?.getSplitTestVariant(context)

    init {
        setCancelable(true)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        requestWindowFeature(Window.FEATURE_NO_TITLE)
        when (testVariant?.splitTestVariant) {
            AirRobeVariants.Enhanced.raw -> {
                setContentView(R.layout.airrobe_dialog_learn_more_enhanced)
                tvSubTitle = findViewById(R.id.tv_sub_title)
                tvHowItWorksTitle = findViewById(R.id.tv_how_it_works_title)
                tvStep3Title = findViewById(R.id.tv_step3_title)
                tvStep3Description = findViewById(R.id.tv_step3_description)

                llOptInContainer = findViewById(R.id.ll_opt_in_container)
                tvOptInTitle = findViewById(R.id.tv_opt_in_title)
                tvOptInDescription = findViewById(R.id.tv_opt_in_description)
                tvClose = findViewById(R.id.tv_close)
            }
            else -> {
                setContentView(R.layout.airrobe_dialog_learn_more_default)
                tvStep1Description = findViewById(R.id.tv_step1_description)

                tvQuestion = findViewById(R.id.tv_question)
                tvAnswer = findViewById(R.id.tv_answer)
                tvReady = findViewById(R.id.tv_ready)
                tvToggleOn = findViewById(R.id.tv_toggle_on)

                ivPlant1 = findViewById(R.id.iv_plant1)
                ivPlant2 = findViewById(R.id.iv_plant2)
                rlStep1Container = findViewById(R.id.rl_step1_container)
                rlStep2Container = findViewById(R.id.rl_step2_container)
                rlToggleContainer = findViewById(R.id.rl_toggle_container)
                step1Divider = findViewById(R.id.step1_divider)
                step2Divider = findViewById(R.id.step2_divider)
            }
        }

        tvTitle = findViewById(R.id.tv_title)
        ivClose = findViewById(R.id.iv_close)
        tvStep1Title = findViewById(R.id.tv_step1_title)
        tvStep2Title = findViewById(R.id.tv_step2_title)
        tvStep2Description = findViewById(R.id.tv_step2_description)

        divider = findViewById(R.id.divider)
        tvFindOutMore = findViewById(R.id.tv_find_out_more)

        optInSwitch = findViewById(R.id.opt_in_switch)

        initView()
        onClickActions()
    }

    private fun initView() {
        optInSwitch.isChecked = AirRobeSharedPreferenceManager.getOptedIn(context)
        if (optInSwitch.isChecked) {
            tvOptInTitle.text = context.resources.getString(R.string.airrobe_added_to)
        } else {
            tvOptInTitle.text = context.resources.getString(R.string.airrobe_add_to)
        }
        optInSwitch.setOnCheckedChangeListener { _, isChecked ->
            AirRobeSharedPreferenceManager.setOptedIn(context, isChecked)
            optInSwitchFromOptInWidget.isChecked = isChecked
            if (isFromMultiOptIn) {
                AirRobeSharedPreferenceManager.setOrderOptedIn(context, isChecked)
            }
            if (isChecked) {
                tvOptInTitle.text = context.resources.getString(R.string.airrobe_added_to)
                if (isFromMultiOptIn) {
                    AirRobeAppUtils.telemetryEvent(context, TelemetryEventName.OptIn.raw, PageName.Cart.raw)
                    AirRobeAppUtils.dispatchEvent(context, EventName.OptIn.raw, PageName.Cart.raw)
                } else {
                    AirRobeAppUtils.telemetryEvent(context, TelemetryEventName.OptIn.raw, PageName.Product.raw)
                    AirRobeAppUtils.dispatchEvent(context, EventName.OptIn.raw, PageName.Product.raw)
                }
            } else {
                tvOptInTitle.text = context.resources.getString(R.string.airrobe_add_to)
                if (isFromMultiOptIn) {
                    AirRobeAppUtils.telemetryEvent(context, TelemetryEventName.OptOut.raw, PageName.Cart.raw)
                    AirRobeAppUtils.dispatchEvent(context, EventName.OptOut.raw, PageName.Cart.raw)
                } else {
                    AirRobeAppUtils.telemetryEvent(context, TelemetryEventName.OptOut.raw, PageName.Product.raw)
                    AirRobeAppUtils.dispatchEvent(context, EventName.OptOut.raw, PageName.Product.raw)
                }
            }
        }

        var findOutMoreText =
            if (testVariant?.splitTestVariant.equals(AirRobeVariants.Enhanced.raw))
                context.resources.getString(R.string.airrobe_learn_more_text_enhanced)
            else
                context.resources.getString(R.string.airrobe_learn_more_find_more_text)
        findOutMoreText = findOutMoreText.replace(findOutMoreText, "<a href='${widgetInstance.shopModel?.data?.shop?.popupFindOutMoreUrl}'>$findOutMoreText</a>")
        tvFindOutMore.text = AirRobeAppUtils.fromHtml(findOutMoreText)
        tvFindOutMore.movementMethod = LinkMovementMethod.getInstance()

        initColorSet()
    }

    private fun initColorSet() {
        if (testVariant?.splitTestVariant.equals(AirRobeVariants.Enhanced.raw)) {
            divider.setBackgroundColor(
                if (widgetInstance.separatorColor == 0)
                    AirRobeAppUtils.getColor(context, R.color.airrobe_widget_enhanced_separator_color)
                else
                    widgetInstance.separatorColor
            )

            tvTitle.setTextColor(
                if (widgetInstance.textColor == 0)
                    AirRobeAppUtils.getColor(context,R.color.airrobe_widget_enhanced_text_color)
                else
                    widgetInstance.textColor
            )
            tvSubTitle.setTextColor(
                if (widgetInstance.textColor == 0)
                    AirRobeAppUtils.getColor(context,R.color.airrobe_widget_enhanced_text_color)
                else
                    widgetInstance.textColor
            )
            tvHowItWorksTitle.setTextColor(
                if (widgetInstance.textColor == 0)
                    AirRobeAppUtils.getColor(context,R.color.airrobe_widget_enhanced_text_color)
                else
                    widgetInstance.textColor
            )

            tvStep1Title.setTextColor(
                if (widgetInstance.textColor == 0)
                    AirRobeAppUtils.getColor(context,R.color.airrobe_widget_enhanced_text_color)
                else
                    widgetInstance.textColor
            )
            tvStep2Title.setTextColor(
                if (widgetInstance.textColor == 0)
                    AirRobeAppUtils.getColor(context,R.color.airrobe_widget_enhanced_text_color)
                else
                    widgetInstance.textColor
            )
            tvStep2Description.setTextColor(
                if (widgetInstance.textColor == 0)
                    AirRobeAppUtils.getColor(context,R.color.airrobe_widget_enhanced_text_color)
                else
                    widgetInstance.textColor
            )
            tvStep3Title.setTextColor(
                if (widgetInstance.textColor == 0)
                    AirRobeAppUtils.getColor(context,R.color.airrobe_widget_enhanced_text_color)
                else
                    widgetInstance.textColor
            )
            tvStep3Description.setTextColor(
                if (widgetInstance.textColor == 0)
                    AirRobeAppUtils.getColor(context,R.color.airrobe_widget_enhanced_text_color)
                else
                    widgetInstance.textColor
            )

            llOptInContainer.setBackgroundColor(
                if (widgetInstance.popupSwitchContainerBackgroundColor == 0)
                    AirRobeAppUtils.getColor(context,R.color.airrobe_widget_enhanced_popup_switch_container_background_color)
                else
                    widgetInstance.popupSwitchContainerBackgroundColor
            )
            tvOptInTitle.setTextColor(
                if (widgetInstance.textColor == 0)
                    AirRobeAppUtils.getColor(context,R.color.airrobe_widget_enhanced_text_color)
                else
                    widgetInstance.textColor
            )
            tvOptInDescription.setTextColor(
                if (widgetInstance.textColor == 0)
                    AirRobeAppUtils.getColor(context,R.color.airrobe_widget_enhanced_text_color)
                else
                    widgetInstance.textColor
            )

            ivClose.setColorFilter(
                if (widgetInstance.textColor == 0)
                    AirRobeAppUtils.getColor(context,R.color.airrobe_widget_enhanced_text_color)
                else
                    widgetInstance.textColor
            )

            tvFindOutMore.setLinkTextColor(
                if (widgetInstance.linkTextColor == 0)
                    AirRobeAppUtils.getColor(context, R.color.airrobe_widget_enhanced_link_text_color)
                else
                    widgetInstance.linkTextColor
            )
            tvClose.setTextColor(
                if (widgetInstance.textColor == 0)
                    AirRobeAppUtils.getColor(context,R.color.airrobe_widget_enhanced_text_color)
                else
                    widgetInstance.textColor
            )
        } else {
            val step1Background = rlStep1Container.background as GradientDrawable
            step1Background.setStroke(1,
                if (widgetInstance.borderColor == 0)
                    AirRobeAppUtils.getColor(context, R.color.airrobe_widget_default_border_color)
                else
                    widgetInstance.borderColor
            )
            val step2Background = rlStep2Container.background as GradientDrawable
            step2Background.setStroke(1,
                if (widgetInstance.borderColor == 0)
                    AirRobeAppUtils.getColor(context, R.color.airrobe_widget_default_border_color)
                else
                    widgetInstance.borderColor
            )
            val toggleBackground = rlToggleContainer.background as GradientDrawable
            toggleBackground.setStroke(1,
                if (widgetInstance.borderColor == 0)
                    AirRobeAppUtils.getColor(context, R.color.airrobe_widget_default_border_color)
                else
                    widgetInstance.borderColor
            )

            divider.setBackgroundColor(
                if (widgetInstance.separatorColor == 0)
                    AirRobeAppUtils.getColor(context, R.color.airrobe_widget_default_separator_color)
                else
                    widgetInstance.separatorColor
            )
            step1Divider.setBackgroundColor(
                if (widgetInstance.separatorColor == 0)
                    AirRobeAppUtils.getColor(context ,R.color.airrobe_widget_default_separator_color)
                else
                    widgetInstance.separatorColor
            )
            step2Divider.setBackgroundColor(
                if (widgetInstance.separatorColor == 0)
                    AirRobeAppUtils.getColor(context, R.color.airrobe_widget_default_separator_color)
                else
                    widgetInstance.separatorColor
            )

            tvTitle.setTextColor(
                if (widgetInstance.textColor == 0)
                    AirRobeAppUtils.getColor(context, R.color.airrobe_widget_default_text_color)
                else
                    widgetInstance.textColor
            )
            tvStep1Title.setTextColor(
                if (widgetInstance.textColor == 0)
                    AirRobeAppUtils.getColor(context, R.color.airrobe_widget_default_text_color)
                else
                    widgetInstance.textColor
            )
            tvStep1Description.setTextColor(
                if (widgetInstance.textColor == 0)
                    AirRobeAppUtils.getColor(context, R.color.airrobe_widget_default_text_color)
                else
                    widgetInstance.textColor
            )
            tvStep2Title.setTextColor(
                if (widgetInstance.textColor == 0)
                    AirRobeAppUtils.getColor(context, R.color.airrobe_widget_default_text_color)
                else
                    widgetInstance.textColor
            )
            tvStep2Description.setTextColor(
                if (widgetInstance.textColor == 0)
                    AirRobeAppUtils.getColor(context, R.color.airrobe_widget_default_text_color)
                else
                    widgetInstance.textColor
            )
            tvQuestion.setTextColor(
                if (widgetInstance.textColor == 0)
                    AirRobeAppUtils.getColor(context, R.color.airrobe_widget_default_text_color)
                else
                    widgetInstance.textColor
            )
            tvAnswer.setTextColor(
                if (widgetInstance.textColor == 0)
                    AirRobeAppUtils.getColor(context, R.color.airrobe_widget_default_text_color)
                else
                    widgetInstance.textColor
            )
            tvReady.setTextColor(
                if (widgetInstance.textColor == 0)
                    AirRobeAppUtils.getColor(context, R.color.airrobe_widget_default_text_color)
                else
                    widgetInstance.textColor
            )
            tvToggleOn.setTextColor(
                if (widgetInstance.textColor == 0)
                    AirRobeAppUtils.getColor(context, R.color.airrobe_widget_default_text_color)
                else
                    widgetInstance.textColor
            )

            ivClose.setColorFilter(
                if (widgetInstance.textColor == 0)
                    AirRobeAppUtils.getColor(context, R.color.airrobe_widget_default_text_color)
                else
                    widgetInstance.textColor
            )
            ivPlant1.setColorFilter(
                if (widgetInstance.textColor == 0)
                    AirRobeAppUtils.getColor(context, R.color.airrobe_widget_default_text_color)
                else
                    widgetInstance.textColor
            )
            ivPlant2.setColorFilter(
                if (widgetInstance.textColor == 0)
                    AirRobeAppUtils.getColor(context, R.color.airrobe_widget_default_text_color)
                else
                    widgetInstance.textColor
            )

            tvFindOutMore.setLinkTextColor(
                if (widgetInstance.linkTextColor == 0)
                    AirRobeAppUtils.getColor(context, R.color.airrobe_widget_default_link_text_color)
                else
                    widgetInstance.linkTextColor
            )
        }

        setSwitchColor()
    }

    private fun setSwitchColor() =
        if (testVariant?.splitTestVariant.equals(AirRobeVariants.Enhanced.raw)) {
            val thumbOffLayerDrawable: LayerDrawable = context.getDrawable(R.drawable.airrobe_switch_thumb_enhanced_false) as LayerDrawable
            val thumbOffGradientDrawable: GradientDrawable = thumbOffLayerDrawable.findDrawableByLayerId(R.id.airrobe_switch_thumb_off_layer_enhanced) as GradientDrawable
            thumbOffGradientDrawable.setColor(widgetInstance.switchThumbOffColor)

            val thumbOnLayerDrawable: LayerDrawable = context.getDrawable(R.drawable.airrobe_switch_thumb_enhanced_true) as LayerDrawable
            val thumbOnGradientDrawable: GradientDrawable = thumbOnLayerDrawable.findDrawableByLayerId(R.id.airrobe_switch_thumb_on_layer_enhanced) as GradientDrawable
            thumbOnGradientDrawable.setColor(widgetInstance.switchThumbOnColor)

            val thumbLayerDrawable: StateListDrawable = context.getDrawable(R.drawable.airrobe_switch_thumb_enhanced) as StateListDrawable
            thumbLayerDrawable.addState(intArrayOf(-android.R.attr.state_checked), thumbOffLayerDrawable)
            thumbLayerDrawable.addState(intArrayOf(android.R.attr.state_checked), thumbOnLayerDrawable)

            val trackOffLayerDrawable: LayerDrawable = context.getDrawable(R.drawable.airrobe_switch_track_off_enhanced) as LayerDrawable
            val trackOffGradientDrawable: GradientDrawable = trackOffLayerDrawable.findDrawableByLayerId(R.id.airrobe_switch_track_off_layer_enhanced) as GradientDrawable
            trackOffGradientDrawable.setColor(widgetInstance.switchOffColor)

            val trackOnLayerDrawable: LayerDrawable = context.getDrawable(R.drawable.airrobe_switch_track_on_enhanced) as LayerDrawable
            val trackOnGradientDrawable: GradientDrawable = trackOnLayerDrawable.findDrawableByLayerId(R.id.airrobe_switch_track_on_layer_enhanced) as GradientDrawable
            trackOnGradientDrawable.setColor(widgetInstance.switchOnColor)

            val trackLayerDrawable: StateListDrawable = context.getDrawable(R.drawable.airrobe_switch_track_enhanced) as StateListDrawable
            trackLayerDrawable.addState(intArrayOf(-android.R.attr.state_checked), trackOffLayerDrawable)
            trackLayerDrawable.addState(intArrayOf(android.R.attr.state_checked), trackOnLayerDrawable)

            optInSwitch.thumbDrawable = thumbLayerDrawable
            optInSwitch.trackDrawable = trackLayerDrawable
        } else {
            val states = arrayOf(
                intArrayOf(-android.R.attr.state_checked),
                intArrayOf(android.R.attr.state_checked)
            )
            val thumbColors = intArrayOf(
                widgetInstance.switchThumbOffColor,
                widgetInstance.switchThumbOnColor
            )
            val trackColors = intArrayOf(
                widgetInstance.switchOffColor,
                widgetInstance.switchOnColor
            )
            optInSwitch.thumbDrawable.setTintList(ColorStateList(states, thumbColors))
            optInSwitch.trackDrawable.setTintList(ColorStateList(states, trackColors))
        }

    @SuppressLint("ClickableViewAccessibility")
    private fun onClickActions() {
        ivClose.setOnTouchListener { v, event ->
            if (AirRobeAppUtils.touchAnimator(context, v, event)) {
                dismiss()
            }
            true
        }

        tvClose.setOnTouchListener { v, event ->
            if (AirRobeAppUtils.touchAnimator(context, v, event)) {
                dismiss()
            }
            true
        }
    }

    override fun dismiss() {
        super.dismiss()
        if (isFromMultiOptIn) {
            AirRobeAppUtils.dispatchEvent(context, EventName.PopupClose.raw, PageName.Cart.raw)
        } else {
            AirRobeAppUtils.dispatchEvent(context, EventName.PopupClose.raw, PageName.Product.raw)
        }
    }
}