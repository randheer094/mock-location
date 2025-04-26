package dev.randheer094.dev.location.presentation.mocklocation.composable

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import dev.randheer094.dev.location.domain.MockLocation
import kotlinx.coroutines.launch

@Composable
fun AddMockLocationBottomSheet(
    modifier: Modifier = Modifier,
    onSubmit: (location: MockLocation) -> Unit,
) {
    var name by remember { mutableStateOf("") }
    var latitude by remember { mutableStateOf("") }
    var longitude by remember { mutableStateOf("") }
    var error by remember { mutableStateOf<String?>(null) }
    val scope = rememberCoroutineScope()

    Column(
        modifier = modifier
            .padding(horizontal = 16.dp)
            .padding(bottom = 16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        Text(
            "Add Mock Location Details",
            style = MaterialTheme.typography.titleLarge,
        )

        OutlinedTextField(
            value = name,
            onValueChange = { name = it },
            label = {
                Text(
                    text = "Name",
                    style = MaterialTheme.typography.bodyMedium,
                )
            },
            singleLine = true,
            keyboardOptions = KeyboardOptions(
                keyboardType = KeyboardType.Text,
                imeAction = ImeAction.Next
            ),
            modifier = Modifier.fillMaxWidth()
        )

        OutlinedTextField(
            value = latitude,
            onValueChange = { latitude = it },
            label = {
                Text(
                    text = "Latitude",
                    style = MaterialTheme.typography.bodyMedium,
                )
            },
            singleLine = true,
            keyboardOptions = KeyboardOptions(
                keyboardType = KeyboardType.Number,
                imeAction = ImeAction.Next
            ),
            modifier = Modifier.fillMaxWidth()
        )

        OutlinedTextField(
            value = longitude,
            onValueChange = { longitude = it },
            label = {
                Text(
                    text = "Longitude",
                    style = MaterialTheme.typography.bodyMedium,
                )
            },
            singleLine = true,
            keyboardOptions = KeyboardOptions(
                keyboardType = KeyboardType.Number,
                imeAction = ImeAction.Done
            ),
            modifier = Modifier.fillMaxWidth()
        )

        if (error != null) {
            Text(
                text = error!!,
                color = MaterialTheme.colorScheme.error,
                style = MaterialTheme.typography.bodyMedium,
            )
        }

        Button(
            onClick = {
                if (validateCoordinates(latitude, longitude)) {
                    error = null
                    scope.launch {
                        onSubmit(
                            MockLocation(
                                name = "(Manual) $name",
                                lat = latitude.toDoubleOrNull() ?: 0.0,
                                long = longitude.toDoubleOrNull() ?: 0.0,
                            )
                        )
                    }
                } else {
                    error = "Invalid latitude or longitude"
                }
            },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text(
                text = "Set Mock Location",
                style = MaterialTheme.typography.bodyLarge,
            )
        }
    }
}


private fun validateCoordinates(latitude: String, longitude: String): Boolean {
    return try {
        val lat = latitude.toDoubleOrNull() ?: -270.0
        val long = longitude.toDoubleOrNull() ?: -270.0
        lat in -90.0..90.0 && long in -180.0..180.0
    } catch (e: NumberFormatException) {
        false
    }
}