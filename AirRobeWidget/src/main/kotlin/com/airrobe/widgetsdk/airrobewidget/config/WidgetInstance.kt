package com.airrobe.widgetsdk.airrobewidget.config

import com.airrobe.widgetsdk.airrobewidget.service.models.CategoryModel

internal object WidgetInstance {
    private var categoryModel: CategoryModel? = null
    private var configuration: AirRobeWidgetConfig? = null
    lateinit var changeListener: InstanceChangeListener

    fun getCategoryModel(): CategoryModel? {
        return categoryModel
    }

    fun setCategoryModel(categoryModel: CategoryModel) {
        this.categoryModel = categoryModel
        changeListener.onCategoryModelChange()
    }

    fun getConfig(): AirRobeWidgetConfig? {
        return configuration
    }

    fun setConfig(configuration: AirRobeWidgetConfig) {
        this.configuration = configuration
        changeListener.onConfigChange()
    }

    fun setInstanceChangeListener(listener: InstanceChangeListener) {
        this.changeListener = listener
    }

    interface InstanceChangeListener {
        fun onCategoryModelChange()
        fun onConfigChange()
    }
}