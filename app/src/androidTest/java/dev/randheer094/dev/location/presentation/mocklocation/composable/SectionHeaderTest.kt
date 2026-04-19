package dev.randheer094.dev.location.presentation.mocklocation.composable

import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithText
import dev.randheer094.dev.location.presentation.mocklocation.state.SectionHeader
import dev.randheer094.dev.location.presentation.theme.MockLocationTheme
import org.junit.Rule
import org.junit.Test

class SectionHeaderTest {

    @get:Rule
    val composeRule = createComposeRule()

    @Test
    fun status_header_reflects_on_state() {
        composeRule.setContent {
            MockLocationTheme {
                SectionHeader(state = SectionHeader.MockLocationStatus(isOn = true))
            }
        }

        composeRule.onNodeWithText("Mock location: (ON)").assertIsDisplayed()
    }

    @Test
    fun status_header_reflects_off_state() {
        composeRule.setContent {
            MockLocationTheme {
                SectionHeader(state = SectionHeader.MockLocationStatus(isOn = false))
            }
        }

        composeRule.onNodeWithText("Mock location: (OFF)").assertIsDisplayed()
    }

    @Test
    fun select_locations_header_renders_expected_label() {
        composeRule.setContent {
            MockLocationTheme { SectionHeader(state = SectionHeader.SelectLocations) }
        }

        composeRule.onNodeWithText("Select locations").assertIsDisplayed()
    }
}
