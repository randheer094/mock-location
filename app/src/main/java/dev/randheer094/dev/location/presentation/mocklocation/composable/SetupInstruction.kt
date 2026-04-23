package dev.randheer094.dev.location.presentation.mocklocation.composable

import android.app.Activity
import android.content.Intent
import android.provider.Settings
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.ArrowOutward
import androidx.compose.material.icons.outlined.Info
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.graphics.PathEffect
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import dev.randheer094.dev.location.R
import dev.randheer094.dev.location.presentation.theme.InterFamily
import dev.randheer094.dev.location.presentation.theme.JetBrainsMonoFamily
import dev.randheer094.dev.location.presentation.theme.LocalMockColors
import dev.randheer094.dev.location.presentation.theme.MockColors
import dev.randheer094.dev.location.presentation.theme.MockLocationTheme

@Composable
fun SetupInstruction(onGotIt: () -> Unit) {
    val colors = LocalMockColors.current
    val context = LocalContext.current

    Scaffold(
        containerColor = colors.bg,
        contentWindowInsets = WindowInsets(0, 0, 0, 0),
    ) { _ ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .statusBarsPadding()
                .navigationBarsPadding()
                .verticalScroll(rememberScrollState()),
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp),
                verticalAlignment = Alignment.CenterVertically,
            ) {
                IconButton(
                    onClick = onGotIt,
                    modifier = Modifier.size(40.dp),
                ) {
                    Icon(
                        imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                        contentDescription = null,
                        tint = colors.text,
                    )
                }
                Text(
                    text = stringResource(R.string.setup_step_label, 1, 3),
                    style = MaterialTheme.typography.labelLarge,
                    color = colors.textDim,
                    modifier = Modifier.weight(1f),
                    textAlign = TextAlign.Center,
                )
                Spacer(modifier = Modifier.size(40.dp))
            }

            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 20.dp)
                    .padding(top = 24.dp),
            ) {
                Surface(
                    shape = RoundedCornerShape(100.dp),
                    color = colors.accentSoft,
                    border = BorderStroke(1.dp, colors.accentGhost),
                ) {
                    Row(
                        modifier = Modifier.padding(
                            start = 9.dp, top = 6.dp, end = 11.dp, bottom = 6.dp,
                        ),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(4.dp),
                    ) {
                        Icon(
                            imageVector = Icons.Outlined.Info,
                            contentDescription = null,
                            tint = colors.accent,
                            modifier = Modifier.size(14.dp),
                        )
                        Text(
                            text = stringResource(R.string.overline_one_time_setup),
                            style = TextStyle(
                                fontFamily = InterFamily,
                                fontWeight = FontWeight.Bold,
                                fontSize = 11.sp,
                                letterSpacing = 1.6.sp,
                            ),
                            color = colors.accent,
                        )
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))

                Text(
                    text = stringResource(R.string.setup_headline_line1),
                    style = TextStyle(
                        fontFamily = InterFamily,
                        fontWeight = FontWeight.Bold,
                        fontSize = 34.sp,
                        lineHeight = (34 * 1.04).sp,
                        letterSpacing = (-1.2).sp,
                    ),
                    color = colors.text,
                )
                Text(
                    text = stringResource(R.string.setup_headline_line2),
                    style = TextStyle(
                        fontFamily = InterFamily,
                        fontWeight = FontWeight.Bold,
                        fontSize = 34.sp,
                        lineHeight = (34 * 1.04).sp,
                        letterSpacing = (-1.2).sp,
                    ),
                    color = colors.textDim,
                )

                Spacer(modifier = Modifier.height(12.dp))

                Text(
                    text = stringResource(R.string.setup_supporting_text),
                    style = TextStyle(
                        fontFamily = InterFamily,
                        fontWeight = FontWeight.Medium,
                        fontSize = 14.sp,
                        lineHeight = (14 * 1.5).sp,
                    ),
                    color = colors.textDim,
                )
            }

            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp)
                    .padding(top = 20.dp),
                verticalArrangement = Arrangement.spacedBy(10.dp),
            ) {
                StepCard(
                    number = "01",
                    title = stringResource(R.string.step1_title),
                    body = stringResource(R.string.step1_body),
                    isAccent = true,
                    codeBlock = stringResource(R.string.step1_code),
                    colors = colors,
                )
                StepCard(
                    number = "02",
                    title = stringResource(R.string.step2_title),
                    body = stringResource(R.string.step2_body),
                    isAccent = false,
                    codeBlock = null,
                    colors = colors,
                )
                StepCard(
                    number = "03",
                    title = stringResource(R.string.step3_title),
                    body = stringResource(R.string.step3_body),
                    isAccent = false,
                    codeBlock = null,
                    colors = colors,
                )
            }

            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp)
                    .padding(top = 18.dp),
                verticalArrangement = Arrangement.spacedBy(10.dp),
            ) {
                Button(
                    onClick = {
                        (context as? Activity)?.startActivity(
                            Intent(Settings.ACTION_APPLICATION_DEVELOPMENT_SETTINGS),
                        )
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(54.dp),
                    shape = RoundedCornerShape(16.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = colors.accent,
                        contentColor = colors.accentInk,
                    ),
                ) {
                    Text(
                        text = stringResource(R.string.btn_open_dev_options),
                        style = MaterialTheme.typography.labelLarge,
                    )
                    Spacer(modifier = Modifier.width(6.dp))
                    Icon(
                        imageVector = Icons.Filled.ArrowOutward,
                        contentDescription = null,
                        modifier = Modifier.size(16.dp),
                    )
                }

                OutlinedButton(
                    onClick = onGotIt,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(48.dp),
                    shape = RoundedCornerShape(14.dp),
                    border = BorderStroke(1.dp, colors.border),
                    colors = ButtonDefaults.outlinedButtonColors(
                        contentColor = colors.textDim,
                    ),
                ) {
                    Text(
                        text = stringResource(R.string.btn_check_again),
                        style = MaterialTheme.typography.labelLarge,
                    )
                }
            }

            Spacer(modifier = Modifier.height(24.dp))
        }
    }
}

