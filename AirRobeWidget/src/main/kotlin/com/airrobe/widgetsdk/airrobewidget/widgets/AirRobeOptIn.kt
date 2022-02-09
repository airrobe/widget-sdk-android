package com.airrobe.widgetsdk.airrobewidget.widgets

import android.content.Context
import android.graphics.Color
import android.graphics.PorterDuff
import android.text.Html
import android.text.SpannableString
import android.text.Spanned
import android.text.TextPaint
import android.text.method.LinkMovementMethod
import android.text.style.ClickableSpan
import android.util.AttributeSet
import android.util.Log
import android.view.View
import android.widget.LinearLayout
import com.airrobe.widgetsdk.airrobewidget.R
import com.airrobe.widgetsdk.airrobewidget.config.AirRobeConstants
import com.airrobe.widgetsdk.airrobewidget.config.AirRobeWidgetInstance
import com.airrobe.widgetsdk.airrobewidget.databinding.AirrobeOptInBinding
import com.airrobe.widgetsdk.airrobewidget.service.api_controllers.AirRobePriceEngineController
import com.airrobe.widgetsdk.airrobewidget.service.listeners.AirRobePriceEngineListener
import com.airrobe.widgetsdk.airrobewidget.utils.AirRobeSharedPreferenceManager
import com.airrobe.widgetsdk.airrobewidget.widgetInstance

class AirRobeOptIn @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : LinearLayout(context, attrs, defStyleAttr), AirRobeWidgetInstance.InstanceChangeListener, AirRobePriceEngineListener {
    private var binding: AirrobeOptInBinding

    internal enum class ExpandType {
        Opened,
        Closed
    }
    private var expandType: ExpandType = ExpandType.Closed
    private var brand: String? = null
    private var material: String? = null
    private var category: String? = null
    private var priceCents: Float = 0.0f
    private var originalFullPriceCents: Float = AirRobeConstants.FLOAT_NULL_MAGIC_VALUE
    private var rrpCents: Float = AirRobeConstants.FLOAT_NULL_MAGIC_VALUE
    private var currency: String? = "AUD"
    private var locale: String? = "en-AU"

    companion object {
        private const val TAG = "AirRobeOptIn"
    }

    init {
        inflate(context, R.layout.airrobe_opt_in, this)
        binding = AirrobeOptInBinding.bind(this)

        binding.tvDetailedDescription.visibility = GONE
        binding.llSwitchContainer.setOnClickListener {
            if (expandType == ExpandType.Opened) {
                binding.tvDetailedDescription.visibility = GONE
                expandType = ExpandType.Closed
                binding.ivArrowDown.animate().rotation(0.0f).duration = 80
            } else {
                binding.tvDetailedDescription.visibility = VISIBLE
                expandType = ExpandType.Opened
                binding.ivArrowDown.animate().rotation(180.0f).duration = 80
            }
        }
        val detailedDescriptionText = SpannableString(context.resources.getString(R.string.airrobe_detailed_description))
        val cs = object : ClickableSpan() {
            override fun updateDrawState(ds: TextPaint) {
                super.updateDrawState(ds)
                ds.isUnderlineText = true
            }

            override fun onClick(p0: View) {
                val dialog = AirRobeLearnMore(context)
                dialog.optInSwitch = binding.optInSwitch
                dialog.isFromMultiOptIn = false
                dialog.show()
            }
        }

        detailedDescriptionText.setSpan(cs, 229, 240, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE)
        binding.tvDetailedDescription.text = detailedDescriptionText
        binding.tvDetailedDescription.movementMethod = LinkMovementMethod.getInstance()
        if (widgetInstance.getConfig() != null) {
            binding.ivArrowDown.setColorFilter(Color.parseColor(widgetInstance.getConfig()?.color), PorterDuff.Mode.SRC_ATOP)
            binding.tvDetailedDescription.highlightColor = Color.parseColor(widgetInstance.getConfig()?.color)
            val extraInfoText = context.resources.getString(R.string.airrobe_extra_info).replace("Privacy Policy", "<a href='${widgetInstance.getConfig()?.privacyPolicyURL}'>Privacy Policy</a>")
            binding.tvExtraInfo.text = Html.fromHtml(extraInfoText)
            binding.tvExtraInfo.movementMethod = LinkMovementMethod.getInstance()
            binding.tvExtraInfo.highlightColor = Color.parseColor(widgetInstance.getConfig()?.color)
        }

        binding.optInSwitch.isChecked = AirRobeSharedPreferenceManager.getOptedIn(context)
        binding.optInSwitch.setOnCheckedChangeListener { _, isChecked ->
            AirRobeSharedPreferenceManager.setOptedIn(context, isChecked)
        }
        binding.tvPotentialValue.text = context.resources.getString(R.string.airrobe_potential_value_text)
        setupAttributes(attrs)
    }

    private fun setupAttributes(attrs: AttributeSet?) {
        val typedArray = context.theme.obtainStyledAttributes(attrs, R.styleable.AirRobeOptIn, 0, 0)
        brand                   = typedArray.getString(R.styleable.AirRobeOptIn_brand)
        category                = typedArray.getString(R.styleable.AirRobeOptIn_category)
        material                = typedArray.getString(R.styleable.AirRobeOptIn_material)
        priceCents              = typedArray.getFloat(R.styleable.AirRobeOptIn_priceCents, 0.0f)
        originalFullPriceCents  = typedArray.getFloat(R.styleable.AirRobeOptIn_originalFullPriceCents, AirRobeConstants.FLOAT_NULL_MAGIC_VALUE)
        rrpCents                = typedArray.getFloat(R.styleable.AirRobeOptIn_rrpCents, AirRobeConstants.FLOAT_NULL_MAGIC_VALUE)
        currency                = typedArray.getString(R.styleable.AirRobeOptIn_currency)
        locale                  = typedArray.getString(R.styleable.AirRobeOptIn_locale)

        widgetInstance.setInstanceChangeListener(this)
        initializeOptInWidget()
    }

