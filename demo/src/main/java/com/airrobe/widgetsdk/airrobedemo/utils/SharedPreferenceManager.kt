package com.airrobe.widgetsdk.airrobedemo.utils

import android.content.Context
import com.airrobe.widgetsdk.airrobedemo.models.ItemModel
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import java.lang.reflect.Type

object SharedPreferenceManager {
    private const val CartItemsKey = "CartItemsKey"
    private const val PREFERENCES_FILENAME = "demo_shared_preference"

    fun setCartItems(context: Context, value: ArrayList<ItemModel>) {
        val sp = context.getSharedPreferences(PREFERENCES_FILENAME, Context.MODE_PRIVATE)
        val editor = sp.edit()
        val gson = Gson()
        val jsonString = gson.toJson(value)
        editor.putString(CartItemsKey, jsonString)
        editor.apply()
    }

    fun getCartItems(context: Context): ArrayList<ItemModel> {
        val gson = Gson()
        val jsonString = context.getSharedPreferences(PREFERENCES_FILENAME, Context.MODE_PRIVATE).getString(CartItemsKey, null) ?: return arrayListOf()
        val type: Type = object: TypeToken<ArrayList<ItemModel>>() {}.type
        return gson.fromJson(jsonString, type)
    }
}