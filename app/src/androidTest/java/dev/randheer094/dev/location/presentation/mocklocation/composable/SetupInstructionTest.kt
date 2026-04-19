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
    fun shows_title_body_and_dismiss_button() {
        composeRule.setContent {
            MockLocationTheme { SetupInstruction(onGotIt = {}) }
        }

        composeRule.onNodeWithText("Setup Instructions").assertIsDisplayed()
        composeRule.onNodeWithText("Got it!").assertIsDisplayed()
    }

    @Test
    fun got_it_click_invokes_callback_exactly_once() {
        var clicks = 0
        composeRule.setContent {
            MockLocationTheme { SetupInstruction(onGotIt = { clicks++ }) }
        }

        composeRule.onNodeWithText("Got it!").performClick()
        assertEquals(1, clicks)
    }
}
