package com.airrobe.widgetsdk.airrobewidget.widgets

import android.annotation.SuppressLint
import android.app.Dialog
import android.content.Context
import android.graphics.Color
import android.os.Bundle
import android.text.method.LinkMovementMethod
import android.view.*
import androidx.appcompat.widget.SwitchCompat
import com.airrobe.widgetsdk.airrobewidget.databinding.AirrobeDialogLearnMoreBinding
import com.airrobe.widgetsdk.airrobewidget.utils.AirRobeAppUtils
import com.airrobe.widgetsdk.airrobewidget.utils.AirRobeSharedPreferenceManager
import com.airrobe.widgetsdk.airrobewidget.widgetInstance

internal class AirRobeLearnMore(context: Context) : Dialog(context) {
    private lateinit var binding: AirrobeDialogLearnMoreBinding
    lateinit var optInSwitch: SwitchCompat
    var isFromMultiOptIn: Boolean = false

    init {
        setCancelable(true)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        requestWindowFeature(Window.FEATURE_NO_TITLE)
        binding = AirrobeDialogLearnMoreBinding.inflate(LayoutInflater.from(context))
        setContentView(binding.root)
        initView()
        onClick()
    }

    private fun initView() {
        binding.optInSwitch.isChecked = AirRobeSharedPreferenceManager.getOptedIn(context)
        binding.optInSwitch.setOnCheckedChangeListener { _, isChecked ->
            AirRobeSharedPreferenceManager.setOptedIn(context, isChecked)
            optInSwitch.isChecked = isChecked
            if (isFromMultiOptIn) {
                AirRobeSharedPreferenceManager.setOrderOptedIn(context, isChecked)
            }
        }

        binding.tvFindOutMore.movementMethod = LinkMovementMethod.getInstance()
        binding.tvFindOutMore.highlightColor = Color.parseColor(widgetInstance.getConfig()?.color)
    }

    @SuppressLint("ClickableViewAccessibility")
    private fun onClick() {
        binding.ivClose.setOnTouchListener { v, event ->
            if (AirRobeAppUtils.touchAnimator(context, v, event)) {
                dismiss()
            }
            true
        }
    }
}