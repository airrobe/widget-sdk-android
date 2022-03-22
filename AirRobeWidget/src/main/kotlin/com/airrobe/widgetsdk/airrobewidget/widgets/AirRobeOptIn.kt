package com.airrobe.widgetsdk.airrobewidget.widgets

import android.content.Context
import android.content.res.ColorStateList
import android.graphics.Color
import android.graphics.PorterDuff
import android.graphics.drawable.ColorDrawable
import android.graphics.drawable.GradientDrawable
import android.os.Build
import android.text.Html
import android.text.SpannableString
import android.text.Spanned
import android.text.TextPaint
import android.text.method.LinkMovementMethod
import android.text.style.ClickableSpan
import android.util.AttributeSet
import android.util.Log
import android.view.View
import android.widget.*
import com.airrobe.widgetsdk.airrobewidget.R
import com.airrobe.widgetsdk.airrobewidget.config.AirRobeConstants
import com.airrobe.widgetsdk.airrobewidget.config.AirRobeWidgetInstance
import com.airrobe.widgetsdk.airrobewidget.service.api_controllers.AirRobePriceEngineController
import com.airrobe.widgetsdk.airrobewidget.service.listeners.AirRobePriceEngineListener
import com.airrobe.widgetsdk.airrobewidget.utils.AirRobeSharedPreferenceManager
import com.airrobe.widgetsdk.airrobewidget.widgetInstance

@Suppress("DEPRECATION")
class AirRobeOptIn @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : LinearLayout(context, attrs, defStyleAttr) {
    private var llMainContainer: LinearLayout
    private var optInSwitch: Switch
    private var llSwitchContainer: LinearLayout
    private var tvTitle: TextView
    private var tvDescription: TextView
    private var tvDetailedDescription: TextView
    private var tvExtraInfo: TextView
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
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M)
                context.getColor(R.color.airrobe_widget_default_border_color) else
                context.resources.getColor(R.color.airrobe_widget_default_border_color)
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
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M)
                context.getColor(R.color.airrobe_widget_default_text_color) else
                context.resources.getColor(R.color.airrobe_widget_default_text_color)
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
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M)
                context.getColor(R.color.airrobe_widget_default_switch_color) else
                context.resources.getColor(R.color.airrobe_widget_default_switch_color)
        else
            widgetInstance.switchColor
        set(value) {
            field = value
            setSwitchColor()
        }

    var arrowColor: Int =
        if (widgetInstance.arrowColor == 0)
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M)
                context.getColor(R.color.airrobe_widget_default_arrow_color) else
                context.resources.getColor(R.color.airrobe_widget_default_arrow_color)
        else
            widgetInstance.arrowColor
        set(value) {
            field = value
            ivArrowDown.setColorFilter(value, PorterDuff.Mode.SRC_ATOP)
        }

    var linkTextColor: Int =
        if (widgetInstance.linkTextColor == 0)
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M)
                context.getColor(R.color.airrobe_widget_default_link_text_color) else
                context.resources.getColor(R.color.airrobe_widget_default_link_text_color)
        else
            widgetInstance.linkTextColor
        set(value) {
            field = value
            tvDetailedDescription.setLinkTextColor(value)
            tvExtraInfo.setLinkTextColor(value)
        }

    init {
        inflate(context, R.layout.airrobe_opt_in, this)
        visibility = GONE

        llMainContainer = findViewById(R.id.ll_main_container)
        optInSwitch = findViewById(R.id.opt_in_switch)
        llSwitchContainer = findViewById(R.id.ll_switch_container)
        tvTitle = findViewById(R.id.tv_title)
        tvDescription = findViewById(R.id.tv_description)
        tvDetailedDescription = findViewById(R.id.tv_detailed_description)
        tvExtraInfo = findViewById(R.id.tv_extra_info)
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
        borderColor = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            typedArray.getColor(R.styleable.AirRobeOptIn_borderColor,
                if (widgetInstance.borderColor == 0)
                    context.getColor(R.color.airrobe_widget_default_border_color)
                else
                    widgetInstance.borderColor
            )
        } else {
            typedArray.getColor(R.styleable.AirRobeOptIn_borderColor,
                if (widgetInstance.borderColor == 0)
                    context.resources.getColor(R.color.airrobe_widget_default_border_color)
                else
                    widgetInstance.borderColor
            )
        }
        textColor = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            typedArray.getColor(R.styleable.AirRobeOptIn_textColor,
                if (widgetInstance.textColor == 0)
                    context.getColor(R.color.airrobe_widget_default_text_color)
                else
                    widgetInstance.textColor
            )
        } else {
            typedArray.getColor(R.styleable.AirRobeOptIn_textColor,
                if (widgetInstance.textColor == 0)
                    context.resources.getColor(R.color.airrobe_widget_default_text_color)
                else
                    widgetInstance.textColor
            )
        }
        switchColor = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            typedArray.getColor(R.styleable.AirRobeOptIn_switchColor,
                if (widgetInstance.switchColor == 0)
                    context.getColor(R.color.airrobe_widget_default_switch_color)
                else
                    widgetInstance.switchColor
            )
        } else {
            typedArray.getColor(R.styleable.AirRobeOptIn_switchColor,
                if (widgetInstance.switchColor == 0)
                    context.resources.getColor(R.color.airrobe_widget_default_switch_color)
                else
                    widgetInstance.switchColor
            )
        }
        arrowColor = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            typedArray.getColor(R.styleable.AirRobeOptIn_arrowColor,
                if (widgetInstance.arrowColor == 0)
                    context.getColor(R.color.airrobe_widget_default_arrow_color)
                else
                    widgetInstance.arrowColor
            )
        } else {
            typedArray.getColor(R.styleable.AirRobeOptIn_arrowColor,
                if (widgetInstance.arrowColor == 0)
                    context.resources.getColor(R.color.airrobe_widget_default_arrow_color)
                else
                    widgetInstance.arrowColor
            )
        }
        linkTextColor = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            typedArray.getColor(R.styleable.AirRobeOptIn_linkTextColor,
                if (widgetInstance.linkTextColor == 0)
                    context.getColor(R.color.airrobe_widget_default_link_text_color)
                else
                    widgetInstance.linkTextColor
            )
        } else {
            typedArray.getColor(R.styleable.AirRobeOptIn_linkTextColor,
                if (widgetInstance.linkTextColor == 0)
                    context.resources.getColor(R.color.airrobe_widget_default_link_text_color)
                else
                    widgetInstance.linkTextColor
            )
        }
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
            borderColor,
            switchColor
        )
        optInSwitch.thumbDrawable = ColorDrawable(switchColor)
        optInSwitch.trackDrawable = ColorDrawable(borderColor)
