package com.airrobe.widgetsdk.airrobewidget.widgets

import android.content.Context
import android.content.res.ColorStateList
import android.graphics.Color
import android.graphics.PorterDuff
import android.graphics.drawable.ColorDrawable
import android.graphics.drawable.GradientDrawable
import android.os.Build
import android.text.SpannableString
import android.text.method.LinkMovementMethod
import android.text.style.ClickableSpan
import android.util.AttributeSet
import android.util.Log
import android.widget.LinearLayout
import com.airrobe.widgetsdk.airrobewidget.R
import com.airrobe.widgetsdk.airrobewidget.widgetInstance
import com.airrobe.widgetsdk.airrobewidget.config.AirRobeWidgetInstance
import android.text.TextPaint

import android.text.Html
import android.text.Spanned
import android.view.View
import androidx.core.graphics.drawable.DrawableCompat
import com.airrobe.widgetsdk.airrobewidget.databinding.AirrobeMultiOptInBinding
import com.airrobe.widgetsdk.airrobewidget.utils.AirRobeSharedPreferenceManager

@Suppress("DEPRECATION")
class AirRobeMultiOptIn @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : LinearLayout(context, attrs, defStyleAttr), AirRobeWidgetInstance.InstanceChangeListener {
    private var binding: AirrobeMultiOptInBinding

    companion object {
        private const val TAG = "AirRobeMultiOptIn"
    }

    internal enum class ExpandType {
        Opened,
        Closed
    }
    private var expandType: ExpandType = ExpandType.Closed
    private var items: Array<CharSequence>? = arrayOf()

