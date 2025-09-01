package com.diamondedge.sample

import android.app.Application
import com.diamondedge.logging.KmLogging
import com.diamondedge.logging.Log
import com.diamondedge.logging.PlatformLogger
import com.diamondedge.logging.VariableLogLevel
import com.diamondedge.logging.logging
import com.google.firebase.Firebase
import com.google.firebase.remoteconfig.get
import com.google.firebase.remoteconfig.remoteConfig
import com.google.firebase.remoteconfig.remoteConfigSettings
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking

class App : Application() {

    private val remoteConfig by lazy { Firebase.remoteConfig }

    override fun onCreate() {
        super.onCreate()
        KmLogging.setLoggers(PlatformLogger(VariableLogLevel(com.diamondedge.logging.LogLevel.Verbose)), CrashlyticsLogger())
        log.info { "onCreate ${Thread.currentThread()}" }

        setupRemoteConfig()
        logOnThread()
    }

    private fun setupRemoteConfig() {
        val configSettings = remoteConfigSettings {
            minimumFetchIntervalInSeconds = /*if (BuildConfig.DEBUG) 60 else*/ 3600
        }
        remoteConfig.setConfigSettingsAsync(configSettings)
        applicationScope.launch(Dispatchers.IO) {
            remoteConfig.fetchAndActivate().addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    val updated = task.result
                    val logLevel = remoteConfig["log_level"].asString()
                    Log.i("App", "log_level: $logLevel ${Thread.currentThread()}")
                    val level = com.diamondedge.logging.LogLevel.valueOf(logLevel)
                    KmLogging.setLogLevel(level)
                    log.i { "Config params updated: $updated. LogLevel: $level" }
                } else {
                    log.w { "Fetch failed" }
                }
            }
        }
    }

    private fun logOnThread() {
        log.d { "logOnThread ${Thread.currentThread()}" }
        runBlocking {

        }
        applicationScope.launch(Dispatchers.IO) {
            log.d { "logOnThread coroutine: ${Thread.currentThread()} $this ${this.coroutineContext}" }
        }
        applicationScope.launch(Dispatchers.Main) {
            log.d { "logOnThread coroutine main ${Thread.currentThread()} $this ${this.coroutineContext}" }
        }
    }

    companion object {
        private val log = logging()
        var applicationScope = MainScope()
    }
}
