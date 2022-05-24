package com.airrobe.widgetsdk.airrobewidget.service.listeners

interface AirRobeEventListener {
    fun onEventEmitted(event: AirRobeEventData)
}

data class AirRobeEventData(
    var app_id: String,
    var anonymous_id: String,
    var session_id: String,
    var event_name: String,
    var source: String,
    var version: String,
    var split_test_variant: String,
    var page_name: String
)