//        DrawableCompat.setTintList(DrawableCompat.wrap(binding.optInSwitch.thumbDrawable), ColorStateList(states, thumbColors))
//        DrawableCompat.setTintList(DrawableCompat.wrap(binding.optInSwitch.trackDrawable), ColorStateList(states, trackColors))
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
            }
        }
        setDetailedDescriptionText()
        setExtraInfoText()
        optInSwitch.isChecked = AirRobeSharedPreferenceManager.getOptedIn(context)
        optInSwitch.setOnCheckedChangeListener { _, isChecked ->
            AirRobeSharedPreferenceManager.setOptedIn(context, isChecked)
        }
        tvPotentialValue.text = context.resources.getString(R.string.airrobe_potential_value_text)
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
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            tvExtraInfo.text = Html.fromHtml(extraInfoText, Html.FROM_HTML_MODE_COMPACT)
        } else {
            tvExtraInfo.text = Html.fromHtml(extraInfoText)
        }
        tvExtraInfo.movementMethod = LinkMovementMethod.getInstance()
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
        if (category.isNullOrEmpty()) {
            Log.e(TAG, "Required params can't be empty")
            visibility = GONE
            return
        }
        val to = widgetInstance.shopModel!!.checkCategoryEligible(arrayListOf(category!!))
        if (to != null) {
            if (widgetInstance.shopModel!!.isBelowPriceThreshold(department, priceCents)) {
                visibility = GONE
                Log.d(TAG, "Below price threshold")
            } else {
                visibility = VISIBLE
                checkIfPotentialValueTextCutOff()
                callPriceEngine(to)
            }
        } else {
            visibility = GONE
            Log.d(TAG, "Category is not eligible")
        }
    }

    private fun checkIfPotentialValueTextCutOff(potentialValue: String? = null) {
        val runnable = Runnable {
            if (tvPotentialValue.layout == null) {
                return@Runnable
            }
            if (tvPotentialValue.layout.getEllipsisCount(0) > 0) {
                if (potentialValue == null) {
                    tvPotentialValue.text = ""
                } else {
                    tvPotentialValue.text = context.resources.getString(R.string.airrobe_potential_value_without_text, potentialValue)
                }
            }
        }
        tvPotentialValue.post(runnable)
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
                    checkIfPotentialValueTextCutOff(fallbackResalePrice())
                } else {
                    tvPotentialValue.text = context.resources.getString(R.string.airrobe_potential_value, resaleValue.toString())
                    checkIfPotentialValueTextCutOff(resaleValue.toString())
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
                checkIfPotentialValueTextCutOff(fallbackResalePrice())
            }
        }
        priceEngineController.start(priceCents, if (rrp == AirRobeConstants.FLOAT_NULL_MAGIC_VALUE) null else rrp , category, brand, material)
    }

    private fun fallbackResalePrice(): String {
        val resaleValue = (priceCents * 65) / 100
        return String.format("%.2f", resaleValue)
    }
}