package com.diamondedge.logging

import kotlin.time.Clock
import kotlin.time.ExperimentalTime

class PrintLogger(logLevel: LogLevelController) : Logger, LogLevelController by logLevel {

    override fun verbose(tag: String, msg: String) {
        println(message("V", tag, msg, null))
    }

    override fun debug(tag: String, msg: String) {
        println(message("D", tag, msg, null))
    }

    override fun info(tag: String, msg: String) {
        println(message("I", tag, msg, null))
    }

    override fun warn(tag: String, msg: String, t: Throwable?) {
        println(message("W", tag, msg, t))
    }

    override fun error(tag: String, msg: String, t: Throwable?) {
        println(message("E", tag, msg, t))
    }

    @OptIn(ExperimentalTime::class)
    private fun message(level: String, tag: String, msg: String, t: Throwable?): String {
        val now = Clock.System.now()
        val str = if (tag.isEmpty()) "$level:" else "$level/$tag:"
        return "$now $str $msg ${t ?: ""}"
    }
}