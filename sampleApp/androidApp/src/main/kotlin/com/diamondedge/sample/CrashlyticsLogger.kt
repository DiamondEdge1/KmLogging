package com.diamondedge.sample

import com.diamondedge.logging.Logger
import com.google.firebase.crashlytics.FirebaseCrashlytics

class CrashlyticsLogger : Logger {

    override fun verbose(tag: String, msg: String) {}

    override fun debug(tag: String, msg: String) {}

    override fun info(tag: String, msg: String) {
        FirebaseCrashlytics.getInstance().log(message(tag, msg))
    }

    override fun warn(tag: String, msg: String, t: Throwable?) {
        FirebaseCrashlytics.getInstance().log(message(tag, msg))
        if (t != null)
            FirebaseCrashlytics.getInstance().recordException(t)
    }

    override fun error(tag: String, msg: String, t: Throwable?) {
        FirebaseCrashlytics.getInstance().log(message(tag, msg))
        if (t != null)
            FirebaseCrashlytics.getInstance().recordException(t)
    }

    private fun message(tag: String, msg: String) = if (tag.isEmpty()) msg else "$tag: $msg"

    override fun isLoggingVerbose(): Boolean = false

    override fun isLoggingDebug(): Boolean = false

    override fun isLoggingInfo(): Boolean = true

    override fun isLoggingWarning(): Boolean = true

    override fun isLoggingError(): Boolean = true
}