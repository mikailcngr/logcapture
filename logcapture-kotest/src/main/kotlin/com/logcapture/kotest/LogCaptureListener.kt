package com.logcapture.kotest

import ch.qos.logback.classic.Logger
import ch.qos.logback.classic.spi.ILoggingEvent
import com.logcapture.LogCapture
import com.logcapture.logback.StubAppender
import io.kotest.core.listeners.TestListener
import io.kotest.core.test.TestCase
import io.kotest.core.test.TestResult
import org.hamcrest.Matcher
import org.slf4j.Logger.ROOT_LOGGER_NAME
import org.slf4j.LoggerFactory

object LogCaptureListener : TestListener {

  private lateinit var logAppender: StubAppender
  private lateinit var root: Logger

  override suspend fun beforeTest(testCase: TestCase) {
    logAppender = StubAppender()
    root = LoggerFactory.getLogger(ROOT_LOGGER_NAME) as Logger
    root.addAppender(logAppender)
  }

  override suspend fun afterTest(testCase: TestCase, result: TestResult) {
    root.detachAppender(logAppender)
  }

  fun logged(expectedLoggingMessage: Matcher<List<ILoggingEvent>>): LogCaptureListener {
    LogCapture<Any>(logAppender.events()).logged(expectedLoggingMessage)
    return this
  }
}
