# AirRobeWidget
The AirRobeWidget Android SDK provides conveniences to make your AirRobeWidget integration experience as smooth and straightforward as possible. We're working on crafting a great framework for developers with easy drop in components to integrate our widgets easy for your customers.

## Installation

### Requirements
- Android 5.0 (API level 21) and above

### Download
- Download the latest AAR from `Maven Central`.
- Grab it via gradle
```gradle
dependencies {
    implementation 'com.airrobe.widgetsdk'
}
```

## ProGuard
If you are using ProGuard you might need to add OkHttp's rules: https://github.com/square/okhttp/#r8--proguard


# Getting Started

The AirRobe SDK contains UI components for Opt-In, Multi-Opt-In Views as well as Confirmation View.


## Integration

You are going to need `AppId` from the Provider which will be used to get the Category Mapping Infos and initialize the sub-widgets.

### Initialization

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


## Widget
Basically, there are 2 traditional ways to initialize the widgets, i.e. one through the XML layout design files, and the other one to use the function of the widget.

### Opt-In View Initialization
#### Through XML

```xml
<com.airrobe.widgetsdk.airrobewidget.widgets.AirRobeOptIn
  android:id="@+id/opt_in_widget"
  android:layout_width="match_parent"
  android:layout_height="wrap_content"/>
```
