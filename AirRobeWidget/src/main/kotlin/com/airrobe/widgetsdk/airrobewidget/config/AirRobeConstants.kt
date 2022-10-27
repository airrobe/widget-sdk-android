package com.airrobe.widgetsdk.airrobewidget.config

internal enum class Connector(val raw: String) {
    Sandbox("https://sandbox.connector.airrobe.com"),
    Production("https://connector.airrobe.com")
}

internal enum class TelemetryEventHost(val raw: String) {
    Sandbox("https://events.stg.airdemo.link"),
    Production("https://events.airrobe.com")
}

internal class AirRobeConstants {
    companion object {
        const val FLOAT_NULL_MAGIC_VALUE = 0.000003f
        const val INT_NULL_MAGIC_VALUE = -987654321
        const val PRICE_ENGINE_HOST = "https://price-engine.airrobe.com"
        const val EMAIL_CHECK_HOST = "https://shop.airrobe.com"
        const val ORDER_ACTIVATE_BASE_URL = "https://shop.airrobe.com/en/orders/"
        const val ORDER_ACTIVATE_SANDBOX_BASE_URL = "https://stg.marketplace.airdemo.link/en/orders/"
    }
}

internal enum class AirRobeVariants(val raw: String) {
    Default("default"),
    Enhanced("enhanced")
}

enum class EventName(val raw: String) {
    PageView("airrobe-pageview"),
    WidgetRender("airrobe-widget-render"),
    WidgetNotRendered("airrobe-widget-not-rendered"),
    OptIn("airrobe-widget-opt-in"),
    OptOut("airrobe-widget-opt-out"),
    Expand("airrobe-widget-expand"),
    Collapse("airrobe-widget-collapse"),
    PopupOpen("airrobe-widget-popup-open"),
    PopupClose("airrobe-widget-popup-close"),
    ConfirmationRender("airrobe-confirmation-render"),
    ConfirmationClick("airrobe-confirmation-click");

    companion object {
        fun getByValue(value: String) = values().find { it.raw == value }
    }
}

enum class TelemetryEventName(val raw: String) {
    PageView("pageview"),
    OptIn("Opted in to AirRobe"),
    OptOut("Opted out of AirRobe"),
    Expand("Widget Expand Arrow Click"),
    PopupOpen("Pop up click"),
    ConfirmationClick("Claim link click");

    companion object {
        fun getByValue(value: String) = values().find { it.raw == value }
    }
}

enum class PageName(val raw: String) {
    Product("Product"),
    Cart("Cart"),
    ThankYou("Thank You"),
    Other("Other");

    companion object {
        fun getByValue(value: String) = values().find { it.raw == value }
    }
}