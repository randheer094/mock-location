package dev.randheer094.dev.location.presentation.mocklocation.composable

import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.assertIsEnabled
import androidx.compose.ui.test.assertIsNotEnabled
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import androidx.compose.ui.test.performTextInput
import dev.randheer094.dev.location.domain.MockLocation
import dev.randheer094.dev.location.presentation.theme.MockLocationTheme
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotNull
import org.junit.Assert.assertTrue
import org.junit.Rule
import org.junit.Test

class AddMockLocationBottomSheetTest {

    @get:Rule
    val composeRule = createComposeRule()

    @Test
    fun fields_render_and_accept_input() {
        composeRule.setContent {
            MockLocationTheme { AddMockLocationBottomSheet(onSubmit = {}) }
        }

        composeRule.onNodeWithTag("name_field").assertIsDisplayed()
        composeRule.onNodeWithTag("lat_field").assertIsDisplayed()
        composeRule.onNodeWithTag("lng_field").assertIsDisplayed()

        composeRule.onNodeWithTag("name_field").performTextInput("Home")
        composeRule.onNodeWithTag("lat_field").performTextInput("12.9716")
        composeRule.onNodeWithTag("lng_field").performTextInput("77.5946")
    }

    @Test
    fun validation_strip_shows_error_for_out_of_range_latitude() {
        composeRule.setContent {
            MockLocationTheme { AddMockLocationBottomSheet(onSubmit = {}) }
        }

        composeRule.onNodeWithTag("lat_field").performTextInput("100")

        composeRule.onNodeWithText("Latitude out of range").assertIsDisplayed()
    }

    @Test
    fun cta_button_is_disabled_when_fields_are_empty() {
        composeRule.setContent {
            MockLocationTheme { AddMockLocationBottomSheet(onSubmit = {}) }
        }

        composeRule.onNodeWithTag("cta_button").assertIsNotEnabled()
    }

    @Test
    fun cta_button_is_enabled_when_valid_coordinates_entered() {
        composeRule.setContent {
            MockLocationTheme { AddMockLocationBottomSheet(onSubmit = {}) }
        }

        composeRule.onNodeWithTag("lat_field").performTextInput("37.7749")
        composeRule.onNodeWithTag("lng_field").performTextInput("-122.4194")

        composeRule.onNodeWithTag("cta_button").assertIsEnabled()
    }

    @Test
    fun onsubmit_fires_with_correct_mock_location_on_button_click() {
        var submitted: MockLocation? = null
        composeRule.setContent {
            MockLocationTheme { AddMockLocationBottomSheet(onSubmit = { submitted = it }) }
        }

        composeRule.onNodeWithTag("name_field").performTextInput("Home")
        composeRule.onNodeWithTag("lat_field").performTextInput("12.9716")
        composeRule.onNodeWithTag("lng_field").performTextInput("77.5946")

        composeRule.onNodeWithTag("cta_button").performClick()

        assertNotNull("Submit callback should fire for valid input", submitted)
        assertEquals(12.9716, submitted!!.lat, 1e-10)
        assertEquals(77.5946, submitted!!.long, 1e-10)
        assertTrue(
            "Name should be wrapped with (Manual) prefix and contain the label, was: ${submitted!!.name}",
            submitted!!.name.startsWith("(Manual)") && submitted!!.name.contains("Home"),
        )
    }
}
