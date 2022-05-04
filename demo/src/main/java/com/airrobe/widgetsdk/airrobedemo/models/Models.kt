package com.airrobe.widgetsdk.airrobedemo.models

data class BrandModel (
    var brand: String,
    var image: Int,
)

data class CategoryModel (
    var category: String,
    var image: Int,
)

data class ItemModel (
    var image: Int,
    var title: String,
    var subTitle: String,
    var price: String,
    var category: String,
)
