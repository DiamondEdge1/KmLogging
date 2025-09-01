package com.diamondedge.logging

import kotlin.time.Clock

internal actual fun getPlatform(): PlatformApi = WasmPlatform()

internal class WasmPlatform : PlatformApi {
    override val isIOS: Boolean = false
    override val isAndroid: Boolean = false
    override val isJS: Boolean = false
    override val isWasmJs: Boolean = true
    override val isJVM: Boolean = false
    override val name: String = "Wasm"
    override val version: Double = 1.0        //TODO
    override val versionName: String = "1.0"  //TODO
    override val timeNanos: Long
        get() {
            val now = Clock.System.now()
            return now.epochSeconds * 1_000_000_000L + now.nanosecondsOfSecond
        }
}
