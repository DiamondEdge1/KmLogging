package com.diamondedge.logging

internal interface PlatformApi {
    val isIOS: Boolean
    val isAndroid: Boolean
    val isJS: Boolean
    val isWasmJs: Boolean
    val isJVM: Boolean
    val name: String
    val version: Double
    val versionName: String
    val timeNanos: Long
}

internal expect fun getPlatform(): PlatformApi

object Platform {
    private val api = getPlatform()

    val isIOS: Boolean = api.isIOS
    val isAndroid: Boolean = api.isAndroid
    val isJS: Boolean = api.isJS
    val isWasmJs: Boolean = api.isWasmJs
    val isJVM: Boolean = api.isJVM
    val name: String = api.name
    val version: Double = api.version
    val versionName: String = api.versionName
    val timeNanos: Long
        get() = api.timeNanos
}