package dev.randheer094.dev.location.presentation.mocklocation.composable

import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import dev.randheer094.dev.location.presentation.theme.MockLocationTheme
import org.junit.Assert.assertEquals
import org.junit.Rule
import org.junit.Test

class SetupInstructionTest {

    @get:Rule
    val composeRule = createComposeRule()

    @Test
    fun shows_all_three_step_card_titles() {
        composeRule.setContent {
            MockLocationTheme { SetupInstruction(onGotIt = {}) }
        }

        composeRule.onNodeWithText("Open Developer Options").assertIsDisplayed()
        composeRule.onNodeWithText(“Find \”Select mock location app\””).assertIsDisplayed()
        composeRule.onNodeWithText("Pick Mock Location").assertIsDisplayed()
    }

    @Test
    fun primary_cta_is_displayed_and_clickable() {
        composeRule.setContent {
            MockLocationTheme { SetupInstruction(onGotIt = {}) }
        }

        composeRule.onNodeWithText("Open developer options").assertIsDisplayed()
        composeRule.onNodeWithText("Open developer options").performClick()
    }

    @Test
    fun secondary_cta_triggers_on_got_it_callback() {
        var clicks = 0
        composeRule.setContent {
            MockLocationTheme { SetupInstruction(onGotIt = { clicks++ }) }
        }

        composeRule.onNodeWithText("I’ve done this — check again").performClick()
        assertEquals(1, clicks)
    }
}
