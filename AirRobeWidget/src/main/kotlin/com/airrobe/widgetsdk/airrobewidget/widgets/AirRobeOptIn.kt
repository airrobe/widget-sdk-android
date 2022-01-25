package com.airrobe.widgetsdk.airrobewidget.widgets

import android.content.Context
import android.util.AttributeSet
import android.util.Log
import android.view.LayoutInflater
import android.widget.LinearLayout
import com.airrobe.widgetsdk.airrobewidget.R
import com.airrobe.widgetsdk.airrobewidget.categoryModelInstance
import com.airrobe.widgetsdk.airrobewidget.config.CategoryModelInstance
import com.airrobe.widgetsdk.airrobewidget.config.Constants
import com.airrobe.widgetsdk.airrobewidget.databinding.LayoutOptInBinding
import com.airrobe.widgetsdk.airrobewidget.service.api_controllers.PriceEngineController
import com.airrobe.widgetsdk.airrobewidget.service.listeners.PriceEngineListener

class AirRobeOptIn @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : LinearLayout(context, attrs, defStyleAttr), CategoryModelInstance.CategoryModelChangeListener, PriceEngineListener {
    private val binding = LayoutOptInBinding.inflate(LayoutInflater.from(context));

    private var brand: String? = null
    private var material: String? = null
    private var category: String? = null
    private var priceCents: Float = 0.0f
    private var originalFullPriceCents: Float = Constants.FLOAT_NULL_MAGIC_VALUE
    private var rrpCents: Float = Constants.FLOAT_NULL_MAGIC_VALUE
    private var currency: String? = "AUD"
    private var locale: String? = "en-AU"

    init {
        inflate(context, R.layout.layout_opt_in, this)
        setupAttributes(attrs)
    }

    companion object {
        private const val TAG = "AirRobeOptIn"
    }

    private fun setupAttributes(attrs: AttributeSet?) {
        val typedArray = context.theme.obtainStyledAttributes(attrs, R.styleable.AirRobeOptIn, 0, 0)
        brand                   = typedArray.getString(R.styleable.AirRobeOptIn_brand)
        category                = typedArray.getString(R.styleable.AirRobeOptIn_category)
        material                = typedArray.getString(R.styleable.AirRobeOptIn_material)
        priceCents              = typedArray.getFloat(R.styleable.AirRobeOptIn_priceCents, 0.0f)
        originalFullPriceCents  = typedArray.getFloat(R.styleable.AirRobeOptIn_originalFullPriceCents, Constants.FLOAT_NULL_MAGIC_VALUE)
        rrpCents                = typedArray.getFloat(R.styleable.AirRobeOptIn_rrpCents, Constants.FLOAT_NULL_MAGIC_VALUE)
        currency                = typedArray.getString(R.styleable.AirRobeOptIn_currency)
        locale                  = typedArray.getString(R.styleable.AirRobeOptIn_locale)

        categoryModelInstance.setListener(this)
        initializeOptInWidget()
    }

    fun initialize(
        brand: String? = null,
        material: String? = null,
        category: String,
        priceCents: Float,
        originalFullPriceCents: Float = Constants.FLOAT_NULL_MAGIC_VALUE,
        rrpCents: Float = Constants.FLOAT_NULL_MAGIC_VALUE,
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
        if (categoryModelInstance.getCategoryModel() == null) {
            Log.e(TAG, "Category Mapping Info is not loaded")
            visibility = GONE
            return
        }
        if (category.isNullOrEmpty()) {
            Log.e(TAG, "Required params can't be empty")
            visibility = GONE
            return
        }
        val to = categoryModelInstance.getCategoryModel()!!.checkCategoryEligible(arrayListOf(category))
        if (to != null) {
            callPriceEngine(to)
        } else {
            Log.d(TAG, "Category is not eligible")
        }
    }

    private fun callPriceEngine(category: String) {
        val rrp = if (originalFullPriceCents == Constants.FLOAT_NULL_MAGIC_VALUE) rrpCents else originalFullPriceCents
        val priceEngineController = PriceEngineController()
        priceEngineController.priceEngineListener = this
        priceEngineController.start(priceCents, if (rrp == Constants.FLOAT_NULL_MAGIC_VALUE) null else rrp , category, brand, material)
    }

    override fun onChange() {
        initializeOptInWidget()
    }

    override fun onSuccessPriceEngineApi(resaleValue: Int?) {
        if (resaleValue == null) {
            visibility = GONE
        } else {
            visibility = VISIBLE
            binding.tvPotentialValue.setText(R.string.potential_value)
        }
    }

    override fun onFailedPriceEngineApi(error: String?) {
        visibility = GONE
        if (error.isNullOrEmpty()) {
            Log.e(TAG, "PriceEngine Api failed")
        } else {
            Log.e(TAG, error)
        }
    }
}