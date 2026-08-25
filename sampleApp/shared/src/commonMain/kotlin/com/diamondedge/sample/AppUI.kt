package com.diamondedge.sample

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material.Button
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import com.diamondedge.logging.logging
import org.jetbrains.compose.ui.tooling.preview.Preview

private val log = logging()

@Composable
@Preview
fun AppUI() {
    MaterialTheme {
        var showContent by remember { mutableStateOf(false) }
        Column(Modifier.fillMaxWidth(), horizontalAlignment = Alignment.CenterHorizontally) {
            Button(onClick = { showContent = !showContent }) {
                Text("Click me!")
            }
            AnimatedVisibility(showContent) {
                val greeting = remember { Greeting().greet() }
                Column(Modifier.fillMaxWidth(), horizontalAlignment = Alignment.CenterHorizontally) {
                    Text("Compose: $greeting")
                }
            }
            Button(onClick = { log.debug { "debug level msg" } }) {
                Text("Debug level")
            }
            Button(onClick = { log.info { "info level msg" } }) {
                Text("Info level")
            }
            Button(onClick = { warnException() }) {
                Text("Warn level")
            }
            Button(onClick = { fatalException() }) {
                Text("Error level")
            }
        }
    }
}

private fun warnException() {
    try {
        throw RuntimeException("Non Fatal")
    } catch (e: Exception) {
        log.warn(e) { "exception handled" }
    }
}

private fun fatalException() {
    try {
        throw RuntimeException("Fatal")
    } catch (e: Exception) {
        log.error(e) { "fatal exception encountered" }
    }
}
