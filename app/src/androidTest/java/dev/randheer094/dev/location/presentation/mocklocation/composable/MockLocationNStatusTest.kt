package dev.randheer094.dev.location.presentation.mocklocation.composable

import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithText
import dev.randheer094.dev.location.domain.MockLocation
import dev.randheer094.dev.location.presentation.mocklocation.state.UiState
import dev.randheer094.dev.location.presentation.theme.MockLocationTheme
import org.junit.Rule
import org.junit.Test

/**
 * Covers BroadcastingHero and IdleHero rendering.
 */
class MockLocationNStatusTest {

    @get:Rule
    val composeRule = createComposeRule()

    private val paris = MockLocation(name = "Paris, France", lat = 48.8566, long = 2.3522)

    @Test
    fun broadcasting_hero_shows_live_status_pill() {
        val state = UiState(
            showInstructions = false,
            status = true,
            hasNotificationPermission = true,
            items = emptyList(),
            elapsedLabel = "00:05:30",
            selected = paris,
        )
        composeRule.setContent {
            MockLocationTheme { BroadcastingHero(uiState = state, onStop = {}) }
        }

        composeRule.onNodeWithText("LIVE · GPS + NET").assertIsDisplayed()
    }

    @Test
    fun broadcasting_hero_shows_stop_button() {
        val state = UiState(
            showInstructions = false,
            status = true,
            hasNotificationPermission = true,
            items = emptyList(),
            elapsedLabel = "00:00:10",
            selected = paris,
        )
        composeRule.setContent {
            MockLocationTheme { BroadcastingHero(uiState = state, onStop = {}) }
        }

        composeRule.onNodeWithText("Stop broadcasting").assertIsDisplayed()
    }

    @Test
    fun broadcasting_hero_shows_city_name_and_elapsed_timer() {
        val state = UiState(
            showInstructions = false,
            status = true,
            hasNotificationPermission = true,
            items = emptyList(),
            elapsedLabel = "00:02:15",
            selected = paris,
        )
        composeRule.setContent {
            MockLocationTheme { BroadcastingHero(uiState = state, onStop = {}) }
        }

        composeRule.onNodeWithText("Paris").assertIsDisplayed()
        composeRule.onNodeWithText("T+ 00:02:15").assertIsDisplayed()
    }

    @Test
    fun idle_hero_shows_off_status_pill() {
        val state = UiState(
            showInstructions = false,
            status = false,
            hasNotificationPermission = true,
            items = emptyList(),
            elapsedLabel = "",
            selected = paris,
        )
        composeRule.setContent {
            MockLocationTheme { IdleHero(uiState = state, onStart = {}) }
        }

        composeRule.onNodeWithText("Mock location off").assertIsDisplayed()
    }

    @Test
    fun idle_hero_shows_ready_text() {
        val state = UiState(
            showInstructions = false,
            status = false,
            hasNotificationPermission = true,
            items = emptyList(),
            elapsedLabel = "",
            selected = null,
        )
        composeRule.setContent {
            MockLocationTheme { IdleHero(uiState = state, onStart = {}) }
        }

        composeRule.onNodeWithText("Ready").assertIsDisplayed()
    }
}
