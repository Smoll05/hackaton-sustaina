package com.hackaton.sustaina.ui.login

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.hackaton.sustaina.R
import com.hackaton.sustaina.ui.navigation.Routes

@Composable
fun LoginPage(navController: NavController, modifier: Modifier = Modifier) {

    var email by remember {
        mutableStateOf("")
    }

    var password by remember {
        mutableStateOf("")
    }

    Column(
        modifier = Modifier.fillMaxSize().padding(40.dp, 0.dp),
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
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(Modifier.height(16.dp))

        OutlinedTextField(
            value = password,
            onValueChange = {
                password = it
            },
            label = { Text("Password") },
            modifier = Modifier.fillMaxWidth(),
            visualTransformation = PasswordVisualTransformation()
        )

        Spacer(Modifier.height(20.dp))

        Button(
            onClick = { navController.navigate(Routes.SignOut.route)},
            modifier = Modifier.fillMaxWidth().height(50.dp),
            shape = RoundedCornerShape(5.dp)
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

        Spacer(Modifier.height(32.dp))

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.Center
        ) {
            Text("Don't have an account? ")
            Text(
                "Register",
                color = Color.Green,
                modifier = Modifier.clickable {
                    navController.navigate(Routes.Register.route)
                }
            )
        }
    }
}