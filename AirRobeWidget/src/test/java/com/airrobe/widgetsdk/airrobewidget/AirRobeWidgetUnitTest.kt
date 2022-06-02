package com.airrobe.widgetsdk.airrobewidget

import com.airrobe.widgetsdk.airrobewidget.service.models.*
import com.google.gson.Gson
import org.junit.Assert.assertNotNull
import org.junit.Assert.assertEquals
import org.junit.Test
import java.io.BufferedReader
import java.io.InputStreamReader
import java.util.stream.Collectors

class AirRobeWidgetUnitTest {
    @Test
    fun testJSONMapping() {
        val loader = ClassLoader.getSystemClassLoader()
        val br = BufferedReader(
            InputStreamReader(
                loader.getResourceAsStream("mappingInfo.json"), "UTF-8"
            )
        )
        val json: String = br.lines()
            .parallel()
            .collect(Collectors.joining())
        val shoppingData = Gson().fromJson(json, AirRobeGetShoppingDataModel::class.java)
        assertNotNull(shoppingData)
        widgetInstance.shopModel = shoppingData
        for (i in 0 until shoppingData.data.shop.categoryMappings.count()) {
            widgetInstance.categoryMapping.categoryMappingsHashmap[shoppingData.data.shop.categoryMappings[i].from] = shoppingData.data.shop.categoryMappings[i]
        }
    }

    private data class OptInInput(
        var category: String,
        var department: String?,
        var priceCents: Float,
    )

    private fun checkOptInEligibility(
        category: String,
        department: String? = null,
        priceCents: Float,
    ): Boolean {
        if (category.isEmpty()) {
            return false
        }
        val to = widgetInstance.categoryMapping.checkCategoryEligible(arrayListOf(category))
        return if (to != null) {
            !widgetInstance.shopModel!!.isBelowPriceThreshold(department, priceCents)
        } else {
            false
        }
    }

    @Test
    fun testOptInEligibility() {
        testJSONMapping()

        val expectedResult: ArrayList<Boolean> = optInExpectedResult
        val widgetInputs: ArrayList<OptInInput> = optInInput

        assertEquals(widgetInputs.count(), expectedResult.count())
        widgetInputs.zip(expectedResult).forEach { pair ->
            val result = checkOptInEligibility(
                pair.first.category,
                pair.first.department,
                pair.first.priceCents
            )
            assertEquals(result, pair.second)
        }
    }

    private val optInInput = arrayListOf(
        // With Department
        OptInInput("Accessories/Belts", "kidswear", 100.0f), // department as `kidswear` - should be true coz min price threshold is 29.9
        OptInInput("Accessories/Belts", "kidswear", 20.0f), // department as `kidswear` - should be false coz min price threshold is 29.9
        OptInInput("Accessories/Belts", "test Department", 100.0f), // department as `test Department` - should be true coz default min price threshold is 49.9
        OptInInput("Accessories/Belts", "test Department", 30.0f), // department as `test Department` - should be false coz default min price threshold is 49.9

        // Without Department
        OptInInput("Accessories/Travel and Luggage", null, 100.0f), //all case meets
        OptInInput("", null, 100.0f), //empty string for category
        OptInInput("Accessories/All Team Sports", null, 100.0f), //category input that `to` value is nil
        OptInInput("Accessories/Travel and Luggage/Test Category", null, 100.0f), // applied for best category mapping logic - should true
        OptInInput("Accessories/Underwear/Test Category", null, 100.0f), // applied for best category mapping logic - should be false because this category has `nil` for `to` value
        OptInInput("Accessories/Underwear & Socks", null, 100.0f), // all case meets except excluded is true - so should be false
        OptInInput("Accessories/Underwear & Socks/Test Category", null, 100.0f), // applied for best category mapping logic - should be false
    )

    private val optInExpectedResult = arrayListOf(
        true,
        false,
        true,
        false,

        true,
        false,
        false,
        true,
        false,
        false,
        false,
    )

    private data class MultiOptInInput(
        var items: ArrayList<String>,
    )

    @Test
    fun testMultiOptInEligibility() {
        testJSONMapping()

        val expectedResult: ArrayList<Boolean> = multiOptInExpectedResult
        val widgetInputs: ArrayList<MultiOptInInput> = multiOptInInput

        assertEquals(widgetInputs.count(), expectedResult.count())
        widgetInputs.zip(expectedResult).forEach { pair ->
            val result = AirRobeWidget.checkMultiOptInEligibility(pair.first.items)
            assertEquals(result, pair.second)
        }
    }

    private val multiOptInInput = arrayListOf(
        MultiOptInInput(arrayListOf("Accessories")), // Contain a category that meets condition
        MultiOptInInput(arrayListOf("Accessories", "Accessories/Belts")), // Contain multiple categories that meets condition
        MultiOptInInput(arrayListOf("Accessories", "RandomCategory")), // Contain 1 category that is in Mapping info that meets condition
        MultiOptInInput(arrayListOf("Accessories", "Accessories/Toys", "Root Category/Clothing/Sleepwear/Gowns/Loungewear")), // Contain only 1 category that is in Mapping info that meets condition, and the others with `to` value equals nil or `excluded` equals true

        MultiOptInInput(arrayListOf()), // empty category
        MultiOptInInput(arrayListOf("Accessories/Underwear & Socks")), // Contain 1 category that excluded equals true
        MultiOptInInput(arrayListOf("Accessories/Underwear & Socks", "RandomCategory")), // Contain a category that excluded equals true, and with random category
    )

    private val multiOptInExpectedResult = arrayListOf(
        true,
        true,
        true,
        true,

        false,
        false,
        false
    )

    private fun checkConfirmationEligibility(orderId: String, email: String, fraudRisk: Boolean, orderOptIn: Boolean): Boolean {
        if (orderId.isEmpty() || email.isEmpty()) {
            return false
        }
        return orderOptIn && !fraudRisk
    }

    private data class ConfirmationInput(
        var orderId: String,
        var email: String,
        var fraudRisk: Boolean,
        var orderOptedIn: Boolean
    )

    @Test
    fun testConfirmationEligibility() {
        testJSONMapping()

        val expectedResult: ArrayList<Boolean> = confirmationExpectedResult
        val widgetInputs: ArrayList<ConfirmationInput> = confirmationInput

        assertEquals(widgetInputs.count(), expectedResult.count())
        widgetInputs.zip(expectedResult).forEach { pair ->
            val result = checkConfirmationEligibility(
                pair.first.orderId,
                pair.first.email,
                pair.first.fraudRisk,
                pair.first.orderOptedIn
            )
            assertEquals(result, pair.second)
        }
    }

    private val confirmationInput = arrayListOf(
        ConfirmationInput("123456", "eli@airrobe.com", false, orderOptedIn = true), // orderOptedIn in cache is true, orderId, email available
        ConfirmationInput("123456", "eli@airrobe.com", false, orderOptedIn = false), // orderOptedIn in cache is false, orderId, email available

        ConfirmationInput("", "eli@airrobe.com", false, orderOptedIn = true), // orderId is empty string, email available
        ConfirmationInput("123456", "", false, orderOptedIn = true), // email is empty string, orderId available
        ConfirmationInput("", "", false, orderOptedIn = true), // both orderId and email are empty strings

        ConfirmationInput("123456", "eli@airrobe.com", true, orderOptedIn = true), // all good, but fraudRisk is true
    )

    private val confirmationExpectedResult = arrayListOf(
        true,
        false,

        false,
        false,
        false,

        false
    )
}