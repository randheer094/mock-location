package dev.randheer094.dev.location.di

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.preferencesDataStore
import dev.randheer094.dev.location.data.MockLocationRepositoryImpl
import dev.randheer094.dev.location.domain.GetMockLocationsUseCase
import dev.randheer094.dev.location.domain.MockLocationRepository
import dev.randheer094.dev.location.presentation.mocklocation.MockLocationViewModel
import kotlinx.serialization.json.Json
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module


private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(
    name = "m_l"
)

val dataSourceModule = module {
    single { get<Context>().dataStore }
    single {
        Json {
            ignoreUnknownKeys = true
        }
    }
}

val repositoryModule = module {
    single<MockLocationRepository> { MockLocationRepositoryImpl(get(), get(), get()) }
}

val useCaseModule = module {
    factory { GetMockLocationsUseCase(get()) }
}

val viewModelModule = module {
    viewModel { MockLocationViewModel(get()) }
}