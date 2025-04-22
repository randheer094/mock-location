package dev.randheer094.dev.location.presentation.mocklocation.composable

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

@Composable
fun SetupInstruction(onGotIt: () -> Unit) {
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
                text = "Setup Instructions",
                style = MaterialTheme.typography.bodyLarge,
            )

            Text(
                text = """
                    Select Mock Location App:
                    - Go to Settings > Developer options.
                    - Scroll down and tap "Select mock location app".
                    - Choose this app from the list.
                """.trimIndent(),
                style = MaterialTheme.typography.bodyLarge,
            )

            Button(onClick = onGotIt) { Text("Got it!") }
        }
    }
}