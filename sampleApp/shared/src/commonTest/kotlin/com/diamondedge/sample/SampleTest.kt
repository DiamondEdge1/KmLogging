package com.diamondedge.sample

import com.diamondedge.logging.FixedLogLevel
import com.diamondedge.logging.KmLogging
import com.diamondedge.logging.PrintLogger
import com.diamondedge.logging.logging
import kotlin.test.BeforeTest
import kotlin.test.Test
import kotlin.test.assertEquals


class SampleTest {
    @BeforeTest
    fun setup() {
        KmLogging.setLoggers(PrintLogger(FixedLogLevel(true)))
    }

    @Test
    fun testLogLevels() {
        log.v { "verbose level" }
        log.d { "debug level" }
        log.i { "info level" }
        assertEquals(1, 1)
    }

    companion object {
        val log = logging()
    }
}
