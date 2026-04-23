package dev.randheer094.dev.location.presentation.mocklocation.composable

import android.content.res.Configuration
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Check
import androidx.compose.material.icons.outlined.Close
import androidx.compose.material.icons.outlined.Warning
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import dev.randheer094.dev.location.R
import dev.randheer094.dev.location.domain.MockLocation
import dev.randheer094.dev.location.presentation.theme.JetBrainsMonoFamily
import dev.randheer094.dev.location.presentation.theme.LocalMockColors
import dev.randheer094.dev.location.presentation.theme.MockLocationTheme
import dev.randheer094.dev.location.presentation.ui.icons.MockIcons
import kotlin.math.atan2
import kotlin.math.cos
import kotlin.math.pow
import kotlin.math.sin
import kotlin.math.sqrt

private data class PresetCity(val name: String, val lat: Double, val lng: Double)

private val PRESET_CITIES = listOf(
    PresetCity("Singapore", 1.2806319, 103.8501362),
    PresetCity("Stockholm", 59.3383223, 18.0549621),
    PresetCity("Istanbul", 41.0763745, 29.0114464),
    PresetCity("Hong Kong", 22.2848692, 114.1410492),
    PresetCity("Kuala Lumpur", 3.1115726, 101.6633107),
    PresetCity("Oslo", 59.9271583, 10.7246093),
    PresetCity("Dhaka", 23.8516649, 90.2535154),
    PresetCity("Karachi", 24.8653886, 67.0535651),
    PresetCity("Manila", 14.5574546, 120.9863673),
    PresetCity("Taipei", 25.0415595, 121.5630013),
    PresetCity("Cambodia", 11.5526867, 104.9374995),
    PresetCity("Laos", 17.9772614, 102.6132033),
    PresetCity("Myanmar", 16.8422397, 96.1543628),
    PresetCity("Prague", 50.1039621, 14.528223),
    PresetCity("Budapest", 47.4752135, 19.0691096),
    PresetCity("Vienna", 48.1972505, 16.3857097),
)

private fun closestCityName(lat: Double, lng: Double): String {
    return PRESET_CITIES.minByOrNull { city ->
        val dLat = Math.toRadians(city.lat - lat)
        val dLng = Math.toRadians(city.lng - lng)
        val a = sin(dLat / 2).pow(2) +
                cos(Math.toRadians(lat)) * cos(Math.toRadians(city.lat)) * sin(dLng / 2).pow(2)
        atan2(sqrt(a), sqrt(1 - a))
    }?.name ?: PRESET_CITIES[0].name
}

