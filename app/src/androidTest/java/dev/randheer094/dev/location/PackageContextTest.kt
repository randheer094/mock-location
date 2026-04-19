package dev.randheer094.dev.location

import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.platform.app.InstrumentationRegistry
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotNull
import org.junit.Test
import org.junit.runner.RunWith

/**
 * Smoke test that the debug build is installed with the expected package name and that the
 * target context is resolvable. Acts as a baseline to detect instrumentation-runner or
 * manifest-merge regressions before the richer UI tests run.
 */
@RunWith(AndroidJUnit4::class)
class PackageContextTest {

    @Test
    fun debug_build_reports_expected_package_name() {
        val context = InstrumentationRegistry.getInstrumentation().targetContext
        assertEquals("dev.randheer094.dev.location.debug", context.packageName)
    }

    @Test
    fun app_label_is_resolvable_from_string_resources() {
        val context = InstrumentationRegistry.getInstrumentation().targetContext
        val name = context.getString(R.string.app_name)
        assertNotNull(name)
        assertEquals("Mock Location for Developers", name)
    }
}
