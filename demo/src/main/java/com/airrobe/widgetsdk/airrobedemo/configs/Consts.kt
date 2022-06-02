package com.airrobe.widgetsdk.airrobedemo.configs

import com.airrobe.widgetsdk.airrobedemo.R
import com.airrobe.widgetsdk.airrobedemo.models.BrandModel
import com.airrobe.widgetsdk.airrobedemo.models.CategoryModel
import com.airrobe.widgetsdk.airrobedemo.models.ItemModel

class Consts {
    companion object {
        val products = arrayListOf (
            ItemModel(R.drawable.bag1, "Aquila", "Montoro Messenger", "$340.00", "Accessories"),
            ItemModel(R.drawable.bag2, "Fjallraven", "Kanken Totepack", "$174.95", "Accessories"),
            ItemModel(R.drawable.bag3, "Stale Superior", "Downtown Weekender", "$59.99", "Accessories"),
            ItemModel(R.drawable.bag4, "Stale Superior", "Downtown Weekender", "$59.99", "Accessories"),
            ItemModel(R.drawable.gift1, "Happy Socks", "Get Set 24 days Of Holiday Socks", "$399.00", "Accessories/Beauty"),
            ItemModel(R.drawable.gift2, "Happy Socks", "Get Set Sports 3-Pack", "$54.95", "Accessories/Beauty"),
            ItemModel(R.drawable.gift3, "Tissot", "Supersport Gent", "$500.00", "Accessories/Beauty"),
            ItemModel(R.drawable.gift4, "Typo", "Art Gift Set", "$49.99", "Accessories/Beauty"),
        )

        val brands = arrayListOf (
            BrandModel("Women", R.drawable.woman),
            BrandModel("Men", R.drawable.man),
            BrandModel("Kids", R.drawable.toy),
            BrandModel("Beauty", R.drawable.beauty),
            BrandModel("Sport", R.drawable.sports),
            BrandModel("Home", R.drawable.home),
        )

        val categories = arrayListOf (
            CategoryModel("New Arrivals", R.drawable.new_arrival),
            CategoryModel("Clothing", R.drawable.clothing),
            CategoryModel("Sport", R.drawable.sports),
            CategoryModel("Shoes", R.drawable.shoes),
            CategoryModel("Accessories", R.drawable.accessories),
            CategoryModel("Gifts", R.drawable.gifts),
            CategoryModel("Designer", R.drawable.designer),
            CategoryModel("Essentials", R.drawable.essentials),
            CategoryModel("Denim", R.drawable.denim),
            CategoryModel("Sale", R.drawable.sales),
        )

        val subCategories = arrayListOf (
            "All Clothing",
            "T-shirt & Singlets",
            "Shirts & Polos",
            "Pants",
            "Sweats & Hoodies",
            "Shorts",
            "Coats & Jackets",
            "Jeans",
            "Swimwear",
            "Suits & Blazers",
            "Jumpers & Cardigans",
            "Sleepwear",
            "Underwear & Socks",
            "Tops",
        )
    }
}