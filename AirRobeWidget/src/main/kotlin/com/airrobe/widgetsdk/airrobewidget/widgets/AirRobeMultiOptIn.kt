package com.airrobe.widgetsdk.airrobewidget.widgets

import android.content.Context
import android.content.res.ColorStateList
import android.graphics.Color
import android.graphics.PorterDuff
import android.graphics.drawable.ColorDrawable
import android.graphics.drawable.GradientDrawable
import android.graphics.drawable.LayerDrawable
import android.graphics.drawable.StateListDrawable
import android.text.SpannableString
import android.text.method.LinkMovementMethod
import android.text.style.ClickableSpan
import android.util.AttributeSet
import android.util.Log
import com.airrobe.widgetsdk.airrobewidget.R
import com.airrobe.widgetsdk.airrobewidget.widgetInstance
import android.text.TextPaint

import android.text.Spanned
import android.view.View
import android.webkit.URLUtil
import android.widget.*
import com.airrobe.widgetsdk.airrobewidget.config.*
import com.airrobe.widgetsdk.airrobewidget.config.AirRobeVariants
import com.airrobe.widgetsdk.airrobewidget.config.AirRobeWidgetInstance
import com.airrobe.widgetsdk.airrobewidget.utils.AirRobeAppUtils
import com.airrobe.widgetsdk.airrobewidget.utils.AirRobeSharedPreferenceManager

class AirRobeMultiOptIn @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : LinearLayout(context, attrs, defStyleAttr) {
    private var llMainContainer: LinearLayout
    private var optInSwitch: Switch
    private var llSwitchContainer: LinearLayout
    private var tvTitle: TextView
    private var tvDescription: TextView
    private var tvDetailedDescription: TextView
    private lateinit var tvExtraInfo: TextView
    private var ivArrowDown: ImageView

    companion object {
        private const val TAG = "AirRobeMultiOptIn"
    }

    internal enum class ExpandType {
        Opened,
        Closed
    }

    private val testVariant = widgetInstance.shopModel?.getSplitTestVariant(context)

    private var expandType: ExpandType = ExpandType.Closed
    private var items: ArrayList<String> = arrayListOf()

    var borderColor: Int =
        if (widgetInstance.borderColor == 0)
            AirRobeAppUtils.getColor(
                context,
                if (testVariant?.splitTestVariant.equals(AirRobeVariants.Enhanced.raw))
                    R.color.airrobe_widget_enhanced_border_color
                else
                    R.color.airrobe_widget_default_border_color
            )
        else
            widgetInstance.borderColor
        set(value) {
            field = value
            val mainBackground = llMainContainer.background as GradientDrawable
            mainBackground.setStroke(1, value)
        }

    var textColor: Int =
        if (widgetInstance.textColor == 0)
            AirRobeAppUtils.getColor(
                context,
                if (testVariant?.splitTestVariant.equals(AirRobeVariants.Enhanced.raw))
                    R.color.airrobe_widget_enhanced_text_color
                else
                    R.color.airrobe_widget_default_text_color
            )
        else
            widgetInstance.textColor
        set(value) {
            field = value
            tvTitle.setTextColor(value)
            tvDescription.setTextColor(value)
            tvDetailedDescription.setTextColor(value)
            if (testVariant?.splitTestVariant.equals(AirRobeVariants.Default.raw)) {
                tvExtraInfo.setTextColor(value)
            }
        }

    var switchOnColor: Int =
        if (widgetInstance.switchOnColor == 0)
            AirRobeAppUtils.getColor(
                context,
                if (testVariant?.splitTestVariant.equals(AirRobeVariants.Enhanced.raw))
                    R.color.airrobe_widget_enhanced_switch_on_color
                else
                    R.color.airrobe_widget_default_switch_on_color
            )
        else
            widgetInstance.switchOnColor
        set(value) {
            field = value
            setSwitchColor()
        }

    var switchOffColor: Int =
        if (widgetInstance.switchOffColor == 0)
            AirRobeAppUtils.getColor(
                context,
                if (testVariant?.splitTestVariant.equals(AirRobeVariants.Enhanced.raw))
                    R.color.airrobe_widget_enhanced_switch_off_color
                else
                    R.color.airrobe_widget_default_switch_off_color
            )
        else
            widgetInstance.switchOffColor
        set(value) {
            field = value
            setSwitchColor()
        }

