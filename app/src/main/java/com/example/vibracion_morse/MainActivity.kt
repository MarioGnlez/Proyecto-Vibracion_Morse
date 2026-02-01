package com.example.vibracion_morse

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import com.example.vibracion_morse.ui.theme.Vibracion_MorseTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            Vibracion_MorseTheme {
                AppNavigation()
            }
        }
    }
}