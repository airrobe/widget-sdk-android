package com.airrobe.widgetsdk.airrobewidget.config

import com.airrobe.widgetsdk.airrobewidget.service.models.CategoryModel

internal object CategoryModelInstance {
    private var categoryModel: CategoryModel? = null
    lateinit var changeListener: CategoryModelChangeListener

    fun getCategoryModel(): CategoryModel? {
        return categoryModel
    }

    fun setCategoryModel(categoryModel: CategoryModel) {
        this.categoryModel = categoryModel
        if (changeListener != null) changeListener.onChange()
    }

    fun setListener(changeListener: CategoryModelChangeListener) {
        this.changeListener = changeListener
    }

    interface CategoryModelChangeListener {
        fun onChange()
    }
}