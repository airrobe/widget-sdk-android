package com.airrobe.widgetsdk.airrobewidget.service.models

internal data class CategoryModel (
    var data: DataModel,
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
        val categoryMappings = data.shop.categoryMappings
        for (category in categoryArray) {
            val filteredMapping = categoryMappings.firstOrNull { it.from == category }
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

internal data class DataModel(
    var shop: ShopModel
)

internal data class ShopModel(
    var categoryMappings: MutableList<CategoryMapping>
)

internal data class CategoryMapping(
    var from: String,
    var to: String?,
    var excluded: Boolean
)