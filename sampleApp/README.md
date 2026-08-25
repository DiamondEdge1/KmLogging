# Kotlin Multiplatform Logging Sample App

## Android App demonstrates

* Adding a logger to send logs to Crashlytics
* Modifying builtin PlatformLogger to log based on Remote Config log level value

## Setup of Crashlytics and Remote Config

_IMPORTANT_: To build the sample app and see Crashlytics and Remote Config in operation, you will
need to add your own google-services.json into the androidApp directory.

In the Firebase console:

* add an app with a packageId of com.diamondedge.sample.
* In the Remote Config tab, add a parameter labelled log_level and set its default value to a string
  matching the enum names for LogLevel

Notes:

* Remote Config log levels would be useful if for example you wanted to turn logging on in release
  builds
  but only for certain users such as QA testers or for all environments prior to production.
* Remote Config doesn't have a way to tell you when they fetch new values so if you wanted a more
  dynamic level that would change while the app is running then you would have to call
  KmLogging.setupLoggingFlags()
  from a timer that would run at the same time interval as you have the Remote Config
  minimumFetchIntervalInSeconds set to.
* Other paid feature flag systems such as LaunchDarkly, ConfigCat, etc have ways to know when config
  changes occur
  so you will know when to call KmLogging.setupLoggingFlags() to update the log levels.

## Project structure

The sample is split into four modules:

* `:sampleApp:shared` — Kotlin Multiplatform library holding the shared Compose UI
  (`commonMain`) and platform code; produces the `Shared` iOS framework.
* `:sampleApp:androidApp` — Android application (`MainActivity`, `App`, launcher manifest).
* `:sampleApp:desktopApp` — JVM desktop application (`Main.kt`).
* `iosApp` — SwiftUI shell. The `.xcodeproj` is not checked in; see
  [iosApp/README.md](iosApp/README.md) for bring-up.

## Running the sample app

### Android

* Run `sampleApp.androidApp` from Android Studio as you would any other Android app.

### Desktop

* Run from the commandline using: ./gradlew :sampleApp:desktopApp:run

### Browser (WASM)

* Run from the commandline using: ./gradlew :sampleApp:shared:wasmJsBrowserRun

### iOS

* See [iosApp/README.md](iosApp/README.md).
