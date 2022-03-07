# AirRobeWidget

The AirRobeWidget Android SDK provides Circular Wardrobe widgets for your native Android application.

## Requirements

- Android 5.0 (API level 21) and above

## Installation

### Manual

Currently, we do not support any other installation methods, so for now the AirRobe SDK has to be manually integrated into your project.

#### GitHub Release

Download the [latest release][latest-release] source zip from GitHub and unzip into to any temporary directory outside your project directory.

#### Project Integration

Now that the AirRobeWidget SDK resides in your temporary directory, you can add this SDK as a module to your project with the following steps:

- Open your project on [Android Studio][android-studio] IDE.
- Import the module by going to `File -> New -> Import Module...`
- Choose the library named `AirRobeWidget` inside the downloaded repo by manually inputting the path or through the file browser window showing by tapping the `Browse` icon.
- Add the imported module to your `build.gradle`.

```gradle
dependencies {
    // Other dependencies are here
    implementation project(':AirRobeWidget')
}
```

You will need to keep track of the library version manually, and when you want to upgrade to a more recent version, you will need to manually download the library and replace it.

## ProGuard

If you are using ProGuard you might need to add [OkHttp's rules][okhttp-rules]

## Getting Started

The AirRobe SDK contains UI components that can be added to your product detail screens, cart screens, and order confirmation screens. The `AirRobeOptIn` component is intended to be used on a single product detail screens, the `AirRobeMultiOptIn` component on cart screen, and the `AirRobeConfirmation` component on the order confirmation screen.

### Integration

You are going to need your AirRobe `appId`, which is used to configure widgets and fetch up-to-date data mapping information. You can access your `appId` [here][here]

#### Initialization

The first step is to perform some global initialization of AirRobeWidgets:

```kotlin
import com.airrobe.widgetsdk.airrobewidget.AirRobeWidget
import com.airrobe.widgetsdk.airrobewidget.config.AirRobeWidgetConfig
import com.airrobe.widgetsdk.airrobewidget.config.Mode

AirRobeWidget.initialize(
    AirRobeWidgetConfig(
        appId = "APP_ID",           // required
        privacyPolicyURL = String,  // required - privacy policy url
        mode = enum                 // optional - (`Mode.PRODUCTION` or `Mode.SANDBOX`), default value is `Mode.PRODUCTION`
    )
)
```

##### Color configuration
```kotlin
AirRobeWidget.borderColor = Color.rgb(255, 255, 255) // the color of the widget border. default value is "#DFDFDF"
AirRobeWidget.arrowColor = Color.rgb(255, 255, 255) // the color of the widget drop down arrow icon. default value is "#42ABC8"
AirRobeWidget.textColor = Color.rgb(255, 255, 255) // the color of the widget text. default value is "#232323"
AirRobeWidget.switchColor = Color.rgb(255, 255, 255) // the color of the widget switch ON color. default value is "#42ABC8"
AirRobeWidget.buttonBackgroundColor = Color.rgb(255, 255, 255) // the color of the widget activate button background. default value is "#232323"
AirRobeWidget.buttonTextColor = Color.rgb(255, 255, 255) // the color of the widget activate button text. default value is "#FFFFFF"
AirRobeWidget.separatorColor = Color.rgb(255, 255, 255) // the color of the learn more dialog separators. default value is "#DFDFDF"
AirRobeWidget.linkTextColor = Color.rgb(255, 255, 255) // the color of the widget legal copy text. default value is "#696969"
```

> **Note:**
> The configuration must always be set when using the SDK, and before any included widgets are initialized.

### Widgets

AirRobe Widgets should be added by using XML or programmatically added inside your activity, and then configured using the `initialize` function, passing required arguments.

#### AirRobeOptIn Initialization

The AirRobeOptIn component should be added to your product details screen, passing arguments for the product's `category` and `price`, as well as `material` and `brand` if available. These attributes are used to fetch estimated resale price from the AirRobe Price Engine. Localisation attributes can also be provided.

_Note: The widget will not be displayed unless the product is AirRobe-eligible. The AirRobeWidget Android SDK considers a product's `category`, `price`, and `department` when making an eligibility determination._

##### In xml

```xml
<androidx.constraintlayout.widget.ConstraintLayout xmlns:android="http://schemas.android.com/apk/res/android"
    xmlns:airrobe="http://schemas.android.com/apk/res-auto"
    android:layout_width="match_parent"
    android:layout_height="match_parent">

    <com.airrobe.widgetsdk.airrobewidget.widgets.AirRobeOptIn
        android:layout_width="match_parent"
        android:layout_height="wrap_content"
        />
    
</androidx.constraintlayout.widget.ConstraintLayout>

```

##### Xml attributes
```xml
airrobe:borderColor="#DFDFDF" <!-- the color of the widget border. default value is "#DFDFDF" -->
airrobe:arrowColor="#42ABC8" <!-- the color of the widget drop down arrow icon. default value is "#42ABC8" -->
airrobe:linkTextColor="#696969" <!-- the color of the widget legal copy text. default value is "#696969" -->
airrobe:switchColor="#42ABC8" <!-- the color of the widget switch ON color. default value is "#42ABC8" -->
airrobe:textColor="#232323" <!-- the color of the widget text. default value is "#232323" -->
```

##### In class

```kotlin
val optInWidget = findViewById<AirRobeOptIn>(R.id.opt_in_widget)
optInWidget.initialize(
    brand = String?,                  // optional - e.g. "Chanel", can be null
    material = String?,               // optional - e.g. "Leather", can be null
    category = String,                // required - e.g. "Hats/fancy-hats"
    department = String?,             // optional - e.g. "Kidswear"
    priceCents = Double,              // required - e.g. 100.95f
    originalFullPriceCents = Double?, // optional - e.g. 62.00f, can be null
    rrpCents = Double?,               // optional - e.g. 62.00f, can be null
    currency = String,                // optional - default is "AUD"
    locale = String                   // optional - default is "en-AU"
)

// Color configuration
optInWidget.borderColor = Color.rgb(255, 255, 255)
optInWidget.textColor = Color.rgb(255, 255, 255)
optInWidget.switchColor = Color.rgb(255, 255, 255)
optInWidget.arrowColor = Color.rgb(255, 255, 255)
optInWidget.linkTextColor = Color.rgb(255, 255, 255)
```

#### AirRobeMultiOptIn Initialization

The AirRobeMultiOptIn component should be added to your cart screen. A list of product categories should be passed as an argument during initialisation.

_Note - The widget will not be displayed on the page if the following conditions are met:_

    - The items argument has an empty array as a value.
    - All the product categories passed in haven't been mapped to the categories in AirRobe. However, it will be displayed if any one of the categories have been mapped to AirRobe.

##### In xml

```xml
<androidx.constraintlayout.widget.ConstraintLayout xmlns:android="http://schemas.android.com/apk/res/android"
    xmlns:airrobe="http://schemas.android.com/apk/res-auto"
    android:layout_width="match_parent"
    android:layout_height="match_parent">

    <com.airrobe.widgetsdk.airrobewidget.widgets.AirRobeMultiOptIn
        android:layout_width="match_parent"
        android:layout_height="wrap_content"
        android:entries="@array/category_array_list" />

</androidx.constraintlayout.widget.ConstraintLayout>
```

##### Xml attributes
```xml
airrobe:borderColor="#DFDFDF" <!-- the color of the widget border. default value is "#DFDFDF" -->
airrobe:arrowColor="#42ABC8" <!-- the color of the widget drop down arrow icon. default value is "#42ABC8" -->
airrobe:linkTextColor="#696969" <!-- the color of the widget legal copy text. default value is "#696969" -->
airrobe:switchColor="#42ABC8" <!-- the color of the widget switch ON color. default value is "#42ABC8" -->
airrobe:textColor="#232323" <!-- the color of the widget text. default value is "#232323" -->
```

##### In class

```kotlin
val multiOptInWidget = findViewById<AirRobeMultiOptIn>(R.id.multi_opt_in_widget)
multiOptInWidget.initialize(
    items = arrayOf(String), // required - e.g. arrayOf("Accessories", "Accessories/Beauty", "Accessories/Bags/Leather bags/Weekender/Handbags", "Accessories/Bags/Clutches/Bum Bags")
)

// Color configuration
multiOptInWidget.borderColor = Color.rgb(255, 255, 255)
multiOptInWidget.textColor = Color.rgb(255, 255, 255)
multiOptInWidget.switchColor = Color.rgb(255, 255, 255)
multiOptInWidget.arrowColor = Color.rgb(255, 255, 255)
multiOptInWidget.linkTextColor = Color.rgb(255, 255, 255)
```

#### AirRobeConfirmation Initialization

The AirRobeConfirmation component should be added to the order confirmation screen in your app. While this component should always be rendered in the activity, it will only be visible to customers if they opted in to AirRobe for their order.

_Note - The widget will not be displayed on the page if the following conditions are met:_

    - If orderId or email are not passed in.
    - If the user doesn't opt in.

##### In xml

```xml
<androidx.constraintlayout.widget.ConstraintLayout xmlns:android="http://schemas.android.com/apk/res/android"
    xmlns:airrobe="http://schemas.android.com/apk/res-auto"
    android:layout_width="match_parent"
    android:layout_height="match_parent">

    <com.airrobe.widgetsdk.airrobewidget.widgets.AirRobeConfirmation
        android:layout_width="match_parent"
        android:layout_height="wrap_content"
        />

</androidx.constraintlayout.widget.ConstraintLayout>
```

##### Xml attributes
```xml
airrobe:borderColor="#DFDFDF" <!-- the color of the widget border. default value is "#DFDFDF" -->
airrobe:textColor="#232323" <!-- the color of the widget text. default value is "#232323" -->
airrobe:buttonBackgroundColor="#232323" <!-- the color of the widget activate button background. default value is "#232323" -->
airrobe:buttonTextColor="#FFFFFF" /> <!-- the color of the widget activate button text. default value is "#FFFFFF" -->
```

##### In class

```kotlin
val confirmationWidget = findViewById<AirRobeConfirmation>(R.id.confirmation_widget)
confirmationWidget.initialize(
    orderId = String,    // required - e.g. "123456" - the order id you got from the checkout.
    email = String,      // required
    fraudRisk = Boolean  // optional - fraud status for the confirmation widget, default value is false.
)

// Color configuration
confirmationWidget.borderColor = Color.rgb(255, 255, 255)
confirmationWidget.textColor = Color.rgb(255, 255, 255)
confirmationWidget.buttonBackgroundColor = Color.rgb(255, 255, 255)
confirmationWidget.buttonTextColor = Color.rgb(255, 255, 255)
```

##### Other Functionality

###### Clear Cache (Opt value reset)

`AirRobeWidget.resetOptedIn()`

###### Get Order-Opt-In value

`AirRobeWidget.orderOptedIn()`

# Examples

The [example project][example] demonstrates how to include AirRobeWidget UI components in a sample application.

[latest-release]: https://github.com/airrobe/widget-sdk-android/releases/latest
[example]: https://github.com/airrobe/widget-sdk-android/tree/master/demo
[android-studio]: https://developer.android.com/studio
[okhttp-rules]: https://github.com/square/okhttp/#r8--proguard
[here]: https://connector.airrobe.com/docs/android#android-getting_started-initialization
