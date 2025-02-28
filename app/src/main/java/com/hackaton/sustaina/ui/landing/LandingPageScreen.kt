package com.hackaton.sustaina.ui.landing

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.displayCutoutPadding
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.Icon
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.hackaton.sustaina.R
import com.hackaton.sustaina.domain.models.toLocalDateTime
import com.hackaton.sustaina.ui.navigation.Routes
import com.hackaton.sustaina.ui.theme.LeafyGreen
import com.hackaton.sustaina.ui.theme.NeonGreen
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

@Composable
fun LandingPageScreen(navController: NavController, viewModel: LandingPageViewModel = hiltViewModel()) {

    val uiState by viewModel.uiState.collectAsState()

    Column(modifier = Modifier
        .padding(24.dp)
        .fillMaxWidth()
        .displayCutoutPadding()) {

        Row(
            modifier = Modifier
                .fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = "Landing Page",
                fontSize = 28.sp,
                fontWeight = FontWeight.Bold
            )

            Button(
                modifier = Modifier
                    .height(55.dp)
                    .width(65.dp),
                onClick = { navController.navigate(Routes.Profile.route) }
            ) {
                Surface(
                    modifier = Modifier
                        .size(35.dp),
                    tonalElevation = 3.dp,
                    color = MaterialTheme.colorScheme.primary
                ) {
                    Icon(
                        painter = painterResource(R.drawable.icon_profile),
                        contentDescription = "User profile"
                    )
                }
            }


        }

        ElevatedCard(modifier = Modifier
            .fillMaxWidth()
            .padding(top = 22.dp)) {

            Row {
                Image(
                    painter = painterResource(R.drawable.medal),
                    contentDescription = null,
                    modifier = Modifier
                        .size(60.dp)
                        .align(Alignment.CenterVertically)
                        .padding(start = 8.dp)
                )

                Column {
                    Text(
                        text = "Level " + (uiState.user.userLevel),
                        fontSize = 20.sp,
                        fontWeight = FontWeight.Bold,
                        modifier = Modifier.padding(top = 16.dp, start = 16.dp)
                    )

                    LinearProgressIndicator(
                        progress = { uiState.progress},
                        color = LeafyGreen,
                        trackColor = NeonGreen,
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 16.dp, vertical = 8.dp)
                    )

                    Text(
                        text = "${uiState.user.userExp} / 1000 EXP",
                        fontSize = 16.sp,
                        modifier = Modifier.padding(start = 16.dp, bottom = 8.dp)
                    )
                }
            }
        }

        Card(modifier = Modifier
            .fillMaxWidth()
            .padding(top = 16.dp)) {

            Text(
                text = "Upcoming Campaigns",
                fontSize = 22.sp,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.padding(top = 20.dp, start = 16.dp)
            )

            if (uiState.upcomingCampaigns.isEmpty()) {
                Text(
                    text = "No Upcoming Campaigns!",
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier
                        .align(Alignment.CenterHorizontally)
                        .padding(vertical = 22.dp)
                )
            } else {
                LazyColumn(modifier = Modifier
                    .padding(16.dp)) {
                        items(uiState.upcomingCampaigns) {
                            UpcomingCampaign(it.campaignId, it.campaignName,
                                it.campaignStartDate.toLocalDateTime(), navController)
                        }
                }
            }
        }
    }
}

@Composable
fun UpcomingCampaign(campaignId: String, name: String, date: LocalDateTime, navController: NavController) {
    val formatter = DateTimeFormatter.ofPattern("MMMM dd, yyyy hh:mm a")
    ElevatedCard(modifier = Modifier
        .fillMaxWidth()
        .clickable {
            navController.navigate("AboutIssue/${campaignId}")
        }
        .padding(vertical = 8.dp)){
        Column(modifier = Modifier.padding(22.dp)) {

            Text(
                text = name,
                fontWeight = FontWeight.Bold,
                fontSize = 22.sp
            )

            Text(
                text = "Starts in " + formatter.format(date),
                fontSize = 14.sp
            )
        }
    }
}