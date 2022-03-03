package com.airrobe.widgetsdk.airrobewidget.config

import com.airrobe.widgetsdk.airrobewidget.service.models.CategoryModel

internal object AirRobeWidgetInstance {
    private var categoryModel: CategoryModel? = null
    private var configuration: AirRobeWidgetConfig? = null
    var changeListener: InstanceChangeListener? = null

    var borderColor: Int = 0
    var textColor: Int = 0
    var switchColor: Int = 0
    var arrowColor: Int = 0
    var linkTextColor: Int = 0
    var buttonBackgroundColor: Int = 0
    var buttonTextColor: Int = 0
    var separatorColor: Int = 0

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