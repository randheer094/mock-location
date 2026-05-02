package dev.randheer094.dev.location.presentation.utils

import android.app.AppOpsManager
import android.content.Context
import android.os.Build
import android.os.Process

class MockAppAuthorizationUtils(
    private val context: Context,
    private val appOpsManager: AppOpsManager,
) {
    @Suppress("DEPRECATION")
    fun isAuthorized(): Boolean {
        val mode = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            appOpsManager.unsafeCheckOpNoThrow(
                AppOpsManager.OPSTR_MOCK_LOCATION,
                Process.myUid(),
                context.packageName,
            )
        } else {
            appOpsManager.checkOpNoThrow(
                AppOpsManager.OPSTR_MOCK_LOCATION,
                Process.myUid(),
                context.packageName,
            )
        }
        return mode == AppOpsManager.MODE_ALLOWED
    }
}
