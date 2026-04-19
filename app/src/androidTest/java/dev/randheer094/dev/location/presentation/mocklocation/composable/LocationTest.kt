package dev.randheer094.dev.location.presentation.mocklocation.composable

import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithText
import dev.randheer094.dev.location.domain.MockLocation
import dev.randheer094.dev.location.presentation.mocklocation.state.Location
import dev.randheer094.dev.location.presentation.theme.MockLocationTheme
import org.junit.Rule
import org.junit.Test

class LocationTest {

    @get:Rule
    val composeRule = createComposeRule()

    @Test
    fun renders_name_and_formatted_coordinates() {
        val tokyo = MockLocation(name = "Tokyo", lat = 35.6762, long = 139.6503)
        composeRule.setContent {
            MockLocationTheme { Location(state = Location(tokyo)) }
        }

        composeRule.onNodeWithText("Tokyo").assertIsDisplayed()
        composeRule.onNodeWithText("35.6762, 139.6503").assertIsDisplayed()
    }
}
