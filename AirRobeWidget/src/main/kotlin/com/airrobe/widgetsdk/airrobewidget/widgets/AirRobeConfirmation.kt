package com.airrobe.widgetsdk.airrobewidget.widgets

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.content.res.ColorStateList
import android.graphics.drawable.GradientDrawable
import android.net.Uri
import android.os.Build
import android.util.AttributeSet
import android.util.Log
import android.widget.LinearLayout
import com.airrobe.widgetsdk.airrobewidget.R
import com.airrobe.widgetsdk.airrobewidget.config.AirRobeConstants
import com.airrobe.widgetsdk.airrobewidget.config.AirRobeWidgetInstance
import com.airrobe.widgetsdk.airrobewidget.databinding.AirrobeConfirmationBinding
import com.airrobe.widgetsdk.airrobewidget.service.api_controllers.AirRobeEmailCheckController
import com.airrobe.widgetsdk.airrobewidget.service.listeners.AirRobeEmailCheckListener
import com.airrobe.widgetsdk.airrobewidget.utils.AirRobeAppUtils
import com.airrobe.widgetsdk.airrobewidget.utils.AirRobeSharedPreferenceManager
import com.airrobe.widgetsdk.airrobewidget.widgetInstance

@SuppressLint("ClickableViewAccessibility")
@Suppress("DEPRECATION")
class AirRobeConfirmation @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : LinearLayout(context, attrs, defStyleAttr) {
    private var binding: AirrobeConfirmationBinding

    companion object {
        private const val TAG = "AirRobeConfirmation"
    }

    private var orderId: String? = null
    private var email: String? = null
    private var fraudRisk: Boolean = false

