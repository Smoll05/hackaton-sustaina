package com.hackaton.sustaina

import android.os.Build
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.annotation.RequiresApi
import androidx.core.content.ContextCompat
import androidx.core.view.WindowCompat
import com.hackaton.sustaina.ui.navigation.MainScreen
import com.hackaton.sustaina.ui.theme.SustainaTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    @RequiresApi(Build.VERSION_CODES.Q)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        WindowCompat.setDecorFitsSystemWindows(window, true)
//        window.navigationBarColor = ContextCompat.getColor(this, R.color.off_grey)

        setContent {
            SustainaTheme(darkTheme = false) {
                MainScreen()
            }
        }
    }
}
