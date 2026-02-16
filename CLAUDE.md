# CLAUDE.md

This file provides guidance to Claude Code (claude.ai/code) when working with code in this repository.

## Overview

Native Android SDK providing AirRobe widgets (OptIn, MultiOptIn, Confirmation) for integration into Android apps. Distributed via Maven Central.

## Tech Stack

- Kotlin, Android API 21+
- Gradle, Maven Central

## Commands

- **Build:** `./gradlew build`
- **Test:** `./gradlew test`

## Architecture

- `AirRobeWidget/src/main/` — SDK source code
- `AirRobeWidget/src/test/` — unit tests
- `AirRobeWidget/src/androidTest/` — instrumented tests
- Widget components: AirRobeOptIn, AirRobeMultiOptIn, AirRobeConfirmation
