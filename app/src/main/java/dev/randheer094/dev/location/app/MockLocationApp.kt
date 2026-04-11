package dev.randheer094.dev.location.app

import android.app.Application
import dev.randheer094.dev.location.di.appModule
import dev.shreyaspatil.permissionFlow.PermissionFlow
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.startKoin

class MockLocationApp : Application() {
    override fun onCreate() {
        super.onCreate()
        // PermissionFlow must be initialized before any call to PermissionFlow.getInstance().
        // Without this, PermissionUtils crashes the first time the notification permission
        // state is collected.
        PermissionFlow.init(this)
        startKoin {
            androidContext(this@MockLocationApp)
            modules(appModule)
        }
    }
}