    var switchThumbOnColor: Int =
        if (widgetInstance.switchThumbOnColor == 0)
            AirRobeAppUtils.getColor(
                context,
                if (testVariant?.splitTestVariant.equals(AirRobeVariants.Enhanced.raw))
                    R.color.airrobe_widget_enhanced_switch_thumb_on_color
                else
                    R.color.airrobe_widget_default_switch_thumb_on_color
            )
        else
            widgetInstance.switchThumbOnColor
        set(value) {
            field = value
            setSwitchColor()
        }

    var switchThumbOffColor: Int =
        if (widgetInstance.switchThumbOffColor == 0)
            AirRobeAppUtils.getColor(
                context,
                if (testVariant?.splitTestVariant.equals(AirRobeVariants.Enhanced.raw))
                    R.color.airrobe_widget_enhanced_switch_thumb_off_color
                else
                    R.color.airrobe_widget_default_switch_thumb_off_color
            )
        else
            widgetInstance.switchThumbOffColor
        set(value) {
            field = value
            setSwitchColor()
        }

    var arrowColor: Int =
        if (widgetInstance.arrowColor == 0)
            AirRobeAppUtils.getColor(
                context,
                if (testVariant?.splitTestVariant.equals(AirRobeVariants.Enhanced.raw))
                    R.color.airrobe_widget_enhanced_arrow_color
                else
                    R.color.airrobe_widget_default_arrow_color
            )
        else
            widgetInstance.arrowColor
        set(value) {
            field = value
            ivArrowDown.setColorFilter(value, PorterDuff.Mode.SRC_ATOP)
        }

    var linkTextColor: Int =
        if (widgetInstance.linkTextColor == 0)
            AirRobeAppUtils.getColor(
                context,
                if (testVariant?.splitTestVariant.equals(AirRobeVariants.Enhanced.raw))
                    R.color.airrobe_widget_enhanced_link_text_color
                else
                    R.color.airrobe_widget_default_link_text_color
            )
        else
            widgetInstance.linkTextColor
        set(value) {
            field = value
            tvDetailedDescription.setLinkTextColor(value)
            if (testVariant?.splitTestVariant.equals(AirRobeVariants.Default.raw)) {
                tvExtraInfo.setLinkTextColor(value)
            }
        }

    init {
        when (testVariant?.splitTestVariant) {
            AirRobeVariants.Enhanced.raw -> {
                inflate(context, R.layout.airrobe_multi_opt_in_enhanced, this)
            }
            else -> {
                inflate(context, R.layout.airrobe_multi_opt_in_default, this)
                tvExtraInfo = findViewById(R.id.tv_extra_info)
            }
        }
        visibility = GONE

        llMainContainer = findViewById(R.id.ll_main_container)
        optInSwitch = findViewById(R.id.opt_in_switch)
        llSwitchContainer = findViewById(R.id.ll_switch_container)
        tvTitle = findViewById(R.id.tv_title)
        tvDescription = findViewById(R.id.tv_description)
        tvDetailedDescription = findViewById(R.id.tv_detailed_description)
        if (testVariant?.splitTestVariant.equals(AirRobeVariants.Default.raw)) {
            tvExtraInfo = findViewById(R.id.tv_extra_info)
        }
        ivArrowDown = findViewById(R.id.iv_arrow_down)

        val listener = object : AirRobeWidgetInstance.InstanceChangeListener {
            override fun onShopModelChange() {
                post {
                    initializeOptInWidget()
                }
            }

            override fun onConfigChange() {
                if (widgetInstance.configuration != null) {
                    initialize()
                }
            }
        }
        widgetInstance.changeListener = listener
        if (widgetInstance.configuration != null) {
            initialize()
        }

        setupAttributes(attrs)
    }

