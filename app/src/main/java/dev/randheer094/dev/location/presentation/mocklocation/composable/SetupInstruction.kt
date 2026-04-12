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
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import dev.randheer094.dev.location.R

@Composable
fun SetupInstruction(onGotIt: () -> Unit) {
    Scaffold {
        Column(
            modifier = Modifier
                .padding(it)
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp),
        ) {
            Text(
                text = stringResource(R.string.setup_instructions_title),
                style = MaterialTheme.typography.titleLarge,
            )

            Text(
                text = stringResource(R.string.setup_instructions_body),
                style = MaterialTheme.typography.bodyLarge,
            )

            Button(onClick = onGotIt) {
                Text(
                    text = stringResource(R.string.setup_instructions_got_it),
                    style = MaterialTheme.typography.bodyLarge,
                )
            }
        }
    }
}