@Composable
private fun StepCard(
    number: String,
    title: String,
    body: String,
    isAccent: Boolean,
    codeBlock: String?,
    colors: MockColors,
) {
    val codeBlockBg = colors.chipBg
    val borderStrong = colors.borderStrong

    Surface(
        shape = RoundedCornerShape(18.dp),
        color = colors.card,
        border = BorderStroke(1.dp, colors.border),
        modifier = Modifier.fillMaxWidth(),
    ) {
        Row(
            modifier = Modifier.padding(14.dp),
            horizontalArrangement = Arrangement.spacedBy(12.dp),
        ) {
            Surface(
                shape = RoundedCornerShape(10.dp),
                color = if (isAccent) colors.accent else colors.chipBg,
                modifier = Modifier.size(32.dp),
            ) {
                Box(contentAlignment = Alignment.Center) {
                    Text(
                        text = number,
                        style = TextStyle(
                            fontFamily = JetBrainsMonoFamily,
                            fontWeight = FontWeight.Bold,
                            fontSize = 14.sp,
                        ),
                        color = if (isAccent) colors.accentInk else colors.textDim,
                    )
                }
            }

            Column(
                modifier = Modifier.weight(1f),
                verticalArrangement = Arrangement.spacedBy(4.dp),
            ) {
                Text(
                    text = title,
                    style = TextStyle(
                        fontFamily = InterFamily,
                        fontWeight = FontWeight.SemiBold,
                        fontSize = 15.sp,
                        letterSpacing = (-0.2).sp,
                    ),
                    color = colors.text,
                )
                Text(
                    text = body,
                    style = TextStyle(
                        fontFamily = InterFamily,
                        fontWeight = FontWeight.Medium,
                        fontSize = 13.sp,
                        lineHeight = (13 * 1.5).sp,
                    ),
                    color = colors.textDim,
                )
                if (codeBlock != null) {
                    Spacer(modifier = Modifier.height(6.dp))
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .drawBehind {
                                drawRoundRect(
                                    color = codeBlockBg,
                                    cornerRadius = CornerRadius(8.dp.toPx()),
                                )
                                drawRoundRect(
                                    color = borderStrong,
                                    style = Stroke(
                                        width = 1.dp.toPx(),
                                        pathEffect = PathEffect.dashPathEffect(
                                            floatArrayOf(4f, 4f),
                                        ),
                                    ),
                                    cornerRadius = CornerRadius(8.dp.toPx()),
                                )
                            }
                            .padding(10.dp),
                    ) {
                        Text(
                            text = codeBlock,
                            style = TextStyle(
                                fontFamily = JetBrainsMonoFamily,
                                fontWeight = FontWeight.Medium,
                                fontSize = 11.sp,
                            ),
                            color = colors.textDim,
                        )
                    }
                }
            }
        }
    }
}

@Preview(name = "Light")
@Composable
private fun SetupInstructionLightPreview() {
    MockLocationTheme(darkTheme = false) {
        SetupInstruction(onGotIt = {})
    }
}

@Preview(name = "Dark")
@Composable
private fun SetupInstructionDarkPreview() {
    MockLocationTheme(darkTheme = true) {
        SetupInstruction(onGotIt = {})
    }
}
