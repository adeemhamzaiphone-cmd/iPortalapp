package com.example

import android.content.Context
import androidx.test.core.app.ApplicationProvider
import org.junit.Assert.assertEquals
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.robolectric.annotation.Config

@RunWith(RobolectricTestRunner::class)
@Config(sdk = [36])
class ExampleRobolectricTest {

  @Test
  fun `read string from context`() {
    val context = ApplicationProvider.getApplicationContext<Context>()
    val appName = context.getString(R.string.app_name)
    assertEquals("IPAK Portal", appName)
  }

  @Test
  fun `verify default transport routes exist`() {
    val routes = com.example.data.IpakRepository.getTransportRoutes()
    org.junit.Assert.assertTrue(routes.isNotEmpty())
    org.junit.Assert.assertEquals(9, routes.size)
  }
}
