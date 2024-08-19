package org.lighthousegames.logging

import kotlinx.cinterop.ExperimentalForeignApi
import kotlinx.cinterop.ptr
import platform.Foundation.NSThread
import platform.darwin.OS_LOG_DEFAULT
import platform.darwin.OS_LOG_TYPE_DEBUG
import platform.darwin.OS_LOG_TYPE_DEFAULT
import platform.darwin.OS_LOG_TYPE_ERROR
import platform.darwin.OS_LOG_TYPE_FAULT
import platform.darwin.OS_LOG_TYPE_INFO
import platform.darwin.__dso_handle
import platform.darwin._os_log_internal

@OptIn(ExperimentalForeignApi::class)
actual class PlatformLogger actual constructor(actual val logLevel: LogLevelController) : Logger, TagProvider, LogLevelController by logLevel {

    actual override fun verbose(tag: String, msg: String) {
        _os_log_internal(
            __dso_handle.ptr,
            OS_LOG_DEFAULT,
            OS_LOG_TYPE_DEBUG,
            "%s",
            message("V", tag, msg)
        )
    }

    actual override fun debug(tag: String, msg: String) {
        _os_log_internal(
            __dso_handle.ptr,
            OS_LOG_DEFAULT,
            OS_LOG_TYPE_INFO,
            "%s",
            message("D", tag, msg)
        )
    }

    actual override fun info(tag: String, msg: String) {
        _os_log_internal(
            __dso_handle.ptr,
            OS_LOG_DEFAULT,
            OS_LOG_TYPE_DEFAULT,
            "%s",
            message("I", tag, msg)
        )
    }

    actual override fun warn(tag: String, msg: String, t: Throwable?) {
        _os_log_internal(
            __dso_handle.ptr,
            OS_LOG_DEFAULT,
            OS_LOG_TYPE_ERROR,
            "%s",
            message("W", tag, msg, t)
        )
    }

    actual override fun error(tag: String, msg: String, t: Throwable?) {
        _os_log_internal(
            __dso_handle.ptr,
            OS_LOG_DEFAULT,
            OS_LOG_TYPE_FAULT,
            "%s",
            message("E", tag, msg, t)
        )
    }

    private fun message(level: String, tag: String, msg: String, t: Throwable? = null): String {
        val str = if (tag.isEmpty()) "$level: $msg" else "$level/$tag: $msg"
        return if (t == null) str else "$str $t"
    }

    actual override fun createTag(fromClass: String?): Pair<String, String> {
        val stack = NSThread.callStackSymbols
        var clsName = ""
        stack.forEachIndexed { index, t ->
            val stackEntry = t.toString()
//            println("tag: $tag stack: $stackEntry")

            if (stackEntry.contains("KmLog") && stack.size > index) {
                val nextEntry = stack[index + 1].toString()
                if (!nextEntry.contains("KmLog"))
                    clsName = nextEntry
            }
            if (fromClass != null && stackEntry.contains(fromClass) && stack.size > index) {
                clsName = stack[index + 1].toString()
            }
        }
        return Pair(getClassName(clsName), clsName)
    }

    private fun getClassName(stackEntry: String): String {
        var tag = ""
        if (stackEntry.contains(".Companion")) {
            tag = stackEntry.substringBefore(".Companion")
        } else if (stackEntry.contains("#<init>()")) {
            tag = stackEntry.substringBefore("#<init>()")
        }
        return tag.substring(tag.lastIndexOf(".") + 1)
    }
}