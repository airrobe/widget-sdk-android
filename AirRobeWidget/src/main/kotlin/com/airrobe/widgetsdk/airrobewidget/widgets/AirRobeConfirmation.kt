package com.airrobe.widgetsdk.airrobewidget.widgets

import android.content.Context
import android.util.AttributeSet
import android.util.Log
import android.widget.RelativeLayout
import com.airrobe.widgetsdk.airrobewidget.R
import com.airrobe.widgetsdk.airrobewidget.config.WidgetInstance
import com.airrobe.widgetsdk.airrobewidget.databinding.AirrobeConfirmationBinding
import com.airrobe.widgetsdk.airrobewidget.service.api_controllers.EmailCheckController
import com.airrobe.widgetsdk.airrobewidget.service.listeners.EmailCheckListener
import com.airrobe.widgetsdk.airrobewidget.utils.SharedPreferenceManager
import com.airrobe.widgetsdk.airrobewidget.widgetInstance

class AirRobeConfirmation @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : RelativeLayout(context, attrs, defStyleAttr), WidgetInstance.InstanceChangeListener, EmailCheckListener {
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
        if (SharedPreferenceManager.getOrderOptedIn(context) && !fraudRisk) {
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
        val emailCheckController = EmailCheckController()
        emailCheckController.emailCheckListener = this
        emailCheckController.start(email)
    }

    override fun onSuccessEmailCheckApi(isCustomer: Boolean) {
        binding.btnLoading.visibility = GONE
        if (isCustomer) {
            binding.tvAction.text = context.resources.getString(R.string.order_confirmation_visit_text)
        } else {
            binding.tvAction.text = context.resources.getString(R.string.order_confirmation_activate_text)
        }
    }

    override fun onFailedEmailCheckApi(error: String?) {
        binding.btnLoading.visibility = GONE
        binding.tvAction.text = context.resources.getString(R.string.order_confirmation_activate_text)
        Log.e(TAG, error ?: "Email Check Api Failed")
    }

    override fun onCategoryModelChange() {
        post {
            initializeConfirmationWidget()
        }
    }

    override fun onConfigChange() {
    }
}