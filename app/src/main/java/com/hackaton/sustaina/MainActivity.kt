package com.hackaton.sustaina

import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import com.hackaton.sustaina.data.AUTH_PORT
import com.hackaton.sustaina.data.DATABASE_PORT
import com.hackaton.sustaina.data.LOCAL_HOST
import androidx.compose.foundation.isSystemInDarkTheme
import com.hackaton.sustaina.ui.navigation.Navigation
import com.hackaton.sustaina.ui.theme.SustainaTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        setContent {
            SustainaTheme(darkTheme = false) { // to simulate dark/light mode, default to white to focus on core features
                Navigation()
            }
        }
    }
}