    var borderColor: Int =
        if (widgetInstance.borderColor == 0)
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M)
                context.getColor(R.color.airrobe_widget_default_border_color) else
                    context.resources.getColor(R.color.airrobe_widget_default_border_color)
        else
            widgetInstance.borderColor
        set(value) {
            field = value
            val mainBackground = binding.rlMainContainer.background as GradientDrawable
            mainBackground.setStroke(1, value)
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
            binding.tvTitle.setTextColor(value)
            binding.tvDescription.setTextColor(value)
        }

    var buttonBorderColor: Int =
        if (widgetInstance.buttonBorderColor == 0)
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M)
                context.getColor(R.color.airrobe_widget_default_button_border_color) else
                context.resources.getColor(R.color.airrobe_widget_default_button_border_color)
        else
            widgetInstance.buttonBorderColor
        set(value) {
            field = value
            val background = binding.rlActionContainer.background as GradientDrawable
            background.setStroke(1, value)
        }

    var buttonTextColor: Int =
        if (widgetInstance.buttonTextColor == 0)
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M)
                context.getColor(R.color.airrobe_widget_default_button_text_color) else
                context.resources.getColor(R.color.airrobe_widget_default_button_text_color)
        else
            widgetInstance.buttonTextColor
        set(value) {
            field = value
            binding.tvAction.setTextColor(value)
            binding.btnLoading.indeterminateTintList = ColorStateList.valueOf(value)
        }

    init {
        inflate(context, R.layout.airrobe_confirmation, this)
        binding = AirrobeConfirmationBinding.bind(this)

        val listener = object : AirRobeWidgetInstance.InstanceChangeListener {
            override fun onCategoryModelChange() {
                post {
                    initializeConfirmationWidget()
                }
            }

            override fun onConfigChange() {
                initialize()
            }
        }
        widgetInstance.setInstanceChangeListener(listener)
        initialize()
        setupAttributes(attrs)
    }

    private fun setupAttributes(attrs: AttributeSet?) {
        val typedArray = context.theme.obtainStyledAttributes(attrs, R.styleable.AirRobeConfirmation, 0, 0)
        borderColor = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            typedArray.getColor(R.styleable.AirRobeConfirmation_borderColor,
                if (widgetInstance.borderColor == 0)
                    context.getColor(R.color.airrobe_widget_default_border_color)
                else
                    widgetInstance.borderColor
            )
        } else {
            typedArray.getColor(R.styleable.AirRobeConfirmation_borderColor,
                if (widgetInstance.borderColor == 0)
                    context.resources.getColor(R.color.airrobe_widget_default_border_color)
                else
                    widgetInstance.borderColor
            )
        }
        textColor = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            typedArray.getColor(R.styleable.AirRobeConfirmation_textColor,
                if (widgetInstance.textColor == 0)
                    context.getColor(R.color.airrobe_widget_default_text_color)
                else
                    widgetInstance.textColor
            )
        } else {
            typedArray.getColor(R.styleable.AirRobeConfirmation_textColor,
                if (widgetInstance.textColor == 0)
                    context.resources.getColor(R.color.airrobe_widget_default_text_color)
                else
                    widgetInstance.textColor
            )
        }
        buttonBorderColor = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            typedArray.getColor(R.styleable.AirRobeConfirmation_buttonBackgroundColor,
                if (widgetInstance.buttonBorderColor == 0)
                    context.getColor(R.color.airrobe_widget_default_button_border_color)
                else
                    widgetInstance.buttonBorderColor
            )
        } else {
            typedArray.getColor(R.styleable.AirRobeConfirmation_buttonBackgroundColor,
                if (widgetInstance.buttonBorderColor == 0)
                    context.resources.getColor(R.color.airrobe_widget_default_button_border_color)
                else
                    widgetInstance.buttonBorderColor
            )
        }
        buttonTextColor = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            typedArray.getColor(R.styleable.AirRobeConfirmation_buttonTextColor,
                if (widgetInstance.buttonTextColor == 0)
                    context.getColor(R.color.airrobe_widget_default_button_text_color)
                else
                    widgetInstance.buttonTextColor
            )
        } else {
            typedArray.getColor(R.styleable.AirRobeConfirmation_buttonTextColor,
                if (widgetInstance.buttonTextColor == 0)
                    context.resources.getColor(R.color.airrobe_widget_default_button_text_color)
                else
                    widgetInstance.buttonTextColor
            )
        }
    }

    private fun initialize() {
        if (widgetInstance.getConfig() != null) {
            binding.tvAction.setOnTouchListener { v, event ->
                if (AirRobeAppUtils.touchAnimator(context, v, event)) {
                    val intent = Intent(Intent.ACTION_VIEW)
                    intent.data = Uri.parse(AirRobeConstants.ORDER_ACTIVATE_BASE_URL + widgetInstance.getConfig()?.appId + "-" + orderId)
                    context.startActivity(intent)
                }
                true
            }
        }
    }

    fun initialize(
        orderId: String,
        email: String,
        fraudRisk: Boolean = false
    ) {
        this.orderId = orderId
        this.email = email
        this.fraudRisk = fraudRisk
        initializeConfirmationWidget()
    }

    private fun initializeConfirmationWidget() {
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
        if (orderId.isNullOrEmpty() || email.isNullOrEmpty()) {
            Log.e(TAG, "Required params can't be empty")
            visibility = GONE
            return
        }
        if (AirRobeSharedPreferenceManager.getOrderOptedIn(context) && !fraudRisk) {
            visibility = VISIBLE
            binding.btnLoading.visibility = VISIBLE
            binding.btnLoading.animate()
            emailCheck(email!!)
        } else {
            visibility = GONE
            Log.e(TAG, "Confirmation widget is not eligible to show up")
        }
    }

    private fun emailCheck(email: String) {
        val emailCheckController = AirRobeEmailCheckController()
        emailCheckController.airRobeEmailCheckListener = object : AirRobeEmailCheckListener {
            override fun onSuccessEmailCheckApi(isCustomer: Boolean) {
                binding.btnLoading.visibility = GONE
                if (isCustomer) {
                    binding.tvAction.text = context.resources.getString(R.string.airrobe_order_confirmation_visit_text)
                } else {
                    binding.tvAction.text = context.resources.getString(R.string.airrobe_order_confirmation_activate_text)
                }
            }

            override fun onFailedEmailCheckApi(error: String?) {
                binding.btnLoading.visibility = GONE
                binding.tvAction.text = context.resources.getString(R.string.airrobe_order_confirmation_activate_text)
                Log.e(TAG, error ?: "Email Check Api Failed")
            }
        }
        emailCheckController.start(email)
    }
}