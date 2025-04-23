package dev.randheer094.dev.location.di

import android.content.Context
import android.location.LocationManager
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.preferencesDataStore
import dev.randheer094.dev.location.domain.GetMockLocationsUseCase
import dev.randheer094.dev.location.domain.MockLocationStatusUseCase
import dev.randheer094.dev.location.domain.SelectMockLocationUseCase
import dev.randheer094.dev.location.domain.SelectedMockLocationUseCase
import dev.randheer094.dev.location.domain.SetMockLocationStatusUseCase
import dev.randheer094.dev.location.domain.SetSetupInstructionStatusUseCase
import dev.randheer094.dev.location.domain.SetupInstructionStatusUseCase
import dev.randheer094.dev.location.presentation.mocklocation.MockLocationViewModel
import dev.randheer094.dev.location.presentation.mocklocation.state.UiStateMapper
import dev.randheer094.dev.location.presentation.utils.LocationUtils
import dev.randheer094.dev.location.presentation.utils.NotificationUtils
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

val useCaseModule = module {
    factory { GetMockLocationsUseCase(get(), get()) }
    factory { MockLocationStatusUseCase(get()) }
    factory { SetMockLocationStatusUseCase(get()) }
    factory { SelectMockLocationUseCase(get(), get()) }
    factory { SelectedMockLocationUseCase(get(), get()) }
    factory { SetSetupInstructionStatusUseCase(get()) }
    factory { SetupInstructionStatusUseCase(get()) }
}

val mapperModule = module {
    single { UiStateMapper() }
}

val viewModelModule = module {
    viewModel {
        MockLocationViewModel(
            get(), get(), get(), get(), get(), get(), get(), get(), get(), get(),
        )
    }
}

val utilsModule = module {
    single { LocationUtils() }
    single { NotificationUtils(get()) }
    factory {
        get<Context>().getSystemService(Context.LOCATION_SERVICE) as LocationManager
    }
}