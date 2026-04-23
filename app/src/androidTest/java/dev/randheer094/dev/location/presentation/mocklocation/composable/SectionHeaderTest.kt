package dev.randheer094.dev.location.presentation.mocklocation.composable

import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithText
import dev.randheer094.dev.location.presentation.theme.MockLocationTheme
import org.junit.Rule
import org.junit.Test

class SectionHeaderTest {

    @get:Rule
    val composeRule = createComposeRule()

    @Test
    fun renders_preset_locations_overline() {
        composeRule.setContent {
            MockLocationTheme { SectionHeader() }
        }

        composeRule.onNodeWithText("PRESET LOCATIONS").assertIsDisplayed()
    }

    @Test
    fun renders_sort_az_label() {
        composeRule.setContent {
            MockLocationTheme { SectionHeader() }
        }

        composeRule.onNodeWithText("Sort · A–Z").assertIsDisplayed()
    }
}
