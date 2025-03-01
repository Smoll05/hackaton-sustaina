package com.hackaton.sustaina

import android.os.Build
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.annotation.RequiresApi
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.colorResource
import androidx.core.content.ContextCompat
import androidx.core.view.WindowCompat
import com.hackaton.sustaina.ui.navigation.MainScreen
import com.hackaton.sustaina.ui.navigation.Navigation
import com.hackaton.sustaina.ui.theme.SustainaTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    @RequiresApi(Build.VERSION_CODES.Q)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        WindowCompat.setDecorFitsSystemWindows(window, true)

        window.navigationBarColor = ContextCompat.getColor(this, R.color.off_grey)

        setContent {
            SustainaTheme(darkTheme = false) {
                MainScreen()
            }
        }
    }
}
