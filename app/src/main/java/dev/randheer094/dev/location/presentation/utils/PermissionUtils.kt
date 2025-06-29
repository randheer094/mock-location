package dev.randheer094.dev.location.presentation.utils

import android.Manifest
import android.os.Build
import dev.shreyaspatil.permissionFlow.PermissionFlow
import dev.shreyaspatil.permissionFlow.PermissionState
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf

class PermissionUtils(
    private val permissionFlow: PermissionFlow,
) {

    fun getNotificationPermissionState(): Flow<PermissionState> {
        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            permissionFlow.getPermissionState(Manifest.permission.POST_NOTIFICATIONS)
        } else {
            flowOf(
                PermissionState(
                    permission = "android.permission.POST_NOTIFICATIONS",
                    isGranted = true,
                    isRationaleRequired = false,
                )
            )
        }
    }

}