    private fun setupAttributes(attrs: AttributeSet?) {
        val typedArray = context.theme.obtainStyledAttributes(attrs, R.styleable.AirRobeMultiOptIn, 0, 0)
        borderColor =
            typedArray.getColor(R.styleable.AirRobeMultiOptIn_borderColor,
                if (widgetInstance.borderColor == 0)
                    AirRobeAppUtils.getColor(
                        context,
                        if (testVariant?.splitTestVariant.equals(AirRobeVariants.Enhanced.raw))
                            R.color.airrobe_widget_enhanced_border_color
                        else
                            R.color.airrobe_widget_default_border_color
                    )
                else
                    widgetInstance.borderColor
            )
        textColor =
            typedArray.getColor(R.styleable.AirRobeMultiOptIn_textColor,
                if (widgetInstance.textColor == 0)
                    AirRobeAppUtils.getColor(
                        context,
                        if (testVariant?.splitTestVariant.equals(AirRobeVariants.Enhanced.raw))
                            R.color.airrobe_widget_enhanced_text_color
                        else
                            R.color.airrobe_widget_default_text_color
                    )
                else
                    widgetInstance.textColor
            )
        switchOnColor =
            typedArray.getColor(R.styleable.AirRobeMultiOptIn_switchOnColor,
                if (widgetInstance.switchOnColor == 0)
                    AirRobeAppUtils.getColor(
                        context,
                        if (testVariant?.splitTestVariant.equals(AirRobeVariants.Enhanced.raw))
                            R.color.airrobe_widget_enhanced_switch_on_color
                        else
                            R.color.airrobe_widget_default_switch_on_color
                    )
                else
                    widgetInstance.switchOnColor
            )
        switchOffColor =
            typedArray.getColor(R.styleable.AirRobeMultiOptIn_switchOffColor,
                if (widgetInstance.switchOffColor == 0)
                    AirRobeAppUtils.getColor(
                        context,
                        if (testVariant?.splitTestVariant.equals(AirRobeVariants.Enhanced.raw))
                            R.color.airrobe_widget_enhanced_switch_off_color
                        else
                            R.color.airrobe_widget_default_switch_off_color
                    )
                else
                    widgetInstance.switchOffColor
            )
        switchThumbOnColor =
            typedArray.getColor(R.styleable.AirRobeMultiOptIn_switchThumbOnColor,
                if (widgetInstance.switchThumbOnColor == 0)
                    AirRobeAppUtils.getColor(
                        context,
                        if (testVariant?.splitTestVariant.equals(AirRobeVariants.Enhanced.raw))
                            R.color.airrobe_widget_enhanced_switch_thumb_on_color
                        else
                            R.color.airrobe_widget_default_switch_thumb_on_color
                    )
                else
                    widgetInstance.switchThumbOnColor
            )
        switchThumbOffColor =
            typedArray.getColor(R.styleable.AirRobeMultiOptIn_switchThumbOffColor,
                if (widgetInstance.switchThumbOffColor == 0)
                    AirRobeAppUtils.getColor(
                        context,
                        if (testVariant?.splitTestVariant.equals(AirRobeVariants.Enhanced.raw))
                            R.color.airrobe_widget_enhanced_switch_thumb_off_color
                        else
                            R.color.airrobe_widget_default_switch_thumb_off_color
                    )
                else
                    widgetInstance.switchThumbOffColor
            )
        arrowColor =
            typedArray.getColor(R.styleable.AirRobeMultiOptIn_arrowColor,
                if (widgetInstance.arrowColor == 0)
                    AirRobeAppUtils.getColor(
                        context,
                        if (testVariant?.splitTestVariant.equals(AirRobeVariants.Enhanced.raw))
                            R.color.airrobe_widget_enhanced_arrow_color
                        else
                            R.color.airrobe_widget_default_arrow_color
                    )
                else
                    widgetInstance.arrowColor
            )
        linkTextColor =
            typedArray.getColor(R.styleable.AirRobeMultiOptIn_linkTextColor,
                if (widgetInstance.linkTextColor == 0)
                    AirRobeAppUtils.getColor(
                        context,
                        if (testVariant?.splitTestVariant.equals(AirRobeVariants.Enhanced.raw))
                            R.color.airrobe_widget_enhanced_link_text_color
                        else
                            R.color.airrobe_widget_default_link_text_color
                    )
                else
                    widgetInstance.linkTextColor
            )
    }