    fun initialize(
        brand: String? = null,
        material: String? = null,
        category: String,
        priceCents: Float,
        originalFullPriceCents: Float = AirRobeConstants.FLOAT_NULL_MAGIC_VALUE,
        rrpCents: Float = AirRobeConstants.FLOAT_NULL_MAGIC_VALUE,
        currency: String = "AUD",
        locale: String = "en-AU"
    ) {
        this.brand = brand
        this.material = material
        this.category = category
        this.priceCents = priceCents
        this.originalFullPriceCents = originalFullPriceCents
        this.rrpCents = rrpCents
        this.currency = currency
        this.locale = locale

        initializeOptInWidget()
    }

    private fun initializeOptInWidget() {
        if (widgetInstance.getConfig() == null) {
            Log.e(TAG, "Widget sdk is not initialized yet")
            visibility = GONE
            return
        }
        if (widgetInstance.getCategoryModel() == null) {
            Log.e(TAG, "Category Mapping Info is not loaded")
            visibility = GONE
            return
        }
        if (category.isNullOrEmpty()) {
            Log.e(TAG, "Required params can't be empty")
            visibility = GONE
            return
        }
        val to = widgetInstance.getCategoryModel()!!.checkCategoryEligible(arrayListOf(category!!))
        if (to != null) {
            visibility = VISIBLE
            checkIfPotentialValueTextCutOff()
            callPriceEngine(to)
        } else {
            visibility = GONE
            Log.d(TAG, "Category is not eligible")
        }
    }

    private fun checkIfPotentialValueTextCutOff(potentialValue: String? = null) {
        val runnable = Runnable {
            if (binding.tvPotentialValue.layout == null) {
                return@Runnable
            }
            if (binding.tvPotentialValue.layout.getEllipsisCount(0) > 0) {
                if (potentialValue == null) {
                    binding.tvPotentialValue.text = ""
                } else {
                    binding.tvPotentialValue.text = context.resources.getString(R.string.airrobe_potential_value_without_text, potentialValue)
                }
            }
        }
        binding.tvPotentialValue.post(runnable)
    }

    private fun callPriceEngine(category: String) {
        binding.priceLoading.visibility = VISIBLE
        binding.priceLoading.animate()
        val rrp = if (originalFullPriceCents == AirRobeConstants.FLOAT_NULL_MAGIC_VALUE) rrpCents else originalFullPriceCents
        val priceEngineController = AirRobePriceEngineController()
        priceEngineController.airRobePriceEngineListener = this
        priceEngineController.start(priceCents, if (rrp == AirRobeConstants.FLOAT_NULL_MAGIC_VALUE) null else rrp , category, brand, material)
    }

    override fun onSuccessPriceEngineApi(resaleValue: Int?) {
        binding.priceLoading.visibility = GONE
        if (resaleValue == null) {
            Log.e(TAG, "Resale price is null")
            binding.tvPotentialValue.text = context.resources.getString(R.string.airrobe_potential_value, fallbackResalePrice())
            checkIfPotentialValueTextCutOff(fallbackResalePrice())
        } else {
            binding.tvPotentialValue.text = context.resources.getString(R.string.airrobe_potential_value, resaleValue.toString())
            checkIfPotentialValueTextCutOff(resaleValue.toString())
        }
    }

    override fun onFailedPriceEngineApi(error: String?) {
        binding.priceLoading.visibility = GONE
        if (error.isNullOrEmpty()) {
            Log.e(TAG, "PriceEngine Api failed")
        } else {
            Log.e(TAG, error)
        }
        binding.tvPotentialValue.text = context.resources.getString(R.string.airrobe_potential_value, fallbackResalePrice())
        checkIfPotentialValueTextCutOff(fallbackResalePrice())
    }

    private fun fallbackResalePrice(): String {
        val resaleValue = (priceCents * 65) / 100
        return String.format("%.2f", resaleValue)
    }

    override fun onCategoryModelChange() {
        post {
            initializeOptInWidget()
        }
    }

    override fun onConfigChange() {
        if (widgetInstance.getConfig() != null) {
            binding.ivArrowDown.setColorFilter(Color.parseColor(widgetInstance.getConfig()?.color), PorterDuff.Mode.SRC_ATOP)
            binding.tvDetailedDescription.highlightColor = Color.parseColor(widgetInstance.getConfig()?.color)
            val extraInfoText = context.resources.getString(R.string.airrobe_extra_info).replace("Privacy Policy", "<a href='${widgetInstance.getConfig()?.privacyPolicyURL}'>Privacy Policy</a>")
            binding.tvExtraInfo.text = Html.fromHtml(extraInfoText)
            binding.tvExtraInfo.movementMethod = LinkMovementMethod.getInstance()
            binding.tvExtraInfo.highlightColor = Color.parseColor(widgetInstance.getConfig()?.color)
        }
    }
}