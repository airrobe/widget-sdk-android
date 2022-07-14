package com.airrobe.widgetsdk.airrobewidget.config

enum class Mode {
    PRODUCTION,
    SANDBOX
}

interface AirRobeWidgetConfig {
    val appId: String
    val mode: Mode
}

internal class AirRobeWidgetConfigImpl (
    override val appId: String,
    override val mode: Mode
) : AirRobeWidgetConfig

/**
 * Create a [AirRobeWidgetConfig] instance.
 *
 * @param appId App ID from https://connector.airrobe.com
 * @param mode Selector for production or sandbox mode
 */
fun AirRobeWidgetConfig (
    appId: String,
    mode: Mode = Mode.PRODUCTION
) : AirRobeWidgetConfig = AirRobeWidgetConfigImpl(
    appId, mode
)