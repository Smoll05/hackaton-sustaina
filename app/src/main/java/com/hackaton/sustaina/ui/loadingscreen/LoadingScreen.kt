package com.hackaton.sustaina.ui.loadingscreen

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Card
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.hackaton.sustaina.ui.theme.SustainaTheme

@Composable
fun LoadingScreen() {
    SustainaTheme {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(Color.Black.copy(alpha = 0.3f))
                .clickable(enabled = false) {}
        ) {
            Card(modifier = Modifier
                .align(Alignment.Center)
            ) {
                CircularProgressIndicator(
                    modifier = Modifier
                        .size(80.dp)
                        .padding(all = 12.dp),
                    color = Color.White,
                    strokeWidth = 5.dp
                )
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun LoadingScreenPreview() {
    LoadingScreen()
}