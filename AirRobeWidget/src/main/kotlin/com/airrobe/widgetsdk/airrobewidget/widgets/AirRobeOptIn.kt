package com.airrobe.widgetsdk.airrobewidget.widgets

import android.content.Context
import android.content.res.ColorStateList
import android.graphics.Color
import android.graphics.PorterDuff
import android.graphics.drawable.ColorDrawable
import android.graphics.drawable.GradientDrawable
import android.text.SpannableString
import android.text.Spanned
import android.text.TextPaint
import android.text.method.LinkMovementMethod
import android.text.style.ClickableSpan
import android.util.AttributeSet
import android.util.Log
import android.view.View
import android.webkit.URLUtil
import android.widget.*
import com.airrobe.widgetsdk.airrobewidget.R
import com.airrobe.widgetsdk.airrobewidget.config.*
import com.airrobe.widgetsdk.airrobewidget.config.AirRobeConstants
import com.airrobe.widgetsdk.airrobewidget.config.AirRobeWidgetInstance
import com.airrobe.widgetsdk.airrobewidget.service.api_controllers.AirRobePriceEngineController
import com.airrobe.widgetsdk.airrobewidget.service.listeners.AirRobePriceEngineListener
import com.airrobe.widgetsdk.airrobewidget.utils.AirRobeAppUtils
import com.airrobe.widgetsdk.airrobewidget.utils.AirRobeSharedPreferenceManager
import com.airrobe.widgetsdk.airrobewidget.widgetInstance

class AirRobeOptIn @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : LinearLayout(context, attrs, defStyleAttr) {
    private var llMainContainer: LinearLayout
    private var optInSwitch: Switch
    private var llSwitchContainer: LinearLayout
    private var tvTitle: TextView
    private var tvDescription: TextView
    private var tvDetailedDescription: TextView
    private lateinit var tvExtraInfo: TextView
    private var tvPotentialValue: TextView
    private var priceLoading: ProgressBar
    private var ivArrowDown: ImageView

    companion object {
        private const val TAG = "AirRobeOptIn"
    }

    internal enum class ExpandType {
        Opened,
        Closed
    }

    private val testVariant = widgetInstance.shopModel?.getSplitTestVariant(context)

    private var expandType: ExpandType = ExpandType.Closed
    private var brand: String? = null
    private var material: String? = null
    private var category: String? = null
    private var department: String? = null
    private var priceCents: Float = 0.0f
    private var originalFullPriceCents: Float = AirRobeConstants.FLOAT_NULL_MAGIC_VALUE
    private var rrpCents: Float = AirRobeConstants.FLOAT_NULL_MAGIC_VALUE
    private var currency: String? = "AUD"
    private var locale: String? = "en-AU"

    var borderColor: Int =
        if (widgetInstance.borderColor == 0)
            AirRobeAppUtils.getColor(context, R.color.airrobe_widget_default_border_color)
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
            AirRobeAppUtils.getColor(context, R.color.airrobe_widget_default_text_color)
        else
            widgetInstance.textColor
        set(value) {
            field = value
            tvTitle.setTextColor(value)
            tvDescription.setTextColor(value)
            tvDetailedDescription.setTextColor(value)
            tvExtraInfo.setTextColor(value)
            tvPotentialValue.setTextColor(value)
            priceLoading.indeterminateTintList = ColorStateList.valueOf(value)
        }

    var switchColor: Int =
        if (widgetInstance.switchColor == 0)
            AirRobeAppUtils.getColor(context, R.color.airrobe_widget_default_switch_color)
        else
            widgetInstance.switchColor
        set(value) {
            field = value
            setSwitchColor()
        }

    var arrowColor: Int =
        if (widgetInstance.arrowColor == 0)
            AirRobeAppUtils.getColor(context, R.color.airrobe_widget_default_arrow_color)
        else
            widgetInstance.arrowColor
        set(value) {
            field = value
            ivArrowDown.setColorFilter(value, PorterDuff.Mode.SRC_ATOP)
        }

    var linkTextColor: Int =
        if (widgetInstance.linkTextColor == 0)
            AirRobeAppUtils.getColor(context, R.color.airrobe_widget_default_link_text_color)
        else
            widgetInstance.linkTextColor
        set(value) {
            field = value
            tvDetailedDescription.setLinkTextColor(value)
            tvExtraInfo.setLinkTextColor(value)
        }

