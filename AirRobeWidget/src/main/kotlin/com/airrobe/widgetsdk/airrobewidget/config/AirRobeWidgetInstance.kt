package com.airrobe.widgetsdk.airrobewidget.config

import com.airrobe.widgetsdk.airrobewidget.service.models.AirRobeCategoryModel
import com.airrobe.widgetsdk.airrobewidget.service.models.AirRobeMinPriceThresholdsModel

internal object AirRobeWidgetInstance {
    var categoryModel: AirRobeCategoryModel? = null
        set(value) {
            field = value
            changeListener?.onCategoryModelChange()
        }
    var minPriceThresholdsModel: AirRobeMinPriceThresholdsModel? = null
        set(value) {
            field = value
            changeListener?.onMinPriceThresholdsChange()
        }
    var configuration: AirRobeWidgetConfig? = null
        set(value) {
            field = value
            changeListener?.onConfigChange()
        }
    var changeListener: InstanceChangeListener? = null

    var borderColor: Int = 0
    var textColor: Int = 0
    var switchColor: Int = 0
    var arrowColor: Int = 0
    var linkTextColor: Int = 0
    var buttonBorderColor: Int = 0
    var buttonTextColor: Int = 0
    var separatorColor: Int = 0

    interface InstanceChangeListener {
        fun onCategoryModelChange()
        fun onMinPriceThresholdsChange()
        fun onConfigChange()
    }
}