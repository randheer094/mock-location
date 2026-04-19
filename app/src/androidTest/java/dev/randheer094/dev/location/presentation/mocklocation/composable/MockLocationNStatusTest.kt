package dev.randheer094.dev.location.presentation.mocklocation.composable

import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithContentDescription
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import dev.randheer094.dev.location.domain.MockLocation
import dev.randheer094.dev.location.presentation.mocklocation.state.MockLocationNStatus
import dev.randheer094.dev.location.presentation.theme.MockLocationTheme
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Rule
import org.junit.Test

/**
 * Covers the dual-layout rendering of [MockLocationNStatus]: a full card with edit and
 * start/stop affordances when a location is selected, and a single "Add or select" CTA when
 * no location has been picked yet.
 */
class MockLocationNStatusTest {

    @get:Rule
    val composeRule = createComposeRule()

    private val paris = MockLocation(name = "Paris", lat = 48.8566, long = 2.3522)

    @Test
    fun shows_play_affordance_when_mocking_is_off() {
        composeRule.setContent {
            MockLocationTheme {
                MockLocationNStatus(
                    state = MockLocationNStatus(location = paris, status = false),
                    onEdit = {},
                    onStartStop = {},
                )
            }
        }

        composeRule.onNodeWithText("Paris").assertIsDisplayed()
        composeRule.onNodeWithContentDescription("Start mocking").assertIsDisplayed()
    }

    @Test
    fun shows_stop_affordance_when_mocking_is_on() {
        composeRule.setContent {
            MockLocationTheme {
                MockLocationNStatus(
                    state = MockLocationNStatus(location = paris, status = true),
                    onEdit = {},
                    onStartStop = {},
                )
            }
        }

        composeRule.onNodeWithContentDescription("Stop mocking").assertIsDisplayed()
    }

    @Test
    fun start_stop_button_invokes_callback() {
        var clicked = 0
        composeRule.setContent {
            MockLocationTheme {
                MockLocationNStatus(
                    state = MockLocationNStatus(location = paris, status = false),
                    onEdit = {},
                    onStartStop = { clicked++ },
                )
            }
        }

        composeRule.onNodeWithContentDescription("Start mocking").performClick()
        assertEquals(1, clicked)
    }

    @Test
    fun edit_button_invokes_callback() {
        var edited = 0
        composeRule.setContent {
            MockLocationTheme {
                MockLocationNStatus(
                    state = MockLocationNStatus(location = paris, status = true),
                    onEdit = { edited++ },
                    onStartStop = {},
                )
            }
        }

        composeRule.onNodeWithContentDescription("Edit location").performClick()
        assertEquals(1, edited)
    }

    @Test
    fun null_location_renders_add_or_select_cta_which_triggers_edit() {
        var edited = 0
        composeRule.setContent {
            MockLocationTheme {
                MockLocationNStatus(
                    state = MockLocationNStatus(location = null, status = false),
                    onEdit = { edited++ },
                    onStartStop = {},
                )
            }
        }

        composeRule.onNodeWithText("Add Location or Select from list below").assertIsDisplayed()
        composeRule.onNodeWithText("Add Location or Select from list below").performClick()
        assertTrue("Edit callback should fire when the placeholder CTA is tapped", edited == 1)
    }
}
