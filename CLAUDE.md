# CLAUDE.md

This file provides guidance to Claude Code (claude.ai/code) when working with code in this repository.

## Overview

Native Android SDK providing AirRobe widgets (OptIn, MultiOptIn, Confirmation) for integration into Android apps. Distributed via Maven Central.

## Tech Stack

- Kotlin 1.9.0, Android API 21+ (target 33)
- Gradle 7.4, Java 11
- GSON (only external dependency)
- JUnit 4, Espresso

## Commands

- **Build:** `./gradlew build`
- **Test (unit):** `./gradlew test`
- **Test (instrumented):** `./gradlew androidTest` (requires device/emulator)
- **Clean:** `./gradlew clean`
- **Publish local:** `./gradlew publishToMavenLocal`
- **Open demo:** Open `demo/` module in Android Studio

## Architecture

- `AirRobeWidget/src/main/kotlin/.../AirRobeWidget.kt` — Main SDK object (singleton entry point)
- `AirRobeWidget/.../widgets/` — Public views: AirRobeOptIn, AirRobeMultiOptIn, AirRobeConfirmation, AirRobeLearnMore
- `AirRobeWidget/.../service/` — HTTP client (raw HttpURLConnection), API controllers, response models
- `AirRobeWidget/.../config/` — Configuration, constants, event listener interface, widget instance state
- `AirRobeWidget/.../utils/` — SharedPreferences manager, telemetry, animations
- `AirRobeWidget/src/main/res/` — Layouts, drawables, animations, strings
- `AirRobeWidget/src/test/` — JUnit unit tests (eligibility logic, JSON mapping)
- `AirRobeWidget/src/androidTest/` — Espresso instrumented tests
- `demo/` — Demo app module

## Public API

```kotlin
AirRobeWidget.initialize(config: AirRobeWidgetConfig)  // Must call first
AirRobeWidget.trackPageView(context, pageName)
AirRobeWidget.checkMultiOptInEligibility(items) -> Boolean
AirRobeWidget.checkConfirmationEligibility(context, orderId, email, fraudRisk) -> Boolean
AirRobeWidget.resetOptedIn(context) / resetOrder(context) / orderOptedIn(context) -> Boolean
```

10 color properties (backgroundColor, borderColor, textColor, switchColor, arrowColor, linkTextColor, etc.).

## Conventions

- Maven Central: `com.airrobe:airrobe-widget-sdk:1.0.8`
- Listener-based async (callbacks, not Coroutines/RxJava)
- SharedPreferences (`airrobe_shared_preference`) for persisting opt-in state and A/B test variants
- Uses raw `HttpURLConnection` (not OkHttp/Retrofit)
- Permissions: INTERNET, ACCESS_NETWORK_STATE
- Build types: debug, staging, release
- No CI/CD configured

## Gotchas

- Magic null values: `0.000003f` (FLOAT_NULL_MAGIC_VALUE), `-987654321` (INT_NULL_MAGIC_VALUE)
- Price thresholds vary per department (e.g. kidswear = $29.9 min, default = $49.9)
- Some categories are excluded (e.g. "Underwear & Socks")
- Widget visibility is automatic — OptIn/MultiOptIn only show if eligible, Confirmation only shows if opted in and no fraud risk
