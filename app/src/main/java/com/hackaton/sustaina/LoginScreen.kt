package com.hackaton.sustaina

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Button
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.hackaton.sustaina.ui.theme.SustainaTheme

@Composable
fun LoginPage(modifier: Modifier = Modifier) {

    var email by remember {
        mutableStateOf("")
    }

    var password by remember {
        mutableStateOf("")
    }

    Column(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Image(
            painter = painterResource(R.drawable.boy_planting_sapling),
            contentDescription = "Company Logo",
            contentScale = ContentScale.Fit,
            modifier = Modifier.size(150.dp)
        )

        Text(
            text = "Sustaina",
            fontSize = 32.sp,
            fontWeight = FontWeight.Bold
        )

        Spacer(Modifier.height(4.dp))

        Text(
            text = "Welcome back!",
            fontSize = 24.sp,
            fontWeight = FontWeight.SemiBold
        )

        Spacer(Modifier.height(16.dp))

        OutlinedTextField(
            value = email,
            onValueChange = {
                email = it
            },
            label = { Text("Email") },
        )

        Spacer(Modifier.height(16.dp))

        OutlinedTextField(
            value = password,
            onValueChange = {
                password = it
            },
            label = { Text("Password") },
            visualTransformation = PasswordVisualTransformation()
        )

        Spacer(Modifier.height(16.dp))

        Button(
           onClick = {}
        ) {
            Text(text = "Login")
        }

        Spacer(Modifier.height(32.dp))

        Text(text = "Forgor Password?")

        Spacer(Modifier.height(32.dp))

        Text(text = "Or sign in with")

        Spacer(Modifier.height(32.dp))

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceEvenly
        ) {
            Image(
                painter = painterResource(R.drawable.logo_facebook),
                contentDescription = "Facebook Logo",
                modifier = Modifier.size(60.dp)
            )
            Image(
                painter = painterResource(R.drawable.logo_google),
                contentDescription = "Google Logo",
                modifier = Modifier.size(60.dp)
            )
            Image(
                painter = painterResource(R.drawable.logo_twitter),
                contentDescription = "Google Logo",
                modifier = Modifier.size(60.dp)
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
fun Preview() {
    SustainaTheme {
        LoginPage()
    }
}
