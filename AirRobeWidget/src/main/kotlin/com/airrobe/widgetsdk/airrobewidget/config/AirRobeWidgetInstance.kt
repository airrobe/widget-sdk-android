package com.airrobe.widgetsdk.airrobewidget.config

import com.airrobe.widgetsdk.airrobewidget.R
import com.airrobe.widgetsdk.airrobewidget.service.models.CategoryModel

internal object AirRobeWidgetInstance {
    private var categoryModel: CategoryModel? = null
    private var configuration: AirRobeWidgetConfig? = null
    var changeListener: InstanceChangeListener? = null

    var borderColor: Int = R.color.airrobe_widget_default_border_color
    var textColor: Int = R.color.airrobe_widget_default_text_color
    var switchColor: Int = R.color.airrobe_widget_default_switch_color
    var arrowColor: Int = R.color.airrobe_widget_default_arrow_color
    var linkTextColor: Int = R.color.airrobe_widget_default_link_text_color
    var buttonBackgroundColor: Int = R.color.airrobe_widget_default_button_background_color
    var buttonTextColor: Int = R.color.airrobe_widget_default_button_text_color
    var separatorColor: Int = R.color.airrobe_widget_default_separator_color

    fun getCategoryModel(): CategoryModel? {
        return categoryModel
    }

    fun setCategoryModel(categoryModel: CategoryModel) {
        this.categoryModel = categoryModel
        changeListener?.onCategoryModelChange()
    }

    fun getConfig(): AirRobeWidgetConfig? {
        return configuration
    }

    fun setConfig(configuration: AirRobeWidgetConfig) {
        this.configuration = configuration
        changeListener?.onConfigChange()
    }

    fun setInstanceChangeListener(listener: InstanceChangeListener) {
        this.changeListener = listener
    }

    interface InstanceChangeListener {
        fun onCategoryModelChange()
        fun onConfigChange()
    }
}