package dev.randheer094.dev.location.presentation.main

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import dev.randheer094.dev.location.presentation.mocklocation.composable.MockLocationScreen
import dev.randheer094.dev.location.presentation.theme.MockLocationTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        enableEdgeToEdge()
        super.onCreate(savedInstanceState)
        setContent {
            MockLocationTheme {
                MockLocationScreen()
            }
        }
    }
}
