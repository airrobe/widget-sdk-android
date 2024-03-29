package com.airrobe.widgetsdk.airrobewidget.widgets

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.content.res.ColorStateList
import android.graphics.drawable.GradientDrawable
import android.net.Uri
import android.util.AttributeSet
import android.util.Log
import android.widget.ProgressBar
import android.widget.RelativeLayout
import android.widget.TextView
import com.airrobe.widgetsdk.airrobewidget.R
import com.airrobe.widgetsdk.airrobewidget.config.*
import com.airrobe.widgetsdk.airrobewidget.config.AirRobeConstants
import com.airrobe.widgetsdk.airrobewidget.config.AirRobeWidgetConfig
import com.airrobe.widgetsdk.airrobewidget.config.AirRobeWidgetInstance
import com.airrobe.widgetsdk.airrobewidget.service.api_controllers.AirRobeCreateOptedOutOrderController
import com.airrobe.widgetsdk.airrobewidget.service.api_controllers.AirRobeEmailCheckController
import com.airrobe.widgetsdk.airrobewidget.service.api_controllers.AirRobeIdentifyOrderController
import com.airrobe.widgetsdk.airrobewidget.service.listeners.AirRobeEmailCheckListener
import com.airrobe.widgetsdk.airrobewidget.utils.AirRobeAppUtils
import com.airrobe.widgetsdk.airrobewidget.utils.AirRobeSharedPreferenceManager
import com.airrobe.widgetsdk.airrobewidget.widgetInstance

@SuppressLint("ClickableViewAccessibility")
class AirRobeConfirmation @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : RelativeLayout(context, attrs, defStyleAttr) {
    private var rlMainContainer: RelativeLayout
    private var tvTitle: TextView
    private var tvDescription: TextView
    private var rlActionContainer: RelativeLayout
    private var tvAction: TextView
    private var btnLoading: ProgressBar

    companion object {
        private const val TAG = "AirRobeConfirmation"
    }

    private var orderId: String? = null
    private var email: String? = null
    private var orderSubtotalCents: Int = AirRobeConstants.INT_NULL_MAGIC_VALUE
    private var currency: String = "AUD"
    private var fraudRisk: Boolean = false

    var widgetBackgroundColor: Int =
        if (widgetInstance.backgroundColor == 0)
            AirRobeAppUtils.getColor(context, R.color.airrobe_widget_default_background_color)
        else
            widgetInstance.backgroundColor
        set(value) {
            field = value
            val mainBackground = rlMainContainer.background as GradientDrawable
            mainBackground.setColor(value)
        }

    var borderColor: Int =
        if (widgetInstance.borderColor == 0)
            AirRobeAppUtils.getColor(context, R.color.airrobe_widget_default_border_color)
        else
            widgetInstance.borderColor
        set(value) {
            field = value
            val mainBackground = rlMainContainer.background as GradientDrawable
            mainBackground.setStroke(1, value)
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
        }

    var buttonBorderColor: Int =
        if (widgetInstance.buttonBorderColor == 0)
            AirRobeAppUtils.getColor(context, R.color.airrobe_widget_default_button_border_color)
        else
            widgetInstance.buttonBorderColor
        set(value) {
            field = value
            val background = rlActionContainer.background as GradientDrawable
            background.setStroke(1, value)
        }

    var buttonTextColor: Int =
        if (widgetInstance.buttonTextColor == 0)
            AirRobeAppUtils.getColor(context, R.color.airrobe_widget_default_button_text_color)
        else
            widgetInstance.buttonTextColor
        set(value) {
            field = value
            tvAction.setTextColor(value)
            btnLoading.indeterminateTintList = ColorStateList.valueOf(value)
        }

    init {
        inflate(context, R.layout.airrobe_confirmation, this)
        visibility = GONE

        rlMainContainer = findViewById(R.id.rl_main_container)
        tvTitle = findViewById(R.id.tv_title)
        tvDescription = findViewById(R.id.tv_description)
        rlActionContainer = findViewById(R.id.rl_action_container)
        tvAction = findViewById(R.id.tv_action)
        btnLoading = findViewById(R.id.btn_loading)

        val listener = object : AirRobeWidgetInstance.InstanceChangeListener {
            override fun onShopModelChange() {
                post {
                    initializeConfirmationWidget()
                }
            }

            override fun onConfigChange() {
                initialize()
            }
        }
        widgetInstance.changeListener = listener
        initialize()
        setupAttributes(attrs)
    }

    private fun setupAttributes(attrs: AttributeSet?) {
        val typedArray = context.theme.obtainStyledAttributes(attrs, R.styleable.AirRobeConfirmation, 0, 0)
        widgetBackgroundColor =
            typedArray.getColor(R.styleable.AirRobeConfirmation_backgroundColor,
                if (widgetInstance.backgroundColor == 0)
                    AirRobeAppUtils.getColor(context, R.color.airrobe_widget_default_background_color)
                else
                    widgetInstance.backgroundColor
            )
        borderColor =
            typedArray.getColor(R.styleable.AirRobeConfirmation_borderColor,
                if (widgetInstance.borderColor == 0)
                    AirRobeAppUtils.getColor(context, R.color.airrobe_widget_default_border_color)
                else
                    widgetInstance.borderColor
            )
        textColor =
            typedArray.getColor(R.styleable.AirRobeConfirmation_textColor,
                if (widgetInstance.textColor == 0)
                    AirRobeAppUtils.getColor(context, R.color.airrobe_widget_default_text_color)
                else
                    widgetInstance.textColor
            )
        buttonBorderColor =
            typedArray.getColor(R.styleable.AirRobeConfirmation_buttonBackgroundColor,
                if (widgetInstance.buttonBorderColor == 0)
                    AirRobeAppUtils.getColor(context, R.color.airrobe_widget_default_button_border_color)
                else
                    widgetInstance.buttonBorderColor
            )
        buttonTextColor =
            typedArray.getColor(R.styleable.AirRobeConfirmation_buttonTextColor,
                if (widgetInstance.buttonTextColor == 0)
                    AirRobeAppUtils.getColor(context, R.color.airrobe_widget_default_button_text_color)
                else
                    widgetInstance.buttonTextColor
            )
    }

