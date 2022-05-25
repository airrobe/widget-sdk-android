package com.airrobe.widgetsdk.airrobewidget.config

internal class AirRobeConstants {
    companion object {
        const val FLOAT_NULL_MAGIC_VALUE = 0.000003f
        const val AIRROBE_CONNECTOR_SANDBOX = "https://sandbox.connector.airrobe.com"
        const val AIRROBE_CONNECTOR_PRODUCTION = "https://connector.airrobe.com"
        const val PRICE_ENGINE_HOST = "https://price-engine.airrobe.com"
        const val EMAIL_CHECK_HOST = "https://shop.airrobe.com"
        const val ORDER_ACTIVATE_BASE_URL = "https://shop.airrobe.com/en/orders/"
        const val ORDER_ACTIVATE_SANDBOX_BASE_URL = "https://stg.marketplace.airdemo.link/en/orders/"
    }
}

enum class EventName(val raw: String) {
    PageView("pageview"),
    WidgetNotRendered("Widget not rendered"),
    OptedIn("Opted in to AirRobe"),
    OptedOut("Opted out of AirRobe"),
    WidgetExpand("Widget Expand Arrow Click"),
    WidgetCollapse("Widget Collapse Arrow Click"),
    PopupClick("Pop up click"),
    ClaimLinkClick("Claim link click")
}

enum class PageName(val raw: String) {
    Product("Product"),
    Cart("Cart"),
    ThankYou("Thank You")
}