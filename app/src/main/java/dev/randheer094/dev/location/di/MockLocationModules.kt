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
import dev.randheer094.dev.location.presentation.utils.PermissionUtils
import dev.shreyaspatil.permissionFlow.PermissionFlow
import kotlinx.serialization.json.Json
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module



private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(
    name = "m_l"
)

val appModule = module {
    single { get<Context>().dataStore }
    single {
        Json {
            ignoreUnknownKeys = true
        }
    }

    factory { GetMockLocationsUseCase(get(), get()) }
    factory { MockLocationStatusUseCase(get()) }
    factory { SetMockLocationStatusUseCase(get()) }
    factory { SelectMockLocationUseCase(get(), get()) }
    factory { SelectedMockLocationUseCase(get(), get()) }
    factory { SetSetupInstructionStatusUseCase(get()) }
    factory { SetupInstructionStatusUseCase(get()) }

    single { UiStateMapper() }

    viewModel {
        MockLocationViewModel(
            get(), get(), get(), get(), get(), get(), get(), get(), get(), get(), get(),
        )
    }

    single { LocationUtils() }
    single { NotificationUtils(get()) }
    single { PermissionUtils(get()) }
    factory {
        get<Context>().getSystemService(Context.LOCATION_SERVICE) as LocationManager
    }
    factory { PermissionFlow.getInstance() }
}
