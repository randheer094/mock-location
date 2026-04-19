package dev.randheer094.dev.location.presentation.mocklocation.composable

import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import androidx.compose.ui.test.performTextInput
import dev.randheer094.dev.location.domain.MockLocation
import dev.randheer094.dev.location.presentation.theme.MockLocationTheme
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotNull
import org.junit.Assert.assertNull
import org.junit.Assert.assertTrue
import org.junit.Rule
import org.junit.Test

/**
 * Compose UI tests for [AddMockLocationBottomSheet]. Covers the validation rules that live in
 * the composable itself: coordinates must parse as doubles within geographic bounds, and the
 * display name is prefixed with "(Manual)" before being emitted.
 */
class AddMockLocationBottomSheetTest {

    @get:Rule
    val composeRule = createComposeRule()

    @Test
    fun valid_coordinates_are_emitted_with_manual_prefix_on_submit() {
        var submitted: MockLocation? = null
        composeRule.setContent {
            MockLocationTheme { AddMockLocationBottomSheet(onSubmit = { submitted = it }) }
        }

        composeRule.onNodeWithText("Name").performTextInput("Home")
        composeRule.onNodeWithText("Latitude").performTextInput("12.9716")
        composeRule.onNodeWithText("Longitude").performTextInput("77.5946")

        composeRule.onNodeWithText("Set Mock Location").performClick()

        assertNotNull("Submit callback should fire for valid input", submitted)
        assertEquals(12.9716, submitted!!.lat, 0.0)
        assertEquals(77.5946, submitted!!.long, 0.0)
        assertTrue(
            "Name should be wrapped with the (Manual) prefix, was: ${submitted!!.name}",
            submitted!!.name.startsWith("(Manual)") && submitted!!.name.contains("Home"),
        )
    }

    @Test
    fun blank_name_falls_back_to_coordinate_pair_as_display_name() {
        var submitted: MockLocation? = null
        composeRule.setContent {
            MockLocationTheme { AddMockLocationBottomSheet(onSubmit = { submitted = it }) }
        }

        composeRule.onNodeWithText("Latitude").performTextInput("48.8566")
        composeRule.onNodeWithText("Longitude").performTextInput("2.3522")
        composeRule.onNodeWithText("Set Mock Location").performClick()

        assertNotNull(submitted)
        assertTrue(
            "Name should fall back to lat, long, was: ${submitted!!.name}",
            submitted!!.name.contains("48.8566") && submitted!!.name.contains("2.3522"),
        )
    }

    @Test
    fun out_of_range_latitude_surfaces_validation_error_and_suppresses_submit() {
        var submitted: MockLocation? = null
        composeRule.setContent {
            MockLocationTheme { AddMockLocationBottomSheet(onSubmit = { submitted = it }) }
        }

        composeRule.onNodeWithText("Latitude").performTextInput("100")
        composeRule.onNodeWithText("Longitude").performTextInput("2.0")
        composeRule.onNodeWithText("Set Mock Location").performClick()

        composeRule.onNodeWithText("Invalid latitude or longitude").assertIsDisplayed()
        assertNull(submitted)
    }

    @Test
    fun out_of_range_longitude_is_rejected() {
        var submitted: MockLocation? = null
        composeRule.setContent {
            MockLocationTheme { AddMockLocationBottomSheet(onSubmit = { submitted = it }) }
        }

        composeRule.onNodeWithText("Latitude").performTextInput("20.0")
        composeRule.onNodeWithText("Longitude").performTextInput("-190")
        composeRule.onNodeWithText("Set Mock Location").performClick()

        composeRule.onNodeWithText("Invalid latitude or longitude").assertIsDisplayed()
        assertNull(submitted)
    }

    @Test
    fun non_numeric_input_is_rejected() {
        var submitted: MockLocation? = null
        composeRule.setContent {
            MockLocationTheme { AddMockLocationBottomSheet(onSubmit = { submitted = it }) }
        }

        composeRule.onNodeWithText("Latitude").performTextInput("abc")
        composeRule.onNodeWithText("Longitude").performTextInput("xyz")
        composeRule.onNodeWithText("Set Mock Location").performClick()

        composeRule.onNodeWithText("Invalid latitude or longitude").assertIsDisplayed()
        assertNull(submitted)
    }
}
