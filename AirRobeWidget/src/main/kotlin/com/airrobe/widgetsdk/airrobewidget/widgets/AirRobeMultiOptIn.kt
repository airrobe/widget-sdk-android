package com.airrobe.widgetsdk.airrobewidget.widgets

import android.content.Context
import android.content.res.ColorStateList
import android.graphics.Color
import android.graphics.PorterDuff
import android.graphics.drawable.ColorDrawable
import android.graphics.drawable.GradientDrawable
import android.text.SpannableString
import android.text.method.LinkMovementMethod
import android.text.style.ClickableSpan
import android.util.AttributeSet
import android.util.Log
import com.airrobe.widgetsdk.airrobewidget.R
import com.airrobe.widgetsdk.airrobewidget.widgetInstance
import com.airrobe.widgetsdk.airrobewidget.config.AirRobeWidgetInstance
import android.text.TextPaint

import android.text.Spanned
import android.view.View
import android.widget.*
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
    private var tvExtraInfo: TextView
    private var ivArrowDown: ImageView

    companion object {
        private const val TAG = "AirRobeMultiOptIn"
    }

    internal enum class ExpandType {
        Opened,
        Closed
    }
    private var expandType: ExpandType = ExpandType.Closed
    private var items: Array<CharSequence>? = arrayOf()

    var borderColor: Int =
        if (widgetInstance.borderColor == 0)
            AirRobeAppUtils.getColor(context,R.color.airrobe_widget_default_border_color)
        else
            widgetInstance.borderColor
        set(value) {
            field = value
            val mainBackground = llMainContainer.background as GradientDrawable
            mainBackground.setStroke(1, value)
            setSwitchColor()
        }

    var textColor: Int =
        if (widgetInstance.textColor == 0)
            AirRobeAppUtils.getColor(context,R.color.airrobe_widget_default_text_color)
        else
            widgetInstance.textColor
        set(value) {
            field = value
            tvTitle.setTextColor(value)
            tvDescription.setTextColor(value)
            tvDetailedDescription.setTextColor(value)
            tvExtraInfo.setTextColor(value)
        }

    var switchColor: Int =
        if (widgetInstance.switchColor == 0)
            AirRobeAppUtils.getColor(context,R.color.airrobe_widget_default_switch_color)
        else
            widgetInstance.switchColor
        set(value) {
            field = value
            setSwitchColor()
        }

    var arrowColor: Int =
        if (widgetInstance.arrowColor == 0)
            AirRobeAppUtils.getColor(context,R.color.airrobe_widget_default_arrow_color)
        else
            widgetInstance.arrowColor
        set(value) {
            field = value
            ivArrowDown.setColorFilter(value, PorterDuff.Mode.SRC_ATOP)
        }

    var linkTextColor: Int =
        if (widgetInstance.linkTextColor == 0)
            AirRobeAppUtils.getColor(context,R.color.airrobe_widget_default_link_text_color)
        else
            widgetInstance.linkTextColor
        set(value) {
            field = value
            tvDetailedDescription.setLinkTextColor(value)
            tvExtraInfo.setLinkTextColor(value)
        }

    init {
        inflate(context, R.layout.airrobe_multi_opt_in, this)
        visibility = GONE

        llMainContainer = findViewById(R.id.ll_main_container)
        optInSwitch = findViewById(R.id.opt_in_switch)
        llSwitchContainer = findViewById(R.id.ll_switch_container)
        tvTitle = findViewById(R.id.tv_title)
        tvDescription = findViewById(R.id.tv_description)
        tvDetailedDescription = findViewById(R.id.tv_detailed_description)
        tvExtraInfo = findViewById(R.id.tv_extra_info)
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
                    AirRobeAppUtils.getColor(context,R.color.airrobe_widget_default_border_color)
                else
                    widgetInstance.borderColor
            )
        textColor =
            typedArray.getColor(R.styleable.AirRobeMultiOptIn_textColor,
                if (widgetInstance.textColor == 0)
                    AirRobeAppUtils.getColor(context,R.color.airrobe_widget_default_text_color)
                else
                    widgetInstance.textColor
            )
        switchColor =
            typedArray.getColor(R.styleable.AirRobeMultiOptIn_switchColor,
                if (widgetInstance.switchColor == 0)
                    AirRobeAppUtils.getColor(context,R.color.airrobe_widget_default_switch_color)
                else
                    widgetInstance.switchColor
            )
        arrowColor =
            typedArray.getColor(R.styleable.AirRobeMultiOptIn_arrowColor,
                if (widgetInstance.arrowColor == 0)
                    AirRobeAppUtils.getColor(context,R.color.airrobe_widget_default_arrow_color)
                else
                    widgetInstance.arrowColor
            )
        linkTextColor =
            typedArray.getColor(R.styleable.AirRobeMultiOptIn_linkTextColor,
                if (widgetInstance.linkTextColor == 0)
                    AirRobeAppUtils.getColor(context,R.color.airrobe_widget_default_link_text_color)
                else
                    widgetInstance.linkTextColor
            )
    }

    private fun setSwitchColor() {
        val states = arrayOf(
            intArrayOf(-android.R.attr.state_checked),
            intArrayOf(android.R.attr.state_checked)
        )
        val thumbColors = intArrayOf(
            Color.WHITE,
            switchColor
        )
        val trackColors = intArrayOf(
            borderColor,
            switchColor
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
            } else {
                tvDetailedDescription.visibility = VISIBLE
                expandType = ExpandType.Opened
                ivArrowDown.animate().rotation(180.0f).duration = 80
                AirRobeAppUtils.telemetryEvent(context, widgetInstance.configuration, "Widget Expand Arrow Click", "Cart")
            }
        }
        setDetailedDescriptionText()
        setExtraInfoText()
        optInSwitch.isChecked = AirRobeSharedPreferenceManager.getOptedIn(context)
        optInSwitch.setOnCheckedChangeListener { _, isChecked ->
            AirRobeSharedPreferenceManager.setOptedIn(context, isChecked)
            AirRobeSharedPreferenceManager.setOrderOptedIn(context, isChecked)
            if (isChecked) {
                AirRobeAppUtils.telemetryEvent(context, widgetInstance.configuration, "Opted in to AirRobe", "Cart")
            } else {
                AirRobeAppUtils.telemetryEvent(context, widgetInstance.configuration, "Opted out of AirRobe", "Cart")
            }
        }
    }

    private fun setDetailedDescriptionText() {
        val detailedDescriptionText = SpannableString(context.resources.getString(R.string.airrobe_detailed_description))
        val cs = object : ClickableSpan() {
            override fun updateDrawState(ds: TextPaint) {
                super.updateDrawState(ds)
                ds.isUnderlineText = true
            }

            override fun onClick(p0: View) {
                val dialog = AirRobeLearnMore(context)
                dialog.optInSwitchFromOptInWidget = optInSwitch
                dialog.isFromMultiOptIn = false
                dialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
                dialog.show()
                AirRobeAppUtils.telemetryEvent(context, widgetInstance.configuration, "Pop up click", "Cart")
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
        val extraInfoText = context.resources.getString(R.string.airrobe_extra_info).replace("Privacy Policy", "<a href='${widgetInstance.configuration?.privacyPolicyURL}'>Privacy Policy</a>")
        tvExtraInfo.text = AirRobeAppUtils.fromHtml(extraInfoText)
        tvExtraInfo.movementMethod = LinkMovementMethod.getInstance()
    }

    fun initialize(
        items: Array<CharSequence> = arrayOf()
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
        if (widgetInstance.shopModel == null) {
            Log.e(TAG, "Category Mapping Info is not loaded")
            visibility = GONE
            AirRobeSharedPreferenceManager.setOrderOptedIn(context, false)
            return
        }
        if (items.isNullOrEmpty()) {
            Log.e(TAG, "Required params can't be empty")
            visibility = GONE
            AirRobeSharedPreferenceManager.setOrderOptedIn(context, false)
            return
        }

        val newItems = arrayListOf<String>()
        for (item in items!!) {
            newItems.add(item.toString())
        }
        val to = widgetInstance.shopModel!!.checkCategoryEligible(newItems)
        if (to != null) {
            visibility = VISIBLE
            AirRobeSharedPreferenceManager.setOrderOptedIn(context, AirRobeSharedPreferenceManager.getOptedIn(context))
            AirRobeAppUtils.telemetryEvent(context, widgetInstance.configuration, "pageview", "Cart")
        } else {
            visibility = GONE
            AirRobeSharedPreferenceManager.setOrderOptedIn(context, false)
            Log.d(TAG, "Category is not eligible")
        }
    }
}