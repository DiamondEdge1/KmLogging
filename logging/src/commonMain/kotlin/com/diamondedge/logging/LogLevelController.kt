package com.diamondedge.logging

interface LogLevelController {
    fun isLoggingVerbose(): Boolean
    fun isLoggingDebug(): Boolean
    fun isLoggingInfo(): Boolean
    fun isLoggingWarning(): Boolean
    fun isLoggingError(): Boolean
}