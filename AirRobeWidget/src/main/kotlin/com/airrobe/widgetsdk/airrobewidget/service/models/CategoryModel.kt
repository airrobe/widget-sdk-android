package com.airrobe.widgetsdk.airrobewidget.service.models

data class CategoryModel (
    var data: DataModel,
)

data class DataModel(
    var shop: ShopModel
)

data class ShopModel(
    var categoryMappings: MutableList<CategoryMapping>
)

data class CategoryMapping(
    var from: String,
    var to: String?,
    var excluded: Boolean
)