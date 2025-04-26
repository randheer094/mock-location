package dev.randheer094.dev.location.presentation.mocklocation.composable

import android.Manifest
import android.os.Build
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import dev.shreyaspatil.permissionFlow.utils.launch
import dev.shreyaspatil.permissionflow.compose.rememberPermissionFlowRequestLauncher

@Composable
fun NotificationPermission() {
    val permissionLauncher = rememberPermissionFlowRequestLauncher()
    Scaffold { padding ->
        Column(
            modifier = Modifier
                .padding(padding)
                .padding(
                    vertical = 12.dp,
                    horizontal = 16.dp,
                ),
            verticalArrangement = Arrangement.spacedBy(16.dp),
        ) {
            Text(
                text = "Notification Permission required",
                style = MaterialTheme.typography.titleLarge,
            )

            Text(
                text = """
                    To ensure the mock location service runs reliably in the background, Android requires a foreground service notification.
                    Please grant notification permission to display this essential status notification.
                """.trimIndent(),
                style = MaterialTheme.typography.bodyLarge,
            )

            Button(
                onClick = {
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                        permissionLauncher.launch(Manifest.permission.POST_NOTIFICATIONS)
                    }
                },
            ) {
                Text(
                    text = "Allow Notifications!",
                    style = MaterialTheme.typography.bodyLarge,
                )
            }
        }
    }
}