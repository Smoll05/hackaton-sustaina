package com.hackaton.sustaina.ui.profile

import android.icu.text.NumberFormat
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.hackaton.sustaina.ui.navigation.Routes
import com.hackaton.sustaina.R
import androidx.compose.ui.tooling.preview.Preview
import java.util.Locale

@Composable
fun ProfilePage(navController: NavController) {
    // Todo: Implement user data retrieval in here, temporary variables for now
    val userName = "John Doe"
    val userId = "12345678"
    val email = "johndoe@gmail.com"
    val campaigns = 123
    val experience = 12300
    val level = 10
    val images = listOf("")

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


        Column(
            modifier = Modifier
                .fillMaxWidth()
                .align(Alignment.BottomCenter)
                .background(
                    color = MaterialTheme.colorScheme.background,
                    shape = RoundedCornerShape(topStart = 50.dp, topEnd = 50.dp)
                )
                .offset(y = (-80).dp)
                .padding(horizontal = 40.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            Image(
                painter = painterResource(R.drawable.placeholder),
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

            Spacer(Modifier.height(30.dp))

            LazyRow(
                horizontalArrangement = Arrangement.SpaceEvenly,
                modifier = Modifier.fillMaxWidth()
            ) {
                item {
                    Column(
                        modifier = Modifier.width(80.dp),
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
                        modifier = Modifier.width(80.dp),
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
                        modifier = Modifier.width(80.dp),
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

            Spacer(Modifier.height(70.dp))

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
                        // Currently hardcoded
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
        }

        Image(
            painter = painterResource(R.drawable.icon_back),
            contentDescription = "Back Button",
            modifier = Modifier
                .size(50.dp)
                .graphicsLayer(
                    translationX = 100f,
                    translationY = 100f
                )
                .clickable {
                    navController.navigate(Routes.Landing.route)
                }
        )

    }
}

//@Preview(showBackground = true)
//@Composable
//fun PreviewProfilePage() {
//    ProfilePage(navController = rememberNavController())
//}