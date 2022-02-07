package com.airrobe.widgetsdk.airrobewidget.widgets

import android.annotation.SuppressLint
import android.app.Dialog
import android.content.Context
import android.graphics.Color
import android.os.Bundle
import android.text.method.LinkMovementMethod
import android.view.*
import androidx.appcompat.widget.SwitchCompat
import com.airrobe.widgetsdk.airrobewidget.databinding.DialogLearnMoreBinding
import com.airrobe.widgetsdk.airrobewidget.utils.AppUtils
import com.airrobe.widgetsdk.airrobewidget.utils.SharedPreferenceManager
import com.airrobe.widgetsdk.airrobewidget.widgetInstance

internal class AirRobeLearnMore(context: Context) : Dialog(context) {
    private lateinit var binding: DialogLearnMoreBinding
    lateinit var optInSwitch: SwitchCompat
    var isFromMultiOptIn: Boolean = false

    init {
        setCancelable(true)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        requestWindowFeature(Window.FEATURE_NO_TITLE)
        binding = DialogLearnMoreBinding.inflate(LayoutInflater.from(context))
        setContentView(binding.root)
        initView()
        onClick()
    }

    private fun initView() {
        binding.optInSwitch.isChecked = SharedPreferenceManager.getOptedIn(context)
        binding.optInSwitch.setOnCheckedChangeListener { _, isChecked ->
            SharedPreferenceManager.setOptedIn(context, isChecked)
            optInSwitch.isChecked = isChecked
            if (isFromMultiOptIn) {
                SharedPreferenceManager.setOrderOptedIn(context, isChecked)
            }
        }

        binding.tvFindOutMore.movementMethod = LinkMovementMethod.getInstance()
        binding.tvFindOutMore.highlightColor = Color.parseColor(widgetInstance.getConfig()?.color)
    }

    @SuppressLint("ClickableViewAccessibility")
    private fun onClick() {
        binding.ivClose.setOnTouchListener { v, event ->
            if (AppUtils.touchAnimator(context, v, event)) {
                dismiss()
            }
            true
        }
    }
}