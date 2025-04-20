package dev.randheer094.dev.location

import android.content.Context.INPUT_METHOD_SERVICE
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.view.inputmethod.InputMethodManager
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.LocalActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import dev.randheer094.dev.location.ui.theme.MockLocationTheme
import kotlinx.coroutines.launch

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            MockLocationTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    LocationInputForm(
                        modifier = Modifier
                            .padding(innerPadding)
                            .fillMaxSize()
                    )
                }
            }
        }
    }
}

@Composable
private fun LocationInputForm(modifier: Modifier = Modifier) {
    var latitude by remember { mutableStateOf("") }
    var longitude by remember { mutableStateOf("") }
    var error by remember { mutableStateOf<String?>(null) }
    val scope = rememberCoroutineScope()
    val context = LocalContext.current
    val activity = LocalActivity.current

    Column(
        modifier = modifier.padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        Text("Set Mock Location", style = MaterialTheme.typography.headlineMedium)

        OutlinedTextField(
            value = latitude,
            onValueChange = { latitude = it },
            label = { Text("Latitude") },
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
            label = { Text("Longitude") },
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
                color = MaterialTheme.colorScheme.error
            )
        }

        Button(
            onClick = {
                if (validateCoordinates(latitude, longitude)) {
                    error = null
                    scope.launch {
                        val inputMethodManager =
                            context.getSystemService(INPUT_METHOD_SERVICE) as InputMethodManager
                        inputMethodManager.hideSoftInputFromWindow(
                            activity?.currentFocus?.windowToken, 0
                        )
                        val intent = Intent(context, MockLocationService::class.java).apply {
                            putExtra("latitude", latitude.toDouble())
                            putExtra("longitude", longitude.toDouble())
                        }
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                            context.startForegroundService(intent)
                        } else {
                            context.startService(intent)
                        }
                        Toast.makeText(
                            context,
                            "Mock location set to: $latitude, $longitude",
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                } else {
                    error = "Invalid latitude or longitude"
                }
            },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Set Mock Location")
        }

        PredefinedLocationsSection(
            onLocationSelect = { loc ->
                latitude = loc.lat.toString()
                longitude = loc.lng.toString()
            }
        )
    }
}

@Composable
private fun PredefinedLocationsSection(
    onLocationSelect: (MockLocation) -> Unit,
    modifier: Modifier = Modifier
) {
    Column(modifier = modifier.fillMaxWidth()) {
        Text("Predefined Locations:", style = MaterialTheme.typography.titleMedium)
        
        MockLocation.getPredefinedLocations().forEach { location ->
            Button(
                onClick = { onLocationSelect(location) },
                modifier = Modifier.fillMaxWidth().padding(vertical = 4.dp)
            ) {
                Text("${location.name}: ${location.lat}, ${location.lng}")
            }
        }
    }
}

private fun validateCoordinates(latitude: String, longitude: String): Boolean {
    return try {
        val lat = latitude.toDouble()
        val long = longitude.toDouble()
        lat in -90.0..90.0 && long in -180.0..180.0
    } catch (e: NumberFormatException) {
        false
    }
}

@Preview(showBackground = true)
@Composable
private fun DefaultPreview() {
    MockLocationTheme {
        Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
            LocationInputForm(
                modifier = Modifier
                    .padding(innerPadding)
                    .fillMaxSize()
            )
        }
    }
}