@Composable
fun AddMockLocationBottomSheet(
    modifier: Modifier = Modifier,
    onDismiss: () -> Unit,
    onSubmit: (location: MockLocation) -> Unit,
) {
    val colors = LocalMockColors.current
    var name by remember { mutableStateOf("") }
    var latitude by remember { mutableStateOf("") }
    var longitude by remember { mutableStateOf("") }

    val manualPrefixTemplate = stringResource(R.string.manual_location_prefix)

    val latDouble = latitude.toDoubleOrNull()
    val lngDouble = longitude.toDoubleOrNull()
    val latValid = latDouble != null && latDouble in -90.0..90.0
    val lngValid = lngDouble != null && lngDouble in -180.0..180.0
    val isValid = latValid && lngValid

    val showStrip = latitude.isNotBlank() || longitude.isNotBlank()

    val latOutOfRangeMsg = stringResource(R.string.validation_lat_out_of_range)
    val lngOutOfRangeMsg = stringResource(R.string.validation_lng_out_of_range)
    val invalidCoordMsg = stringResource(R.string.error_invalid_lat_long)

    val errorMsg: String? = when {
        !showStrip || isValid -> null
        latDouble != null && latDouble !in -90.0..90.0 -> latOutOfRangeMsg
        latitude.isNotBlank() && latDouble == null -> invalidCoordMsg
        lngDouble != null && lngDouble !in -180.0..180.0 -> lngOutOfRangeMsg
        longitude.isNotBlank() && lngDouble == null -> invalidCoordMsg
        else -> null
    }

    val closestCity = if (isValid) closestCityName(latDouble!!, lngDouble!!) else ""
    val validMsg = stringResource(R.string.validation_valid, closestCity)

    val fieldColors = OutlinedTextFieldDefaults.colors(
        unfocusedContainerColor = colors.chipBg,
        focusedContainerColor = colors.chipBg,
        unfocusedBorderColor = colors.borderStrong,
        focusedBorderColor = colors.accent,
        unfocusedTextColor = colors.text,
        focusedTextColor = colors.text,
        unfocusedSupportingTextColor = colors.textMute,
        focusedSupportingTextColor = colors.textMute,
        unfocusedLabelColor = colors.textDim,
        focusedLabelColor = colors.accent,
    )

    Column(
        modifier = modifier
            .padding(horizontal = 20.dp)
            .padding(top = 10.dp)
            .navigationBarsPadding()
            .padding(bottom = 28.dp),
    ) {
        // Title row
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Text(
                text = stringResource(R.string.sheet_title_custom_location),
                style = MaterialTheme.typography.titleLarge,
                color = colors.text,
            )
            IconButton(
                onClick = onDismiss,
                modifier = Modifier.size(32.dp),
            ) {
                Icon(
                    imageVector = Icons.Outlined.Close,
                    contentDescription = stringResource(R.string.cd_close_sheet),
                    tint = colors.textDim,
                    modifier = Modifier.size(20.dp),
                )
            }
        }

        Spacer(Modifier.height(8.dp))

        Text(
            text = stringResource(R.string.sheet_subtitle),
            style = MaterialTheme.typography.labelLarge.copy(fontWeight = FontWeight.Medium),
            color = colors.textDim,
        )

        Spacer(Modifier.height(16.dp))

        // Name field with overline label
        Text(
            text = stringResource(R.string.overline_name),
            style = MaterialTheme.typography.labelSmall,
            color = colors.textDim,
        )
        Spacer(Modifier.height(4.dp))
        OutlinedTextField(
            value = name,
            onValueChange = { name = it },
            singleLine = true,
            shape = RoundedCornerShape(14.dp),
            colors = fieldColors,
            keyboardOptions = KeyboardOptions(
                keyboardType = KeyboardType.Text,
                imeAction = ImeAction.Next,
            ),
            supportingText = {
                Text(
                    text = stringResource(R.string.helper_name),
                    style = MaterialTheme.typography.bodySmall,
                    color = colors.textMute,
                )
            },
            modifier = Modifier
                .fillMaxWidth()
                .testTag("name_field"),
        )

        Spacer(Modifier.height(8.dp))

        // Latitude / Longitude side-by-side row
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(10.dp),
        ) {
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = stringResource(R.string.overline_latitude),
                    style = MaterialTheme.typography.labelSmall,
                    color = colors.textDim,
                )
                Spacer(Modifier.height(4.dp))
                OutlinedTextField(
                    value = latitude,
                    onValueChange = { newVal ->
                        if (newVal.contains(",")) {
                            val parts = newVal.split(",")
                            latitude = parts[0].trim()
                            longitude = parts.getOrElse(1) { "" }.trim()
                        } else {
                            latitude = newVal
                        }
                    },
                    singleLine = true,
                    shape = RoundedCornerShape(14.dp),
                    textStyle = MaterialTheme.typography.bodyMedium.copy(fontFamily = JetBrainsMonoFamily),
                    colors = fieldColors,
                    keyboardOptions = KeyboardOptions(
                        keyboardType = KeyboardType.Number,
                        imeAction = ImeAction.Next,
                    ),
                    supportingText = {
                        Text(
                            text = stringResource(R.string.helper_latitude),
                            style = MaterialTheme.typography.bodySmall,
                            color = colors.textMute,
                        )
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .testTag("lat_field"),
                )
            }
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = stringResource(R.string.overline_longitude),
                    style = MaterialTheme.typography.labelSmall,
                    color = colors.textDim,
                )
                Spacer(Modifier.height(4.dp))
                OutlinedTextField(
                    value = longitude,
                    onValueChange = { newVal ->
                        if (newVal.contains(",")) {
                            val parts = newVal.split(",")
                            latitude = parts[0].trim()
                            longitude = parts.getOrElse(1) { "" }.trim()
                        } else {
                            longitude = newVal
                        }
                    },
                    singleLine = true,
                    shape = RoundedCornerShape(14.dp),
                    textStyle = MaterialTheme.typography.bodyMedium.copy(fontFamily = JetBrainsMonoFamily),
                    colors = fieldColors,
                    keyboardOptions = KeyboardOptions(
                        keyboardType = KeyboardType.Number,
                        imeAction = ImeAction.Done,
                    ),
                    supportingText = {
                        Text(
                            text = stringResource(R.string.helper_longitude),
                            style = MaterialTheme.typography.bodySmall,
                            color = colors.textMute,
                        )
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .testTag("lng_field"),
                )
            }
        }

        // Validation strip (12dp top spacing, shown when at least one coord field has content)
        Spacer(Modifier.height(12.dp))
        if (showStrip && (isValid || errorMsg != null)) {
            val stripBg = if (isValid) colors.accentSoft else colors.danger.copy(alpha = 0.14f)
            val stripBorder = if (isValid) colors.accentGhost else colors.danger.copy(alpha = 0.24f)
            val stripIcon = if (isValid) Icons.Outlined.Check else Icons.Outlined.Warning
            val stripIconColor = if (isValid) colors.accent else colors.danger
            val stripText = if (isValid) validMsg else errorMsg!!

            Surface(
                shape = RoundedCornerShape(12.dp),
                color = stripBg,
                modifier = Modifier
                    .fillMaxWidth()
                    .border(1.dp, stripBorder, RoundedCornerShape(12.dp)),
            ) {
                Row(
                    modifier = Modifier.padding(horizontal = 12.dp, vertical = 8.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(6.dp),
                ) {
                    Icon(
                        imageVector = stripIcon,
                        contentDescription = null,
                        tint = stripIconColor,
                        modifier = Modifier.size(16.dp),
                    )
                    Text(
                        text = stripText,
                        style = MaterialTheme.typography.labelMedium.copy(fontWeight = FontWeight.SemiBold),
                        color = stripIconColor,
                    )
                }
            }

            Spacer(Modifier.height(12.dp))
        }

        // CTA button (accent glow shadow when enabled, 50% alpha when disabled)
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .then(
                    if (isValid)
                        Modifier.shadow(
                            elevation = 14.dp,
                            shape = RoundedCornerShape(16.dp),
                            clip = false,
                            spotColor = colors.liveGlow,
                        )
                    else
                        Modifier,
                ),
        ) {
            Button(
                onClick = {
                    val lat = latDouble ?: return@Button
                    val lng = lngDouble ?: return@Button
                    val displayName = manualPrefixTemplate.format(
                        name.ifBlank { "$lat, $lng" }
                    )
                    onSubmit(MockLocation(name = displayName, lat = lat, long = lng))
                },
                enabled = isValid,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(56.dp)
                    .testTag("cta_button"),
                shape = RoundedCornerShape(16.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = colors.accent,
                    contentColor = colors.accentInk,
                    disabledContainerColor = colors.accent.copy(alpha = 0.5f),
                    disabledContentColor = colors.accentInk.copy(alpha = 0.5f),
                ),
            ) {
                Icon(
                    imageVector = MockIcons.Pin,
                    contentDescription = null,
                    modifier = Modifier.size(18.dp),
                )
                Spacer(Modifier.size(8.dp))
                Text(
                    text = stringResource(R.string.btn_set_mock_location_new),
                    style = MaterialTheme.typography.titleMedium,
                )
            }
        }

        // Tip line
        Spacer(Modifier.height(8.dp))
        Text(
            text = stringResource(R.string.tip_paste),
            style = MaterialTheme.typography.labelMedium,
            color = colors.textMute,
            textAlign = TextAlign.Center,
            modifier = Modifier.fillMaxWidth(),
        )
    }
}

@Preview(showBackground = true, name = "Light")
@Composable
private fun AddMockLocationBottomSheetPreviewLight() {
    MockLocationTheme {
        AddMockLocationBottomSheet(onDismiss = {}, onSubmit = {})
    }
}

@Preview(showBackground = true, name = "Dark", uiMode = Configuration.UI_MODE_NIGHT_YES)
@Composable
private fun AddMockLocationBottomSheetPreviewDark() {
    MockLocationTheme {
        AddMockLocationBottomSheet(onDismiss = {}, onSubmit = {})
    }
}
