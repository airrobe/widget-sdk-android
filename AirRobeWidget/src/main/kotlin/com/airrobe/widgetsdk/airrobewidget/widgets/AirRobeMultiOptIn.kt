package com.airrobe.widgetsdk.airrobewidget.widgets

import android.content.Context
import android.text.SpannableString
import android.text.method.LinkMovementMethod
import android.text.style.ClickableSpan
import android.util.AttributeSet
import android.util.Log
import android.widget.LinearLayout
import com.airrobe.widgetsdk.airrobewidget.R
import com.airrobe.widgetsdk.airrobewidget.widgetInstance
import com.airrobe.widgetsdk.airrobewidget.config.WidgetInstance
import android.text.TextPaint

import android.graphics.Color
import android.graphics.PorterDuff
import android.text.Html
import android.text.Spanned
import android.view.View
import com.airrobe.widgetsdk.airrobewidget.databinding.AirrobeMultiOptInBinding
import com.airrobe.widgetsdk.airrobewidget.utils.SharedPreferenceManager

class AirRobeMultiOptIn @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : LinearLayout(context, attrs, defStyleAttr), WidgetInstance.InstanceChangeListener {
    private var binding: AirrobeMultiOptInBinding

    internal enum class ExpandType {
        Opened,
        Closed
    }
    private var expandType: ExpandType = ExpandType.Closed
    private var items: Array<CharSequence>? = arrayOf()

    companion object {
        private const val TAG = "AirRobeMultiOptIn"
    }

    init {
        inflate(context, R.layout.airrobe_multi_opt_in, this)
        binding = AirrobeMultiOptInBinding.bind(this)

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
        val detailedDescriptionText = SpannableString(context.resources.getString(R.string.detailed_description))
        val cs = object : ClickableSpan() {
            override fun updateDrawState(ds: TextPaint) {
                super.updateDrawState(ds)
                ds.isUnderlineText = true
            }

            override fun onClick(p0: View) {
                val dialog = AirRobeLearnMore(context)
                dialog.optInSwitch = binding.optInSwitch
                dialog.isFromMultiOptIn = true
                dialog.show()
            }
        }

        detailedDescriptionText.setSpan(cs, 229, 240, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE)
        binding.tvDetailedDescription.text = detailedDescriptionText
        binding.tvDetailedDescription.movementMethod = LinkMovementMethod.getInstance()
        if (widgetInstance.getConfig() != null) {
            binding.ivArrowDown.setColorFilter(Color.parseColor(widgetInstance.getConfig()?.color), PorterDuff.Mode.SRC_ATOP)
            binding.tvDetailedDescription.highlightColor = Color.parseColor(widgetInstance.getConfig()?.color)
            val extraInfoText = context.resources.getString(R.string.extra_info).replace("Privacy Policy", "<a href='${widgetInstance.getConfig()?.privacyPolicyURL}'>Privacy Policy</a>")
            binding.tvExtraInfo.text = Html.fromHtml(extraInfoText)
            binding.tvExtraInfo.movementMethod = LinkMovementMethod.getInstance()
            binding.tvExtraInfo.highlightColor = Color.parseColor(widgetInstance.getConfig()?.color)
        }

        binding.optInSwitch.isChecked = SharedPreferenceManager.getOptedIn(context)
        binding.optInSwitch.setOnCheckedChangeListener { _, isChecked ->
            SharedPreferenceManager.setOptedIn(context, isChecked)
            SharedPreferenceManager.setOrderOptedIn(context, isChecked)
        }

        setupAttributes(attrs)
    }

    private fun setupAttributes(attrs: AttributeSet?) {
        val typedArray = context.theme.obtainStyledAttributes(attrs, R.styleable.AirRobeMultiOptIn, 0, 0)
        items = typedArray.getTextArray(R.styleable.AirRobeMultiOptIn_android_entries)

        widgetInstance.setInstanceChangeListener(this)
        initializeOptInWidget()
    }

    fun initialize(
        items: Array<CharSequence> = arrayOf()
    ) {
        this.items = items
        initializeOptInWidget()
    }

    private fun initializeOptInWidget() {
        if (widgetInstance.getConfig() == null) {
            Log.e(TAG, "Widget sdk is not initialized yet")
            visibility = GONE
            SharedPreferenceManager.setOrderOptedIn(context, false)
            return
        }
        if (widgetInstance.getCategoryModel() == null) {
            Log.e(TAG, "Category Mapping Info is not loaded")
            visibility = GONE
            SharedPreferenceManager.setOrderOptedIn(context, false)
            return
        }
        if (items.isNullOrEmpty()) {
            Log.e(TAG, "Required params can't be empty")
            visibility = GONE
            SharedPreferenceManager.setOrderOptedIn(context, false)
            return
        }

        val newItems = arrayListOf<String>()
        for (item in items!!) {
            newItems.add(item.toString())
        }
        val to = widgetInstance.getCategoryModel()!!.checkCategoryEligible(newItems)
        if (to != null) {
            visibility = VISIBLE
            SharedPreferenceManager.setOrderOptedIn(context, SharedPreferenceManager.getOptedIn(context))
        } else {
            visibility = GONE
            SharedPreferenceManager.setOrderOptedIn(context, false)
            Log.d(TAG, "Category is not eligible")
        }
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
            val extraInfoText = context.resources.getString(R.string.extra_info).replace("Privacy Policy", "<a href='${widgetInstance.getConfig()?.privacyPolicyURL}'>Privacy Policy</a>")
            binding.tvExtraInfo.text = Html.fromHtml(extraInfoText)
            binding.tvExtraInfo.movementMethod = LinkMovementMethod.getInstance()
            binding.tvExtraInfo.highlightColor = Color.parseColor(widgetInstance.getConfig()?.color)
        }
    }
}