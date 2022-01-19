package com.airrobe.widgetsdk.airrobewidget.config

enum class Mode {
    PRODUCTION,
    SANDBOX
}

interface AirRobeWidgetConfig {
    val appId: String
    val privacyPolicyURL: String
    val color: String
    val mode: Mode
}

internal class AirRobeWidgetConfigImpl (
    override val appId: String,
    override val privacyPolicyURL: String,
    override val color: String,
    override val mode: Mode
) : AirRobeWidgetConfig

/**
 * Create a [AirRobeWidgetConfig] instance.
 *
 * @param appId App ID from https://connector.airrobe.com
 * @param privacyPolicyURL Privacy Policy link for the Iconic
 * @param color Primary color for the widgets
 * @param mode Selector for production or sandbox mode
 */
fun AirRobeWidgetConfig (
    appId: String = "",
    privacyPolicyURL: String = "",
    color: String = "#42abc8",
    mode: Mode = Mode.PRODUCTION
) : AirRobeWidgetConfig = AirRobeWidgetConfigImpl(
    appId, privacyPolicyURL, color, mode
)