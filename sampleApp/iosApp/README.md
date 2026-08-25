# iOS App — Bring-up

The `iosApp.xcodeproj` is **not** checked into version control. Only the SwiftUI
source shell is tracked:

```
iosApp/
├── iosApp/
│   ├── iOSApp.swift          # @main App entry point
│   ├── ContentView.swift     # hosts the Compose UI via the Shared framework
│   ├── Info.plist
│   └── Assets.xcassets/
├── Configuration/
│   └── Config.xcconfig       # TEAM_ID / BUNDLE_ID / APP_NAME
└── README.md
```

The Compose UI is delivered by the `:sampleApp:shared` Kotlin Multiplatform module,
which produces a static framework named **`Shared`**. `ContentView.swift` does
`import Shared` and calls `MainViewControllerKt.MainViewController()`.

## Creating the Xcode project

1. In Xcode: **File ▸ New ▸ Project… ▸ iOS App**.
    - Product Name: `iosApp`
    - Interface: **SwiftUI**, Language: **Swift**
    - Save it inside this `iosApp/` directory (so the `.xcodeproj` sits next to
      `iosApp/`, `Configuration/`, and this README).
2. Delete the auto-generated `ContentView.swift` / `*App.swift` and instead add
   the tracked sources from `iosApp/iosApp/` (`iOSApp.swift`, `ContentView.swift`),
   plus `Info.plist` and `Assets.xcassets`.
3. Point the target at the shared config: **Target ▸ Build Settings ▸ Configurations**
   and set the project/target to use `Configuration/Config.xcconfig`. Fill in your
   Apple `TEAM_ID` there.

## Wiring the Compose framework

Add a **Run Script** build phase (before *Compile Sources*) named
"Compile Kotlin Framework" with:

```sh
if [ "YES" = "$OVERRIDE_KOTLIN_BUILD_IDE_SUPPORTED" ]; then
  echo "Skipping Gradle build task invocation due to OVERRIDE_KOTLIN_BUILD_IDE_SUPPORTED environment variable set to \"YES\""
  exit 0
fi
cd "$SRCROOT/../.."
./gradlew :sampleApp:shared:embedAndSignAppleFrameworkForXcode
```

Then add the framework search path under **Build Settings ▸ Framework Search Paths**:

```
$(SRCROOT)/../shared/build/xcode-frameworks/$(CONFIGURATION)/$(SDK_NAME)
```

and link the generated `Shared.framework` (Build Phases ▸ Link Binary With Libraries).

> Tip: if you use [KMP for the Xcode project generation], the simplest path is to
> start from a fresh Compose Multiplatform iOS template and repoint the script/search
> path at `:sampleApp:shared` as shown above.

## Running

Build the framework first, then run from Xcode:

```sh
./gradlew :sampleApp:shared:linkDebugFrameworkIosSimulatorArm64
```

Select an iOS Simulator in Xcode and **Run**.