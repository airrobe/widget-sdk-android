package com.airrobe.widgetsdk.airrobewidget.service.models

import android.content.Context
import com.airrobe.widgetsdk.airrobewidget.utils.AirRobeSharedPreferenceManager

internal data class AirRobeGetShoppingDataModel (
    var data: AirRobeShoppingDataModel
) {
    fun isBelowPriceThreshold(department: String?, price: Float) : Boolean {
        if (department.isNullOrEmpty()) return false
        val applicablePriceThreshold = data.shop.minimumPriceThresholds.firstOrNull {
            it.department?.lowercase() == department.lowercase()
        } ?: data.shop.minimumPriceThresholds.firstOrNull {
            it.default
        } ?: return false

        return price < (applicablePriceThreshold.minimumPriceCents / 100)
    }

    fun getSplitTestVariant(context: Context) : AirRobeWidgetVariant? {
        val testVariant = AirRobeSharedPreferenceManager.getSplitTestVariant(context)
        if (testVariant != null && data.shop.widgetVariants.contains(testVariant)) {
            return testVariant
        }
        if (data.shop.widgetVariants.isNotEmpty()) {
            val newVariant = data.shop.widgetVariants.random()
            AirRobeSharedPreferenceManager.setSplitTestVariant(context, newVariant)
            return newVariant
        }
        AirRobeSharedPreferenceManager.setSplitTestVariant(context, null)
        return null
    }
}

internal data class AirRobeCategoryMappingHashMap(
    var categoryMappingsHashmap: HashMap<String, AirRobeCategoryMapping>
) {
    fun checkCategoryEligible(items: ArrayList<String>): String? {
        val eligibleItem = items.firstOrNull { bestCategoryMapping(factorize(it)) != null }
        return if (eligibleItem.isNullOrEmpty()) {
            null
        } else {
            bestCategoryMapping(factorize(eligibleItem))
        }
    }

    private fun factorize(category: String, delimiter: String = "/"): List<String> {
        val parts = category.split(delimiter)
        val array: ArrayList<String> = arrayListOf()
        for(i in parts.indices) {
            array.add(parts.slice(0..i).joinToString(delimiter))
        }
        return array.reversed()
    }

    private fun bestCategoryMapping(categoryArray: List<String>): String? {
        for (category in categoryArray) {
            val filteredMapping = categoryMappingsHashmap[category]
            if (filteredMapping != null) {
                return if (filteredMapping.to.isNullOrEmpty() || filteredMapping.excluded) {
                    null
                } else {
                    filteredMapping.to
                }
            }
        }
        return null
    }
}

internal data class AirRobeShoppingDataModel(
    var shop: AirRobeShopModel
)

internal data class AirRobeShopModel(
    var companyName: String,
    var privacyUrl: String?,
    var popupFindOutMoreUrl: String,
    var categoryMappings: MutableList<AirRobeCategoryMapping>,
    var minimumPriceThresholds: MutableList<AirRobeMinPriceThresholds>,
    var widgetVariants: MutableList<AirRobeWidgetVariant>
)

internal data class AirRobeCategoryMapping(
    var from: String,
    var to: String?,
    var excluded: Boolean
)

internal data class AirRobeMinPriceThresholds(
    var minimumPriceCents: Double,
    var department: String?,
    var default: Boolean
)

internal data class AirRobeWidgetVariant(
    var disabled: Boolean,
    var splitTestVariant: String?
)