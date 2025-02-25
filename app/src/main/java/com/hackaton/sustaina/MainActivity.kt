package com.hackaton.sustaina

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import com.hackaton.sustaina.ui.navigation.Navigation
import com.hackaton.sustaina.ui.theme.SustainaTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
//            SustainaTheme(darkTheme = false) { to simulate dark/light mode
            SustainaTheme() {
                Navigation()
            }
        }
    }
}