    private fun setSwitchColor() =
        if (testVariant?.splitTestVariant.equals(AirRobeVariants.Enhanced.raw)) {
            val thumbOffLayerDrawable: LayerDrawable = context.getDrawable(R.drawable.airrobe_switch_thumb_enhanced_false) as LayerDrawable
            val thumbOffGradientDrawable: GradientDrawable = thumbOffLayerDrawable.findDrawableByLayerId(R.id.airrobe_switch_thumb_off_layer_enhanced) as GradientDrawable
            thumbOffGradientDrawable.setColor(switchThumbOffColor)

            val thumbOnLayerDrawable: LayerDrawable = context.getDrawable(R.drawable.airrobe_switch_thumb_enhanced_true) as LayerDrawable
            val thumbOnGradientDrawable: GradientDrawable = thumbOnLayerDrawable.findDrawableByLayerId(R.id.airrobe_switch_thumb_on_layer_enhanced) as GradientDrawable
            thumbOnGradientDrawable.setColor(switchThumbOnColor)

            val thumbLayerDrawable: StateListDrawable = context.getDrawable(R.drawable.airrobe_switch_thumb_enhanced) as StateListDrawable
            thumbLayerDrawable.addState(intArrayOf(-android.R.attr.state_checked), thumbOffLayerDrawable)
            thumbLayerDrawable.addState(intArrayOf(android.R.attr.state_checked), thumbOnLayerDrawable)

            val trackOffLayerDrawable: LayerDrawable = context.getDrawable(R.drawable.airrobe_switch_track_off_enhanced) as LayerDrawable
            val trackOffGradientDrawable: GradientDrawable = trackOffLayerDrawable.findDrawableByLayerId(R.id.airrobe_switch_track_off_layer_enhanced) as GradientDrawable
            trackOffGradientDrawable.setColor(switchOffColor)

            val trackOnLayerDrawable: LayerDrawable = context.getDrawable(R.drawable.airrobe_switch_track_on_enhanced) as LayerDrawable
            val trackOnGradientDrawable: GradientDrawable = trackOnLayerDrawable.findDrawableByLayerId(R.id.airrobe_switch_track_on_layer_enhanced) as GradientDrawable
            trackOnGradientDrawable.setColor(switchOnColor)

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
                switchThumbOffColor,
                switchThumbOnColor
            )
            val trackColors = intArrayOf(
                switchOffColor,
                switchOnColor
            )
            optInSwitch.thumbDrawable.setTintList(ColorStateList(states, thumbColors))
            optInSwitch.trackDrawable.setTintList(ColorStateList(states, trackColors))
        }

    private fun initialize() {
        tvDetailedDescription.visibility = GONE
        llSwitchContainer.setOnClickListener {
            if (expandType == ExpandType.Opened) {
                tvDetailedDescription.visibility = GONE
                expandType = ExpandType.Closed
                ivArrowDown.animate().rotation(0.0f).duration = 80
                AirRobeAppUtils.dispatchEvent(context, EventName.Collapse.raw, PageName.Cart.raw)
            } else {
                tvDetailedDescription.visibility = VISIBLE
                expandType = ExpandType.Opened
                ivArrowDown.animate().rotation(180.0f).duration = 80
                AirRobeAppUtils.telemetryEvent(context, TelemetryEventName.Expand.raw, PageName.Cart.raw)
                AirRobeAppUtils.dispatchEvent(context, EventName.Expand.raw, PageName.Cart.raw)
            }
        }
        setDetailedDescriptionText()
        if (testVariant?.splitTestVariant.equals(AirRobeVariants.Default.raw)) {
            setExtraInfoText()
        }
        optInSwitch.isChecked = AirRobeSharedPreferenceManager.getOptedIn(context)
        if (optInSwitch.isChecked) {
            tvTitle.text = context.resources.getString(R.string.airrobe_added_to)
        } else {
            tvTitle.text = context.resources.getString(R.string.airrobe_add_to)
        }
        optInSwitch.setOnCheckedChangeListener { _, isChecked ->
            AirRobeSharedPreferenceManager.setOptedIn(context, isChecked)
            AirRobeSharedPreferenceManager.setOrderOptedIn(context, isChecked)
            if (isChecked) {
                tvTitle.text = context.resources.getString(R.string.airrobe_added_to)
                AirRobeAppUtils.telemetryEvent(context, TelemetryEventName.OptIn.raw, PageName.Cart.raw)
                AirRobeAppUtils.dispatchEvent(context, EventName.OptIn.raw, PageName.Cart.raw)
            } else {
                tvTitle.text = context.resources.getString(R.string.airrobe_add_to)
                AirRobeAppUtils.telemetryEvent(context, TelemetryEventName.OptOut.raw, PageName.Cart.raw)
                AirRobeAppUtils.dispatchEvent(context, EventName.OptOut.raw, PageName.Cart.raw)
            }
        }
    }

    private fun setDetailedDescriptionText() {
        val detailedDescriptionText =
            if (testVariant?.splitTestVariant.equals(AirRobeVariants.Enhanced.raw))
                SpannableString(context.resources.getString(R.string.airrobe_detailed_description_enhanced))
            else
                SpannableString(context.resources.getString(R.string.airrobe_detailed_description))
        val cs = object : ClickableSpan() {
            override fun updateDrawState(ds: TextPaint) {
                super.updateDrawState(ds)
                ds.isUnderlineText = true
            }

            override fun onClick(p0: View) {
                val dialog = AirRobeLearnMore(context)
                dialog.optInSwitchFromOptInWidget = optInSwitch
                dialog.isFromMultiOptIn = true
                dialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
                dialog.show()
                AirRobeAppUtils.telemetryEvent(context, TelemetryEventName.PopupOpen.raw, PageName.Cart.raw)
                AirRobeAppUtils.dispatchEvent(context, EventName.PopupOpen.raw, PageName.Cart.raw)
            }
        }

        val learnMoreText: String = context.resources.getString(R.string.airrobe_learn_more_link_text)
        val start = detailedDescriptionText.indexOf(learnMoreText)
        val end = start + learnMoreText.length
        detailedDescriptionText.setSpan(cs, start, end, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE)
        tvDetailedDescription.text = detailedDescriptionText
        tvDetailedDescription.movementMethod = LinkMovementMethod.getInstance()
    }

    private fun setExtraInfoText() {
        if (!URLUtil.isValidUrl(widgetInstance.shopModel?.data?.shop?.privacyUrl)) {
            tvExtraInfo.visibility = GONE
        } else {
            tvExtraInfo.visibility = VISIBLE
            var extraInfoText = context.resources.getString(R.string.airrobe_extra_info, widgetInstance.shopModel?.data?.shop?.companyName)
            extraInfoText = extraInfoText.replace("Privacy Policy", "<a href='${widgetInstance.shopModel?.data?.shop?.privacyUrl}'>Privacy Policy</a>")
            tvExtraInfo.text = AirRobeAppUtils.fromHtml(extraInfoText)
            tvExtraInfo.movementMethod = LinkMovementMethod.getInstance()
        }
    }

    fun initialize(
        items: ArrayList<String> = arrayListOf()
    ) {
        this.items = items
        initializeOptInWidget()
    }

    private fun initializeOptInWidget() {
        if (widgetInstance.configuration == null) {
            Log.e(TAG, "Widget sdk is not initialized yet")
            visibility = GONE
            AirRobeSharedPreferenceManager.setOrderOptedIn(context, false)
            return
        }
        if (widgetInstance.shopModel == null || widgetInstance.categoryMapping.categoryMappingsHashmap.isEmpty()) {
            Log.e(TAG, "Category Mapping Info is not loaded")
            visibility = GONE
            AirRobeSharedPreferenceManager.setOrderOptedIn(context, false)
            return
        }
        AirRobeAppUtils.telemetryEvent(context, TelemetryEventName.PageView.raw, PageName.Cart.raw, itemCount = items.size)
        AirRobeAppUtils.dispatchEvent(context, EventName.PageView.raw, PageName.Cart.raw)

        val testVariant = widgetInstance.shopModel!!.getSplitTestVariant(context)
        if (testVariant != null && testVariant.disabled) {
            Log.e(TAG, "Widget is not enabled in target variant")
            visibility = GONE
            return
        }

        if (items.isEmpty()) {
            Log.e(TAG, "Required params can't be empty")
            visibility = GONE
            AirRobeSharedPreferenceManager.setOrderOptedIn(context, false)
            return
        }

        val to = widgetInstance.categoryMapping.checkCategoryEligible(items)
        if (to != null) {
            visibility = VISIBLE
            AirRobeSharedPreferenceManager.setOrderOptedIn(context, AirRobeSharedPreferenceManager.getOptedIn(context))
            AirRobeAppUtils.dispatchEvent(context, EventName.WidgetRender.raw, PageName.Cart.raw)
        } else {
            visibility = GONE
            AirRobeSharedPreferenceManager.setOrderOptedIn(context, false)
            AirRobeAppUtils.dispatchEvent(context, EventName.WidgetNotRendered.raw, PageName.Cart.raw)
            Log.d(TAG, "Category is not eligible")
        }
    }
}
