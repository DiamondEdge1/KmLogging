package com.diamondedge.logging

import kotlin.time.Clock

internal actual fun getLoggerApi(): PlatformLoggerApi = WasmPlatformLogger()

internal class WasmPlatformLogger : PlatformLoggerApi {

    override fun verbose(tag: String, msg: String) {
        console(message("V", tag, msg, null))
    }

    override fun debug(tag: String, msg: String) {
        console(message("D", tag, msg, null))
    }

    override fun info(tag: String, msg: String) {
        consoleInfo(message("I", tag, msg, null))
    }

    override fun warn(tag: String, msg: String, t: Throwable?) {
        consoleWarn(message("W", tag, msg, t))
    }

    override fun error(tag: String, msg: String, t: Throwable?) {
        consoleError(message("E", tag, msg, t))
    }

    override fun createTag(fromClass: String?): Pair<String, String> {
        return Pair("", "")
    }

    private fun message(level: String, tag: String, msg: String, t: Throwable?): String {
        val now = Clock.System.now()
        val str = if (tag.isEmpty()) "$level:" else "$level/$tag:"
        return "$now $str $msg ${t ?: ""}"
    }
}

private fun console(msg: String): Unit = js("console.log(msg)")
private fun consoleInfo(msg: String): Unit = js("console.info(msg)")
private fun consoleWarn(msg: String): Unit = js("console.warn(msg)")
private fun consoleError(msg: String): Unit = js("console.error(msg)")