    var borderColor: Int = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M)
        context.getColor(widgetInstance.borderColor) else
        context.resources.getColor(widgetInstance.borderColor)
        set(value) {
            field = value
            val mainBackground = binding.llMainContainer.background as GradientDrawable
            mainBackground.setStroke(1, value)
            setSwitchColor()
        }

    var textColor: Int = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M)
        context.getColor(widgetInstance.textColor) else
        context.resources.getColor(widgetInstance.textColor)
        set(value) {
            field = value
            binding.tvTitle.setTextColor(value)
            binding.tvDescription.setTextColor(value)
            binding.tvDetailedDescription.setTextColor(value)
            binding.tvExtraInfo.setTextColor(value)
        }

    var switchColor: Int = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M)
        context.getColor(widgetInstance.switchColor) else
        context.resources.getColor(widgetInstance.switchColor)
        set(value) {
            field = value
            setSwitchColor()
        }

    var arrowColor: Int = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M)
        context.getColor(widgetInstance.arrowColor) else
        context.resources.getColor(widgetInstance.arrowColor)
        set(value) {
            field = value
            binding.ivArrowDown.setColorFilter(value, PorterDuff.Mode.SRC_ATOP)
        }

    var linkTextColor: Int = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M)
        context.getColor(widgetInstance.linkTextColor) else
        context.resources.getColor(widgetInstance.linkTextColor)
        set(value) {
            field = value
            binding.tvDetailedDescription.setLinkTextColor(value)
            binding.tvExtraInfo.setLinkTextColor(value)
        }

    init {
        inflate(context, R.layout.airrobe_multi_opt_in, this)
        binding = AirrobeMultiOptInBinding.bind(this)

        widgetInstance.setInstanceChangeListener(this)
        if (widgetInstance.getConfig() != null) {
            initialize()
        }

        setupAttributes(attrs)
    }

    private fun setupAttributes(attrs: AttributeSet?) {
        val typedArray = context.theme.obtainStyledAttributes(attrs, R.styleable.AirRobeMultiOptIn, 0, 0)
        borderColor = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            typedArray.getColor(R.styleable.AirRobeMultiOptIn_borderColor, context.getColor(widgetInstance.borderColor))
        } else {
            typedArray.getColor(R.styleable.AirRobeMultiOptIn_borderColor, context.resources.getColor(widgetInstance.borderColor))
        }
        textColor = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            typedArray.getColor(R.styleable.AirRobeMultiOptIn_textColor, context.getColor(widgetInstance.textColor))
        } else {
            typedArray.getColor(R.styleable.AirRobeMultiOptIn_textColor, context.resources.getColor(widgetInstance.textColor))
        }
        switchColor = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            typedArray.getColor(R.styleable.AirRobeMultiOptIn_switchColor, context.getColor(widgetInstance.switchColor))
        } else {
            typedArray.getColor(R.styleable.AirRobeMultiOptIn_switchColor, context.resources.getColor(widgetInstance.switchColor))
        }
        arrowColor = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            typedArray.getColor(R.styleable.AirRobeMultiOptIn_arrowColor, context.getColor(widgetInstance.arrowColor))
        } else {
            typedArray.getColor(R.styleable.AirRobeMultiOptIn_arrowColor, context.resources.getColor(widgetInstance.arrowColor))
        }
        linkTextColor = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            typedArray.getColor(R.styleable.AirRobeMultiOptIn_linkTextColor, context.getColor(widgetInstance.linkTextColor))
        } else {
            typedArray.getColor(R.styleable.AirRobeMultiOptIn_linkTextColor, context.resources.getColor(widgetInstance.linkTextColor))
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
        DrawableCompat.setTintList(DrawableCompat.wrap(binding.optInSwitch.thumbDrawable), ColorStateList(states, thumbColors))
        DrawableCompat.setTintList(DrawableCompat.wrap(binding.optInSwitch.trackDrawable), ColorStateList(states, trackColors))
    }

    private fun initialize() {
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
        setDetailedDescriptionText()
        setExtraInfoText()
        binding.optInSwitch.isChecked = AirRobeSharedPreferenceManager.getOptedIn(context)
        binding.optInSwitch.setOnCheckedChangeListener { _, isChecked ->
            AirRobeSharedPreferenceManager.setOptedIn(context, isChecked)
            AirRobeSharedPreferenceManager.setOrderOptedIn(context, isChecked)
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
                dialog.optInSwitch = binding.optInSwitch
                dialog.isFromMultiOptIn = false
                dialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
                dialog.show()
            }
        }

        val learnMoreText: String = context.resources.getString(R.string.airrobe_learn_more_link_text)
        val start = detailedDescriptionText.indexOf(learnMoreText)
        val end = start + learnMoreText.length
        detailedDescriptionText.setSpan(cs, start, end, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE)
        binding.tvDetailedDescription.text = detailedDescriptionText
        binding.tvDetailedDescription.movementMethod = LinkMovementMethod.getInstance()
    }

    private fun setExtraInfoText() {
        val extraInfoText = context.resources.getString(R.string.airrobe_extra_info).replace("Privacy Policy", "<a href='${widgetInstance.getConfig()?.privacyPolicyURL}'>Privacy Policy</a>")
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            binding.tvExtraInfo.text = Html.fromHtml(extraInfoText, Html.FROM_HTML_MODE_COMPACT)
        } else {
            binding.tvExtraInfo.text = Html.fromHtml(extraInfoText)
        }
        binding.tvExtraInfo.movementMethod = LinkMovementMethod.getInstance()
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
            AirRobeSharedPreferenceManager.setOrderOptedIn(context, false)
            return
        }
        if (widgetInstance.getCategoryModel() == null) {
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
        val to = widgetInstance.getCategoryModel()!!.checkCategoryEligible(newItems)
        if (to != null) {
            visibility = VISIBLE
            AirRobeSharedPreferenceManager.setOrderOptedIn(context, AirRobeSharedPreferenceManager.getOptedIn(context))
        } else {
            visibility = GONE
            AirRobeSharedPreferenceManager.setOrderOptedIn(context, false)
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
            initialize()
        }
    }
}