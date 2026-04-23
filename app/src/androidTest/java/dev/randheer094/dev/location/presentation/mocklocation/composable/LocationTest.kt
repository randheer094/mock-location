package dev.randheer094.dev.location.presentation.mocklocation.composable

import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import dev.randheer094.dev.location.domain.MockLocation
import dev.randheer094.dev.location.presentation.theme.MockLocationTheme
import org.junit.Assert.assertEquals
import org.junit.Rule
import org.junit.Test

class LocationTest {

    @get:Rule
    val composeRule = createComposeRule()

    private val tokyo = MockLocation(name = "Tokyo, Japan", lat = 35.6762, long = 139.6503)

    @Test
    fun renders_city_name_from_location() {
        composeRule.setContent {
            MockLocationTheme {
                LocationRow(
                    location = tokyo,
                    isSelected = false,
                    isActive = false,
                    onClick = {},
                )
            }
        }

        composeRule.onNodeWithText("Tokyo").assertIsDisplayed()
    }

    @Test
    fun renders_formatted_coordinates() {
        composeRule.setContent {
            MockLocationTheme {
                LocationRow(
                    location = tokyo,
                    isSelected = false,
                    isActive = false,
                    onClick = {},
                )
            }
        }

        composeRule.onNodeWithText("35.6762, 139.6503").assertIsDisplayed()
    }

    @Test
    fun click_invokes_callback() {
        var count = 0
        composeRule.setContent {
            MockLocationTheme {
                LocationRow(
                    location = tokyo,
                    isSelected = false,
                    isActive = false,
                    onClick = { count++ },
                )
            }
        }

        composeRule.onNodeWithText("Tokyo").performClick()
        assertEquals(1, count)
    }
}
