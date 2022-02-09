package com.airrobe.widgetsdk.airrobewidget.widgets

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.util.AttributeSet
import android.util.Log
import android.widget.RelativeLayout
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
class AirRobeConfirmation @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : RelativeLayout(context, attrs, defStyleAttr), AirRobeWidgetInstance.InstanceChangeListener, AirRobeEmailCheckListener {
    private var binding: AirrobeConfirmationBinding

    private var orderId: String? = null
    private var email: String? = null
    private var fraudRisk: Boolean = false

    companion object {
        private const val TAG = "AirRobeMultiOptIn"
    }

    init {
        inflate(context, R.layout.airrobe_confirmation, this)
        binding = AirrobeConfirmationBinding.bind(this)
        setupAttributes(attrs)
    }

    private fun setupAttributes(attrs: AttributeSet?) {
        val typedArray = context.theme.obtainStyledAttributes(attrs, R.styleable.AirRobeConfirmation, 0, 0)
        orderId   = typedArray.getString(R.styleable.AirRobeConfirmation_orderId)
        email     = typedArray.getString(R.styleable.AirRobeConfirmation_email)
        fraudRisk = typedArray.getBoolean(R.styleable.AirRobeConfirmation_fraudRisk, false)

        widgetInstance.setInstanceChangeListener(this)
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
        initializeConfirmationWidget()
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
        emailCheckController.airRobeEmailCheckListener = this
        emailCheckController.start(email)
    }

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

    override fun onCategoryModelChange() {
        post {
            initializeConfirmationWidget()
        }
    }

    override fun onConfigChange() {
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
}