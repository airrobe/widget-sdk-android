package com.airrobe.widgetsdk.airrobewidget

import android.content.Context
import android.graphics.PorterDuff
import android.graphics.drawable.GradientDrawable
import android.os.Build
import android.util.Log
import com.airrobe.widgetsdk.airrobewidget.config.AirRobeWidgetConfig
import com.airrobe.widgetsdk.airrobewidget.config.AirRobeWidgetInstance
import com.airrobe.widgetsdk.airrobewidget.service.api_controllers.AirRobeGetCategoryMappingController
import com.airrobe.widgetsdk.airrobewidget.service.listeners.AirRobeGetCategoryMappingListener
import com.airrobe.widgetsdk.airrobewidget.service.models.CategoryModel
import com.airrobe.widgetsdk.airrobewidget.utils.AirRobeSharedPreferenceManager

internal val widgetInstance = AirRobeWidgetInstance

object AirRobeWidget : AirRobeGetCategoryMappingListener {
    private const val TAG = "AirRobeWidget"

    var borderColor: Int = R.color.airrobe_widget_default_border_color
        set(value) {
            field = value
            widgetInstance.borderColor = value
        }
    var textColor: Int = R.color.airrobe_widget_default_text_color
        set(value) {
            field = value
            widgetInstance.textColor = value
        }
    var switchColor: Int = R.color.airrobe_widget_default_switch_color
        set(value) {
            field = value
            widgetInstance.switchColor = value
        }
    var arrowColor: Int = R.color.airrobe_widget_default_arrow_color
        set(value) {
            field = value
            widgetInstance.arrowColor = value
        }
    var linkTextColor: Int = R.color.airrobe_widget_default_link_text_color
        set(value) {
            field = value
            widgetInstance.linkTextColor = value
        }
    var buttonBackgroundColor: Int = R.color.airrobe_widget_default_button_background_color
        set(value) {
            field = value
            widgetInstance.buttonBackgroundColor = value
        }
    var buttonTextColor: Int = R.color.airrobe_widget_default_button_text_color
        set(value) {
            field = value
            widgetInstance.buttonTextColor = value
        }
    var separatorColor: Int = R.color.airrobe_widget_default_separator_color
        set(value) {
            field = value
            widgetInstance.separatorColor = value
        }

    fun initialize(
        config: AirRobeWidgetConfig
    ) {
        widgetInstance.setConfig(config)
        val getCategoryMappingController = AirRobeGetCategoryMappingController()
        getCategoryMappingController.airRobeGetCategoryMappingListener = this
        getCategoryMappingController.start(config.appId, config.mode)
    }

    fun resetOptedIn(context: Context) {
        AirRobeSharedPreferenceManager.setOptedIn(context, false)
    }

    fun orderOptedIn(context: Context): Boolean {
        return AirRobeSharedPreferenceManager.getOrderOptedIn(context)
    }

    override fun onSuccessGetCategoryMappingApi(categoryModel: CategoryModel) {
        widgetInstance.setCategoryModel(categoryModel)
    }

    override fun onFailedGetCategoryMappingApi(error: String?) {
        Log.e(TAG, error ?: "Email Check Api Failed")
    }
}