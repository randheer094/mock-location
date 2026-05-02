package dev.randheer094.dev.location.domain

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.emptyPreferences
import androidx.datastore.preferences.core.mutablePreferencesOf
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.updateAndGet
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Test

class SetupInstructionStatusUseCaseTest {

    @Test
    fun `wizard hidden when authorised and stored flag missing`() = runTest {
        val useCase = SetupInstructionStatusUseCase(
            dataStore = FakePreferenceDataStore(),
            isMockAppAuthorized = { true },
        )
        assertEquals(false, useCase.execute().first())
    }

    @Test
    fun `wizard shown when not authorised even if stored flag missing`() = runTest {
        val useCase = SetupInstructionStatusUseCase(
            dataStore = FakePreferenceDataStore(),
            isMockAppAuthorized = { false },
        )
        assertEquals(true, useCase.execute().first())
    }

    @Test
    fun `stored true forces wizard regardless of authorisation`() = runTest {
        val initial = mutablePreferencesOf().apply { set(SETUP_INSTRUCTION_STATUS, true) }
        val useCase = SetupInstructionStatusUseCase(
            dataStore = FakePreferenceDataStore(initial),
            isMockAppAuthorized = { true },
        )
        assertEquals(true, useCase.execute().first())
    }

    @Test
    fun `stored false hides wizard when authorised`() = runTest {
        val initial = mutablePreferencesOf().apply { set(SETUP_INSTRUCTION_STATUS, false) }
        val useCase = SetupInstructionStatusUseCase(
            dataStore = FakePreferenceDataStore(initial),
            isMockAppAuthorized = { true },
        )
        assertEquals(false, useCase.execute().first())
    }

    @Test
    fun `stored false still shows wizard when authorisation lost`() = runTest {
        val initial = mutablePreferencesOf().apply { set(SETUP_INSTRUCTION_STATUS, false) }
        val useCase = SetupInstructionStatusUseCase(
            dataStore = FakePreferenceDataStore(initial),
            isMockAppAuthorized = { false },
        )
        assertEquals(true, useCase.execute().first())
    }
}

private class FakePreferenceDataStore(
    initial: Preferences = emptyPreferences(),
) : DataStore<Preferences> {
    private val state = MutableStateFlow(initial)
    override val data: Flow<Preferences> = state
    override suspend fun updateData(
        transform: suspend (Preferences) -> Preferences,
    ): Preferences = state.updateAndGet { transform(it) }
}