    init {
        when (testVariant?.splitTestVariant) {
            AirRobeVariants.Enhanced.raw -> {
                inflate(context, R.layout.airrobe_opt_in_enhanced, this)
            }
            else -> {
                inflate(context, R.layout.airrobe_opt_in_default, this)
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
        tvPotentialValue = findViewById(R.id.tv_potential_value)
        priceLoading = findViewById(R.id.price_loading)
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
        val typedArray = context.theme.obtainStyledAttributes(attrs, R.styleable.AirRobeOptIn, 0, 0)
        borderColor =
            typedArray.getColor(R.styleable.AirRobeOptIn_borderColor,
                if (widgetInstance.borderColor == 0)
                    AirRobeAppUtils.getColor(context, R.color.airrobe_widget_default_border_color)
                else
                    widgetInstance.borderColor
            )
        textColor =
            typedArray.getColor(R.styleable.AirRobeOptIn_textColor,
                if (widgetInstance.textColor == 0)
                    AirRobeAppUtils.getColor(context, R.color.airrobe_widget_default_text_color)
                else
                    widgetInstance.textColor
            )
        switchColor =
            typedArray.getColor(R.styleable.AirRobeOptIn_switchColor,
                if (widgetInstance.switchColor == 0)
                    AirRobeAppUtils.getColor(context, R.color.airrobe_widget_default_switch_color)
                else
                    widgetInstance.switchColor
            )
        arrowColor =
            typedArray.getColor(R.styleable.AirRobeOptIn_arrowColor,
                if (widgetInstance.arrowColor == 0)
                    AirRobeAppUtils.getColor(context, R.color.airrobe_widget_default_arrow_color)
                else
                    widgetInstance.arrowColor
            )
        linkTextColor =
            typedArray.getColor(R.styleable.AirRobeOptIn_linkTextColor,
                if (widgetInstance.linkTextColor == 0)
                    AirRobeAppUtils.getColor(context, R.color.airrobe_widget_default_link_text_color)
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
                AirRobeAppUtils.dispatchEvent(context, EventName.Collapse.raw, PageName.Product.raw)
            } else {
                tvDetailedDescription.visibility = VISIBLE
                expandType = ExpandType.Opened
                ivArrowDown.animate().rotation(180.0f).duration = 80
                AirRobeAppUtils.telemetryEvent(context, TelemetryEventName.Expand.raw, PageName.Product.raw)
                AirRobeAppUtils.dispatchEvent(context, EventName.Expand.raw, PageName.Product.raw)
            }
        }
        setDetailedDescriptionText()
        setExtraInfoText()
        optInSwitch.isChecked = AirRobeSharedPreferenceManager.getOptedIn(context)
        if (optInSwitch.isChecked) {
            tvTitle.text = context.resources.getString(R.string.airrobe_added_to)
        } else {
            tvTitle.text = context.resources.getString(R.string.airrobe_add_to)
        }
        optInSwitch.setOnCheckedChangeListener { _, isChecked ->
            AirRobeSharedPreferenceManager.setOptedIn(context, isChecked)

            if (isChecked) {
                tvTitle.text = context.resources.getString(R.string.airrobe_added_to)
                AirRobeAppUtils.telemetryEvent(context, TelemetryEventName.OptIn.raw, PageName.Product.raw)
                AirRobeAppUtils.dispatchEvent(context, EventName.OptIn.raw, PageName.Product.raw)
            } else {
                tvTitle.text = context.resources.getString(R.string.airrobe_add_to)
                AirRobeAppUtils.telemetryEvent(context, TelemetryEventName.OptOut.raw, PageName.Product.raw)
                AirRobeAppUtils.dispatchEvent(context, EventName.OptOut.raw, PageName.Product.raw)
            }
        }
        tvPotentialValue.text = context.resources.getString(R.string.airrobe_potential_value_without_value)
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
                AirRobeAppUtils.telemetryEvent(context, TelemetryEventName.PopupOpen.raw, PageName.Product.raw)
                AirRobeAppUtils.dispatchEvent(context, EventName.PopupOpen.raw, PageName.Product.raw)
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
        brand: String? = null,
        material: String? = null,
        category: String,
        department: String? = null,
        priceCents: Float,
        originalFullPriceCents: Float = AirRobeConstants.FLOAT_NULL_MAGIC_VALUE,
        rrpCents: Float = AirRobeConstants.FLOAT_NULL_MAGIC_VALUE,
        currency: String = "AUD",
        locale: String = "en-AU"
    ) {
        this.brand = brand
        this.material = material
        this.category = category
        this.department = department
        this.priceCents = priceCents
        this.originalFullPriceCents = originalFullPriceCents
        this.rrpCents = rrpCents
        this.currency = currency
        this.locale = locale

        initializeOptInWidget()
    }

    private fun initializeOptInWidget() {
        if (widgetInstance.configuration == null) {
            Log.e(TAG, "Widget sdk is not initialized yet")
            visibility = GONE
            return
        }
        if (widgetInstance.shopModel == null) {
            Log.e(TAG, "Category Mapping Info is not loaded")
            visibility = GONE
            return
        }
        AirRobeAppUtils.telemetryEvent(
            context,
            TelemetryEventName.PageView.raw,
            PageName.Product.raw,
            brand,
            material,
            category,
            department
        )
        AirRobeAppUtils.dispatchEvent(context, EventName.PageView.raw, PageName.Product.raw)

        val testVariant = widgetInstance.shopModel!!.getSplitTestVariant(context)
        if (testVariant != null && testVariant.disabled) {
            Log.e(TAG, "Widget is not enabled in target variant")
            visibility = GONE
            return
        }

        if (category.isNullOrEmpty()) {
            Log.e(TAG, "Required params can't be empty")
            visibility = GONE
            return
        }
        val to = widgetInstance.categoryMapping.checkCategoryEligible(arrayListOf(category!!))
        if (to != null) {
            if (widgetInstance.shopModel!!.isBelowPriceThreshold(department, priceCents)) {
                visibility = GONE
                AirRobeAppUtils.dispatchEvent(context, EventName.WidgetNotRendered.raw, PageName.Product.raw)
                Log.d(TAG, "Below price threshold")
            } else {
                visibility = VISIBLE
                checkIfDescriptionTextCutOff()
                callPriceEngine(to)
                AirRobeAppUtils.dispatchEvent(context, EventName.WidgetRender.raw, PageName.Product.raw)
            }
        } else {
            visibility = GONE
            AirRobeAppUtils.dispatchEvent(context, EventName.WidgetNotRendered.raw, PageName.Product.raw)
            Log.d(TAG, "Category is not eligible")
        }
    }

    private fun checkIfDescriptionTextCutOff() {
        val runnable = Runnable {
            if (tvPotentialValue.layout == null) {
                return@Runnable
            }
            if (tvPotentialValue.layout.getEllipsisCount(0) > 0) {
                tvDescription.text = context.resources.getString(R.string.airrobe_description_text_cut_off)
            }
        }
        tvDescription.post(runnable)
    }

    private fun callPriceEngine(category: String) {
        priceLoading.visibility = VISIBLE
        priceLoading.animate()
        val rrp = if (originalFullPriceCents == AirRobeConstants.FLOAT_NULL_MAGIC_VALUE) rrpCents else originalFullPriceCents
        val priceEngineController = AirRobePriceEngineController()
        priceEngineController.airRobePriceEngineListener = object : AirRobePriceEngineListener {
            override fun onSuccessPriceEngineApi(resaleValue: Int?) {
                priceLoading.visibility = GONE
                if (resaleValue == null) {
                    Log.e(TAG, "Resale price is null")
                    tvPotentialValue.text = context.resources.getString(R.string.airrobe_potential_value, fallbackResalePrice())
                    checkIfDescriptionTextCutOff()
                } else {
                    tvPotentialValue.text = context.resources.getString(R.string.airrobe_potential_value, resaleValue.toString())
                    checkIfDescriptionTextCutOff()
                }
            }

            override fun onFailedPriceEngineApi(error: String?) {
                priceLoading.visibility = GONE
                if (error.isNullOrEmpty()) {
                    Log.e(TAG, "PriceEngine Api failed")
                } else {
                    Log.e(TAG, error)
                }
                tvPotentialValue.text = context.resources.getString(R.string.airrobe_potential_value, fallbackResalePrice())
                checkIfDescriptionTextCutOff()
            }
        }
        priceEngineController.start(priceCents, if (rrp == AirRobeConstants.FLOAT_NULL_MAGIC_VALUE) null else rrp, category, brand, material)
    }

    private fun fallbackResalePrice(): String {
        val resaleValue = (priceCents * 65) / 100
        return String.format("%.2f", resaleValue)
    }
}