    private fun initialize() {
        if (widgetInstance.configuration != null) {
            tvAction.setOnTouchListener { v, event ->
                if (AirRobeAppUtils.touchAnimator(context, v, event)) {
                    val intent = Intent(Intent.ACTION_VIEW)
                    val baseUrl = if (widgetInstance.configuration?.mode == Mode.PRODUCTION)
                        AirRobeConstants.ORDER_ACTIVATE_BASE_URL
                    else
                        AirRobeConstants.ORDER_ACTIVATE_SANDBOX_BASE_URL
                    intent.data = Uri.parse(baseUrl + widgetInstance.configuration?.appId + "-" + orderId + "/claim")
                    context.startActivity(intent)
                    AirRobeAppUtils.telemetryEvent(context, TelemetryEventName.ConfirmationClick.raw, PageName.ThankYou.raw)
                    AirRobeAppUtils.dispatchEvent(context, EventName.ConfirmationClick.raw, PageName.ThankYou.raw)
                }
                true
            }
        }
    }

    fun initialize(
        orderId: String,
        email: String,
        orderSubtotalCents: Int = AirRobeConstants.INT_NULL_MAGIC_VALUE,
        currency: String = "AUD",
        fraudRisk: Boolean = false
    ) {
        this.orderId = orderId
        this.email = email
        this.orderSubtotalCents = orderSubtotalCents
        this.currency = currency
        this.fraudRisk = fraudRisk
        initializeConfirmationWidget()
    }

    private fun initializeConfirmationWidget() {
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
        AirRobeAppUtils.telemetryEvent(context, TelemetryEventName.PageView.raw, PageName.ThankYou.raw)
        AirRobeAppUtils.dispatchEvent(context, EventName.PageView.raw, PageName.ThankYou.raw)

        val testVariant = widgetInstance.shopModel!!.getSplitTestVariant(context)
        if (testVariant != null && testVariant.disabled) {
            Log.e(TAG, "Widget is not enabled in target variant")
            visibility = GONE
            return
        }

        if (orderId.isNullOrEmpty() || email.isNullOrEmpty()) {
            Log.e(TAG, "Required params can't be empty")
            visibility = GONE
            return
        }

        if (AirRobeSharedPreferenceManager.getOrderOptedIn(context) && !fraudRisk) {
            visibility = VISIBLE
            btnLoading.visibility = VISIBLE
            btnLoading.animate()
            emailCheck(email!!)
            identifyOrder(widgetInstance.configuration!!, orderId!!)
            AirRobeAppUtils.dispatchEvent(context, EventName.ConfirmationRender.raw, PageName.ThankYou.raw)
        } else {
            visibility = GONE
            createOptedOutOrder()
            Log.e(TAG, "Confirmation widget is not eligible to show up")
        }
    }

    private fun identifyOrder(config: AirRobeWidgetConfig, orderId: String) {
        val identifyOrderController = AirRobeIdentifyOrderController()
        identifyOrderController.start(
            context,
            config,
            orderId,
            AirRobeSharedPreferenceManager.getOrderOptedIn(context)
        )
    }

    private fun createOptedOutOrder() {
        if (orderId.isNullOrEmpty() || orderSubtotalCents == AirRobeConstants.INT_NULL_MAGIC_VALUE) {
            Log.e(TAG, "Not able to call CreateOptedOutOrder because orderId or orderSubtotalCents is not passed.")
            return
        }
        val createOptedOutOrderController = AirRobeCreateOptedOutOrderController()
        createOptedOutOrderController.start(
            orderId!!,
            widgetInstance.configuration!!.appId,
            orderSubtotalCents,
            currency,
            widgetInstance.configuration!!.mode
        )
    }

    private fun emailCheck(email: String) {
        val emailCheckController = AirRobeEmailCheckController()
        emailCheckController.airRobeEmailCheckListener = object : AirRobeEmailCheckListener {
            override fun onSuccessEmailCheckApi(isCustomer: Boolean) {
                btnLoading.visibility = GONE
                if (isCustomer) {
                    tvAction.text = context.resources.getString(R.string.airrobe_order_confirmation_visit_text)
                } else {
                    tvAction.text = context.resources.getString(R.string.airrobe_order_confirmation_activate_text)
                }
            }

            override fun onFailedEmailCheckApi(error: String?) {
                btnLoading.visibility = GONE
                tvAction.text = context.resources.getString(R.string.airrobe_order_confirmation_activate_text)
                Log.e(TAG, error ?: "Email Check Api Failed")
            }
        }
        emailCheckController.start(email)
    }
}