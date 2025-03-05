package com.hackaton.sustaina.ui.profile

import android.annotation.SuppressLint
import android.icu.text.NumberFormat
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.hackaton.sustaina.ui.navigation.Routes
import com.hackaton.sustaina.R
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.window.Dialog
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.compose.rememberNavController
import com.hackaton.sustaina.ui.loadingscreen.LoadingScreen
import java.util.Locale
import kotlin.random.Random

@SuppressLint("DiscouragedApi")
@Composable
fun ProfilePage(navController: NavController,
                profileViewModel: ProfileViewModel = hiltViewModel()) {

    val uiState by profileViewModel.uiState.collectAsState()

    val userName = uiState.userName
    val userId = uiState.userId
    val email = uiState.email
    val campaigns = uiState.joinedCampaigns.size
    val experience = uiState.currentExp
    val level = uiState.level

    val context = LocalContext.current
    val userProfile = uiState.profilePicUrl ?: "profile_1"
    val resourceId = context.resources.getIdentifier(userProfile, "drawable", context.packageName)

    if (uiState.loading) {
        LoadingScreen()
        return
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
    ) {
        Image(
            painter = painterResource(id = R.drawable.banner_image),
            contentDescription = "Banner Image",
            contentScale = ContentScale.Crop,
            modifier = Modifier
                .fillMaxWidth()
                .height(200.dp)
                .graphicsLayer(
                    scaleX = 1.3f,
                    scaleY = 1.3f,
                    alpha = 0.6f
                )
        )

        Box(
            modifier = Modifier
                .fillMaxWidth()
                .offset(y = 160.dp)
                .background(
                    color = MaterialTheme.colorScheme.background,
                    shape = RoundedCornerShape(topStart = 50.dp, topEnd = 50.dp)
                )
        ) {
            Box(
                modifier = Modifier.offset(y = (-70).dp)
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .align(Alignment.BottomCenter)
                        .background(
                            color = MaterialTheme.colorScheme.background,
                            shape = RoundedCornerShape(topStart = 50.dp, topEnd = 50.dp)
                        )
                        .offset(y = (-50).dp)
                        .padding(horizontal = 40.dp),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    Image(
                        painter = painterResource(resourceId),
                        contentDescription = "Profile Picture",
                        contentScale = ContentScale.Fit,
                        modifier = Modifier
                            .size(250.dp)
                            .clip(RoundedCornerShape(50.dp))
                    )

                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Text(
                            fontWeight = FontWeight.Bold,
                            fontSize = 25.sp,
                            text = userName
                        )
                        Text(text = "ID:\t$userId")
                        Text(text = email)
                    }

                    Spacer(Modifier.height(15.dp))

                    LazyRow(
                        horizontalArrangement = Arrangement.SpaceEvenly,
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        item {
                            Column(
                                modifier = Modifier.widthIn(80.dp, 100.dp),
                                horizontalAlignment = Alignment.CenterHorizontally
                            ) {
                                Text(
                                    fontWeight = FontWeight.Bold,
                                    fontSize = 25.sp,
                                    text = campaigns.toString(),
                                    textAlign = TextAlign.Center
                                )
                                Text(
                                    text = "Campaigns",
                                    textAlign = TextAlign.Center
                                )
                            }
                        }
                        item {
                            Column(
                                modifier = Modifier.widthIn(80.dp, 100.dp),
                                horizontalAlignment = Alignment.CenterHorizontally
                            ) {
                                Text(
                                    fontWeight = FontWeight.Bold,
                                    fontSize = 25.sp,
                                    text = NumberFormat.getNumberInstance(Locale.US).format(experience),
                                    textAlign = TextAlign.Center
                                )
                                Text(
                                    text = "Experience",
                                    textAlign = TextAlign.Center
                                )
                            }
                        }
                        item {
                            Column(
                                modifier = Modifier.widthIn(80.dp, 100.dp),
                                horizontalAlignment = Alignment.CenterHorizontally
                            ) {
                                Text(
                                    fontWeight = FontWeight.Bold,
                                    fontSize = 25.sp,
                                    text = level.toString(),
                                    textAlign = TextAlign.Center
                                )
                                Text(
                                    text = "Level",
                                    textAlign = TextAlign.Center
                                )

                            }
                        }
                    }

                    Spacer(Modifier.height(15.dp))

                    Box(
                        modifier = Modifier
                            .width(500.dp)
                            .height(200.dp)
                            .background(Color(0xFFD9D9D9), shape = RoundedCornerShape(30.dp))
                            .padding(20.dp)
                    ) {
                        Column(
                            modifier = Modifier.fillMaxSize(),
                            verticalArrangement = Arrangement.Top,
                            horizontalAlignment = Alignment.Start
                        ) {
                            Text(
                                text = "Gallery",
                                fontWeight = FontWeight.Bold,
                                fontSize = 25.sp,
                            )

                            Spacer(Modifier.height(20.dp))
                            LazyRow(
                                modifier = Modifier.clip(RoundedCornerShape(12.dp)),
                                horizontalArrangement = Arrangement.spacedBy(12.dp)
                            ) {
                                items(5) { index ->
                                    Image(
                                        painter = painterResource(id = R.drawable.white_placeholder),
                                        contentDescription = null,
                                        contentScale = ContentScale.Crop,
                                        modifier = Modifier
                                            .size(100.dp)
                                            .clip(RoundedCornerShape(12.dp))
                                    )
                                }
                            }
                        }

                    }

                    Spacer(Modifier.height(30.dp))

                    var showDialog by remember { mutableStateOf(false) }

                    Button(
                        onClick = { showDialog = true },
                        modifier = Modifier
                            .width(150.dp)
                            .height(50.dp),
                        shape = RoundedCornerShape(15.dp),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = Color(0xFFBE4F4F),
                            contentColor = MaterialTheme.colorScheme.onSurfaceVariant
                        )

                    ) {
                        Text(
                            fontWeight = FontWeight.Bold,
                            text = "Sign out",
                        )
                    }

                    if (showDialog) {

                        Dialog(onDismissRequest = { showDialog = false }) {
                            Surface(
                                color = MaterialTheme.colorScheme.onSurfaceVariant,
                                shape = RoundedCornerShape(30.dp),
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(20.dp)
                                    .shadow(
                                        elevation = 8.dp,
                                        shape = RoundedCornerShape(30.dp),
                                        clip = false
                                    )
                            ) {
                                Column(
                                    horizontalAlignment = Alignment.CenterHorizontally,
                                    modifier = Modifier.padding(15.dp)
                                ) {
                                    Spacer(modifier = Modifier.height(5.dp))
                                    Text("Confirm sign out", fontWeight = FontWeight.Bold)
                                    Spacer(modifier = Modifier.height(5.dp))
                                    HorizontalDivider(
                                        modifier = Modifier.padding(vertical = 8.dp),
                                        thickness = 1.dp,
                                        color = Color.Gray
                                    )
                                    Spacer(modifier = Modifier.height(15.dp))
                                    Text("Are you sure you want to sign out?")
                                    Spacer(modifier = Modifier.height(20.dp))
                                    Row(
                                        horizontalArrangement = Arrangement.SpaceEvenly,
                                        modifier = Modifier.align(Alignment.CenterHorizontally)
                                    ) {
                                        Button(
                                            onClick = {
                                                navController.navigate(Routes.Login.route) {
                                                    popUpTo(Routes.Landing.route) {
                                                        inclusive = true
                                                    }
                                                }

                                                profileViewModel.logout()
                                                showDialog = false
                                            },
                                            modifier = Modifier
                                                .width(110.dp)
                                                .padding(8.dp),
                                            colors = ButtonDefaults.buttonColors(
                                                Color(0xFFD36B6B),
                                                contentColor = Color.White
                                            )
                                        ) {
                                            Text("Yes")
                                        }

                                        Button(
                                            onClick = { showDialog = false },
                                            modifier = Modifier
                                                .width(110.dp)
                                                .padding(8.dp),
                                            colors = ButtonDefaults.buttonColors(
                                                Color(0xFFB9B9B9),
                                                contentColor = Color.White
                                            )
                                        ) {
                                            Text("No")
                                        }

                                    }
                                }
                            }
                        }
                    }
                }
            }
        }

                        Image(
            painter = painterResource(R.drawable.icon_back),
            contentDescription = "Back Button",
            modifier = Modifier
                .size(40.dp)
                .graphicsLayer(
                    translationX = 90f,
                    translationY = 90f
                )
                .clickable {
                    navController.navigate(Routes.Landing.route)
                }
        )
    }
}

@Preview(showBackground = true)
@Composable
fun PreviewProfilePage() {
    ProfilePage(navController = rememberNavController())
}

private fun getRandomProfilePicture(): String {
    val profileNumber = Random.nextInt(1, 6)
    return "profile_$profileNumber"
}