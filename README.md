# AirRobeWidget
The AirRobeWidget Android SDK provides conveniences to make your AirRobeWidget integration experience as smooth and straightforward as possible. We're working on crafting a great framework for developers with easy drop in components to integrate our widgets easy for your customers.

## Requirements
- Android 5.0 (API level 21) and above

## Installation

### Manual

Currently, we do not support any of other installation methods, so for now choose to manually integrate the AirRobe SDK into your project.
> **Note:**
> We assume that you already have the access to our Repo since its access is restricted for purposes.

#### GitHub Release

Download the [latest release][latest-release] source zip from GitHub and unzip into to any temporary directory outside your project directory.

#### Project Integration
Now that the AirRobeWidget sdk resides in your temporary directory outside your project directory. You can add this sdk as a module to your project with the following steps:
- Open your project on [Android Studio][Android-Studio] IDE.
- Import the module by going to `File -> New -> Import Module...`
- Choose the library named `AirRobeWidget` inside the downloaded repo by manually inputting the path or through the file browser window showing by tapping the `Browse` icon.
- Add the imported module to your `build.gradle`.
```gradle
dependencies {
    // Other dependencies are here
    implementation project(':AirRobeWidget')
}
```

You will have to keep track of the library version manually, and when you want to upgrade to a more recent version, you will have to manually download the library and replace it.

## ProGuard
If you are using ProGuard you might need to add OkHttp's rules: https://github.com/square/okhttp/#r8--proguard


## Getting Started

The AirRobe SDK contains UI components for Opt-In, Multi-Opt-In Views as well as Confirmation View.

### Integration

You are going to need `AppId` from the Provider which will be used to get the Category Mapping Infos and initialize the sub-widgets.

#### Initialization

```kotlin
AirRobeWidget.initialize(
    AirRobeWidgetConfig(
        appId = "APP_ID",           // required
        privacyPolicyURL = String,  // required - privacy policy url of The Iconic
        color = String,             // optional - color HexCode, default value is "#42abc8"
        mode = enum                 // optional - (`Mode.production` or `Mode.sandbox`), default value is `Mode.production`
    )
)
```

> **Note:**
> The configuration must always be set when using the SDK, and before any included widgets are initialized.


### Widgets
Basically, there are 2 traditional ways to initialize the widgets, i.e. one through the XML layout design files, and the other one to use the function of the widget.

#### Opt-In View Initialization
##### Through XML

```xml
<androidx.constraintlayout.widget.ConstraintLayout xmlns:android="http://schemas.android.com/apk/res/android"
    xmlns:app="http://schemas.android.com/apk/res-auto"
    android:layout_width="match_parent"
    android:layout_height="match_parent">

    <com.airrobe.widgetsdk.airrobewidget.widgets.AirRobeOptIn
        android:layout_width="match_parent"
        android:layout_height="wrap_content"
        app:brand="String"
        app:material="String"
        app:category="String"
        app:priceCents="Float"
        app:originalFullPriceCents="Float"
        app:rrpCents="Float"
        app:currency="String"
        app:locale="String" />

</androidx.constraintlayout.widget.ConstraintLayout>

```
##### Through Function
```kotlin
val optInWidget = findViewById<AirRobeOptIn>(R.id.opt_in_widget)
optInWidget.initialize(
    brand = String?,                  // optional - e.g. "Chanel", can be nil
    material = String?,               // optional - e.g. "Leather", can be nil
    category = String,                // required - e.g. "Hats/fancy-hats"
    priceCents = Double,              // required - e.g. 100.95
    originalFullPriceCents = Double?, // optional - e.g. 62.00, can be nil
    rrpCents = Double?,               // optional - e.g. 62.00, can be nil
    currency = String,                // optional - default is "AUD"
    locale = String                   // optional - default is "en-AU"
)
```

#### Multi-Opt-In View Initialization
##### Through XML

```xml
<androidx.constraintlayout.widget.ConstraintLayout xmlns:android="http://schemas.android.com/apk/res/android"
    android:layout_width="match_parent"
    android:layout_height="match_parent">

    <com.airrobe.widgetsdk.airrobewidget.widgets.AirRobeMultiOptIn
        android:layout_width="match_parent"
        android:layout_height="wrap_content"
        android:entries="@array/category_array_list" />

</androidx.constraintlayout.widget.ConstraintLayout>
```

##### Through Function
```kotlin
val multiOptInWidget = findViewById<AirRobeMultiOptIn>(R.id.multi_opt_in_widget)
multiOptInWidget.initialize(
    items = arrayOf(String), // required - e.g. arrayOf("Accessories", "Accessories/Beauty", "Accessories/Bags/Leather bags/Weekender/Handbags", "Accessories/Bags/Clutches/Bum Bags")
)
```

#### Confirmation View Initialization
##### Through XML

```xml
<androidx.constraintlayout.widget.ConstraintLayout xmlns:android="http://schemas.android.com/apk/res/android"
    xmlns:app="http://schemas.android.com/apk/res-auto"
    android:layout_width="match_parent"
    android:layout_height="match_parent">

    <com.airrobe.widgetsdk.airrobewidget.widgets.AirRobeConfirmation
        android:layout_width="match_parent"
        android:layout_height="wrap_content"
        app:orderId="String"
        app:email="String"
        app:fraudRisk="Boolean" />

</androidx.constraintlayout.widget.ConstraintLayout>
```

##### Through Function
```kotlin
val confirmationWidget = findViewById<AirRobeConfirmation>(R.id.confirmation_widget)
confirmationWidget.initialize(
    orderId = String,    // required - e.g. "123456" - the order id you got from the checkout.
    email = String,      // required
    fraudRisk = Boolean  // optional - fraud status for the confirmation widget, default value is false.
)
```

# Examples

The [example project][example] demonstrates how to include AirRobeWidget UI components.

[latest-release]: https://github.com/airrobe/widget-sdk-android/releases/latest
[example]: https://github.com/airrobe/widget-sdk-android/tree/master/demo
[Android-Studio]: https://developer.android.com/studio
