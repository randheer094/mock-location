package dev.randheer094.dev.location.presentation.mocklocation.composable

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import dev.randheer094.dev.location.R
import dev.randheer094.dev.location.domain.MockLocation

@Composable
fun AddMockLocationBottomSheet(
    modifier: Modifier = Modifier,
    onSubmit: (location: MockLocation) -> Unit,
) {
    var name by remember { mutableStateOf("") }
    var latitude by remember { mutableStateOf("") }
    var longitude by remember { mutableStateOf("") }
    var error by remember { mutableStateOf<String?>(null) }

    val errorMessage = stringResource(R.string.error_invalid_lat_long)
    val manualPrefixTemplate = stringResource(R.string.manual_location_prefix)

    Column(
        modifier = modifier
            .padding(horizontal = 24.dp)
            .padding(bottom = 24.dp)
            .navigationBarsPadding(),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        Text(
            text = stringResource(R.string.add_mock_location_title),
            style = MaterialTheme.typography.headlineSmall,
            color = MaterialTheme.colorScheme.onSurface,
        )

        Text(
            text = stringResource(R.string.add_mock_location_subtitle),
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
        )

        OutlinedTextField(
            value = name,
            onValueChange = { name = it },
            label = { Text(stringResource(R.string.label_name)) },
            singleLine = true,
            shape = MaterialTheme.shapes.medium,
            keyboardOptions = KeyboardOptions(
                keyboardType = KeyboardType.Text,
                imeAction = ImeAction.Next
            ),
            modifier = Modifier.fillMaxWidth()
        )

        OutlinedTextField(
            value = latitude,
            onValueChange = {
                latitude = it
                if (error != null) error = null
            },
            label = { Text(stringResource(R.string.label_latitude)) },
            singleLine = true,
            shape = MaterialTheme.shapes.medium,
            keyboardOptions = KeyboardOptions(
                keyboardType = KeyboardType.Number,
                imeAction = ImeAction.Next
            ),
            modifier = Modifier.fillMaxWidth(),
            isError = error != null,
            supportingText = if (error != null) {
                { Text(text = error!!) }
            } else {
                null
            },
        )

        OutlinedTextField(
            value = longitude,
            onValueChange = {
                longitude = it
                if (error != null) error = null
            },
            label = { Text(stringResource(R.string.label_longitude)) },
            singleLine = true,
            shape = MaterialTheme.shapes.medium,
            keyboardOptions = KeyboardOptions(
                keyboardType = KeyboardType.Number,
                imeAction = ImeAction.Done
            ),
            modifier = Modifier.fillMaxWidth(),
            isError = error != null,
        )

        Button(
            onClick = {
                val lat = latitude.toDoubleOrNull()
                val long = longitude.toDoubleOrNull()

                if (lat == null || long == null || lat !in -90.0..90.0 || long !in -180.0..180.0) {
                    error = errorMessage
                    return@Button
                }

                error = null
                val displayName = manualPrefixTemplate.format(name.ifBlank { "${lat}, ${long}" })
                onSubmit(
                    MockLocation(
                        name = displayName,
                        lat = lat,
                        long = long,
                    )
                )
            },
            modifier = Modifier.fillMaxWidth(),
            shape = MaterialTheme.shapes.large,
            colors = ButtonDefaults.buttonColors(
                containerColor = MaterialTheme.colorScheme.primary,
                contentColor = MaterialTheme.colorScheme.onPrimary,
            ),
        ) {
            Icon(imageVector = Icons.Default.Check, contentDescription = null)
            Spacer(Modifier.size(8.dp))
            Text(
                text = stringResource(R.string.cta_set_mock_location),
                style = MaterialTheme.typography.labelLarge,
            )
        }
    }
}
