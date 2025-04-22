package dev.randheer094.dev.location.app

import android.app.Application
import dev.randheer094.dev.location.di.dataSourceModule
import dev.randheer094.dev.location.di.mapperModule
import dev.randheer094.dev.location.di.useCaseModule
import dev.randheer094.dev.location.di.utilsModule
import dev.randheer094.dev.location.di.viewModelModule
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.startKoin

class MockLocationApp : Application() {

    override fun onCreate() {
        super.onCreate()
        startKoin {
            androidContext(this@MockLocationApp)
            modules(
                listOf(
                    dataSourceModule,
                    useCaseModule,
                    mapperModule,
                    viewModelModule,
                    utilsModule,
                )
            )
        }
    }
}