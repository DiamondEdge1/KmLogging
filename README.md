# Kotlin Multiplatform Logging  <img src="https://upload.wikimedia.org/wikipedia/commons/0/06/Kotlin_Icon.svg" width="30">  <img src="https://upload.wikimedia.org/wikipedia/commons/d/d7/Android_robot.svg" width="30">  <img src="https://upload.wikimedia.org/wikipedia/commons/6/66/Apple_iOS_logo.svg" width="30">  <img src="https://upload.wikimedia.org/wikipedia/commons/6/6a/JavaScript-logo.png" width="30">  <img src="https://upload.wikimedia.org/wikipedia/commons/1/18/OpenJDK_logo.svg" width="80">

[![ver](https://img.shields.io/maven-central/v/com.diamondedge/logging)](https://repo1.maven.org/maven2/com/diamondedge/logging/)
[![Kotlin](https://img.shields.io/badge/Kotlin-2.2.20-blue.svg?logo=kotlin)](http://kotlinlang.org)
![kmm](https://img.shields.io/badge/Multiplatform-Android%20iOS%20WasmJS%20JS%20JVM-blue)
[![License](https://img.shields.io/badge/License-Apache--2.0-blue)](http://www.apache.org/licenses/LICENSE-2.0)
[![libs.tech recommends](https://libs.tech/project/332284926/badge.svg)](https://libs.tech/project/332284926/kmlogging)

Kotlin multiplatform logging library targeting Android, iOS, JVM, WasmJS and JS.

## Features

* Uses the native logging facility on each platform: Log on Android, os_log on iOS, SLF4J on JVM and
  console on WasmJs and JavaScript.
* High performance. Very little overhead when logging is disabled. When disabled, only one boolean
  is evaluated and no function calls. Building the message string and running the code to calculate
  it is not executed.
* No configuration necessary.
* Can add additional loggers such as Crashlytics or replace/extend the builtin PlatformLogger with
  something else
* Can provide custom/configurable log level control on builtin PlatformLogger such as changing the
  log level from Remote Config
* Each logger can log at a different level.
* All platforms can use the same set of loggers by configuring in common code or can use different
  ones on each platform by configuring in platform specific code.
* It is thread-safe

## Migration from version 1.x

Version 2.0 changed the package from `org.lighthousegames.logging` to `com.diamondedge.logging`
To do the migration simply change the dependency to
`implementation("com.diamondedge:logging:$logging_version")`
and replace all occurrences of `org.lighthousegames.logging` in your code base with
`com.diamondedge.logging`.

## Setup

The library is available from the Maven Central repository with the current version
of ![ver](https://img.shields.io/maven-central/v/com.diamondedge/logging)
You should use version `2.0.21` or later of the kotlin multiplatform plugin (Older version of Kotlin
are supported in older versions of the library). Place the following in the commonMain section.

build.gradle.kts

```kotlin
sourceSets {
    val commonMain by getting {
        dependencies {
            api("com.diamondedge:logging:$logging_version")
        }
    }
}
```

build.gradle

```gradle
sourceSets {
    commonMain {
        dependencies {
            api "com.diamondedge:logging:$logging_version"
        }
    }
}
```

## Setup for non-multiplatform

So, you want the best and easiest to use kotlin logging library but are not yet ready for
multiplatform development then just include on the following in your `dependencies` section:

```gradle
    implementation("com.diamondedge:logging-android:$logging_version")
    implementation("com.diamondedge:logging-jvm:$logging_version")
    implementation("com.diamondedge:logging-js:$logging_version")
```

## Usage

Create an instance of logging class by using the convenience function `logging()`.
On Android, iOS and JVM the class from where `logging()` was called will be used as the tag in the
logs. For JS or when a specific tag is desired it can be supplied i.e `val log = logging("mytag")`
or `val log = KmLog("mytag")`

```kotlin
class MyClass {
    fun easyPeasy() {
        log.i { "use traditional Android short function name" }
    }

    fun easyPeasyLemonSqueesy() {
        log.info { "use longer more explicit function name" }
    }

    companion object {
        val log = logging()
    }
}
```

## Performance

There are 3 aspects to logging that have significant overhead:

1. calculating the tag or class name
1. formatting the log message
1. determining whether a given log call should be logged or not

KmLogging addresses and improves on each of these as compared to other logging libraries:

1. KmLogging calculates the tag just once per module The tag (class name) is only when
   the `val log = logging()` is executed. Most logging libraries calculate it on every log call or
   require you to supply it. Calculating the tag is expensive since it normally requires creating
   and parsing a call stack to get the class name. KmLogging chose to eliminate this performance
   drain by asking the developer to add an additional line of code `val log = logging()` to each
   class or module so this cost is only paid one time per class.
1. KmLogging does not evaluate the message string except when it will be output. Formatting the log
   message can require many steps and the concatenation of the strings is expensive. All of this
   code is captured in a lambda which is not executed unless it is determined that the result will
   be output to the logs.
1. KmLogging has one a single boolean check to determine if it should log. Calculating whether each
   level is to be logged is calculated at configuration time i.e. only once and then a boolean flag
   is stored for each level to signify whether that level is logging or not. The logging functions
   are inline and result in evaluating one boolean to determine if any logging work should be done.
   Most logging libraries have a lot of overhead such as many method calls and loops over the
   configuration objects to determine if logging should be performed or not.

Since KmLogging has very little overhead when it is disabled, if you turn off logging in release
builds you can leave a lot of logging in your code without paying performance penalties in
production.

Note: if any logger has a given log level enabled then the lambda for that log level will be
evaluated. Suppose you used the default configuration and you added a Crashlytics logger that logs
at info, warn and error levels. This would mean that the lambda for info, warn and error levels will
be evaluated because the Crashlytics logger needs it. So in this scenario you would want to have
minimal info logging code so as to not slow down the application at runtime and put most of the
logging at verbose and debug levels where it will not be evaluated in release builds.

## Configuration

With no configuration, logging is enabled for all log levels.

### Turn off logging for release builds

If logging is not desired for release builds then use the following to turn off the logging of the
default PlatformLogger

```kotlin
KmLogging.setLogLevel(LogLevel.Off)

// in Android this could be based on the debug flag:
KmLogging.setLogLevel(if (BuildConfig.DEBUG) LogLevel.Verbose else LogLevel.Off)
```

or use `PlatformLogger` and supply it a log level controller that is disabled for all log levels:

```kotlin
KmLogging.setLoggers(PlatformLogger(FixedLogLevel(false)))

// in Android this could be based on the debug flag:
KmLogging.setLoggers(PlatformLogger(FixedLogLevel(BuildConfig.DEBUG)))
```

## Kotlin version support

| KmLogging version | Kotlin version |
|-------------------|----------------|
| 2.1.0             | 2.2.20         |
| 2.0.3             | 2.0.21         |
| 1.5.0             | 1.9.24         |
| 1.4.2             | 1.8.22         |
| 1.3.0             | 1.8.10         |
| 1.2.1             | 1.7.21         |
| 1.2.0             | 1.6.10         |
| 1.1.0             | 1.5.32         |
| 1.0.0             | 1.4.30         |

## Miscellaneous

* When calling `KmLogging.setLoggers()` the existing loggers are removed and the supplied ones are
  added in.
* If the existing ones should remain then `KmLogging.addLoggers()` should be used.
* `PlatformLogger` uses Log on Android, os_log on iOS, SLF4j on JVM and console on WasmJS and JS.
* If a custom logger is created that changes the log level dynamically such as from a Remote Config
  change then `KmLogging.setupLoggingFlags()` should be called when the logger's log levels were
  changed to calculate which log levels are enabled. KmLogging maintains variables for each log
  level corresponding to whether any logger is enabled at that level. This is done for performance
  reason so only a single boolean check will be done at runtime to minimize the overhead when
  running in production. The android sample app demonstrates this.

## Use in unit testing

* If you want log messages to show up while unit testing then in your test class you can set up a
  logger that prints to the console using something like:

```kotlin
    @BeforeTest
    fun setup() {
        KmLogging.setLoggers(PrintLogger(FixedLogLevel(true)))
    }
```

* On Android unit tests will complain about not supplying default values or not being mocked. You
  will want to include the following:

```kotlin
android {
    testOptions {
        unitTests {
            isReturnDefaultValues = true
        }
    }
}
```

## Logging to another system such as Crashlytics

If logging is only desired at certain levels that can be setup. For example, if only the more
important logs should be sent to Crashlytics to give some context to crashes then only log info
level and above. That can be easily done by by defining and adding in a logger to do that. The
sample android app implement this.

```kotlin
class CrashlyticsLogger : Logger {
    override fun verbose(tag: String?, msg: String) {}

    override fun debug(tag: String?, msg: String) {}

    override fun info(tag: String?, msg: String) {
        FirebaseCrashlytics.getInstance().log(msg)
    }

    override fun warn(tag: String?, msg: String, t: Throwable?) {
        FirebaseCrashlytics.getInstance().log(msg)
    }

    override fun error(tag: String?, msg: String, t: Throwable?) {
        FirebaseCrashlytics.getInstance().log(msg)
    }

    override fun isLoggingVerbose(): Boolean = false

    override fun isLoggingDebug(): Boolean = false

    override fun isLoggingInfo(): Boolean = true

    override fun isLoggingWarning(): Boolean = true

    override fun isLoggingError(): Boolean = true
}

// at App creation time configure logging
// use addLogger to keep the existing loggers

KmLogging.addLogger(CrashlyticsLogger())
``` 

## Usage in iOS and Swift

By default the kotlin multiplatform toolchain will not export all KmLogging classes and those that
are will be prefaced with the stringLogging.
If you want to use classes from Swift code you will need to direct the plugin to export the logging
library in your `build.gradle.kts`:

```kotlin
    ios {
    binaries {
        framework {
            baseName = "my-shared-module-name"
            export("com.diamondedge:logging:$logging_version")
        }
    }
}
```

Note: logging must also be included as an api dependency.
See https://kotlinlang.org/docs/reference/mpp-build-native-binaries.html

The code to figure out what class KmLog was instantiated from does not work from within Swift, so
you will always want to pass in the class name:

```swift
class MyClass {
    let log = KmLog(tag: "MyClass")
}
```

## Usage on JVM

You will need to include a dependency on the logging library of your choice that is compatible with
SLF4J. See the sample app which uses `logback`. Since SLF4J controls the log level using its own
configurations it is advisable to retain the use of FixedLogLevel in the default configuration as
the log level controller. The log level controller in KmLogging controls the possibility of logging
occurring with SLF4J controlling whether a particular log usage is output or not.

## Usage in Libraries

A best practice for libraries is to not have its logging turned on be default. If both a library and
an application both use KmLogging then the library will have its logging turned on automatically. To
enable or disable a library's logging independently of the application, the library needs to use a
wrapper so logging can be turned on/off using a variable.

Example usage with code implemented
in [ChartsLogging.kt](https://github.com/ellsworthrw/DiamondCharts/blob/main/charts/src/main/java/com/diamondedge/charts/ChartsLogging.kt):

```kotlin
 object ChartsLogging {
    var enabled = true
}

fun moduleLogging(tag: String? = null): KmModuleLog {
    // string passed into createTag should be the name of the class that this function is implemented in
    // if it is a top level function then the class name is the file name with Kt appended
    val t = tag ?: KmLogging.createTag("ChartsLoggingKt").first
    return KmModuleLog(logging(t), ChartsLogging::enabled)
}
```

The library code would then use this new function to do its logging:

```kotlin
    val log = moduleLogging()
```

## Quick migration of Android Log calls

Once you have adopted KmLogging, what do you do with all your existing Android code that is using
the Log class? The first quick and easy step is to switch all your code to using
com.diamondedge.logging.Log class which mimics the Android Log class but sends all output
through KmLogging so you can turn it on and off at the same time as with all other KmLog usages and
have all its benefits. To do this simply replace all occurrences of `import android.util.Log` in
your code base with `import com.diamondedge.logging.Log`

