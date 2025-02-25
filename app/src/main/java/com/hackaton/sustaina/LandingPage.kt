package com.hackaton.sustaina

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.displayCutoutPadding
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Card
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.hackaton.sustaina.ui.theme.SustainaTheme
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

// TODO: Temporary; for testing purposes
data class Event(
    val name: String,
    val date: LocalDateTime
)

@Composable
fun LandingPage(navController: NavController, upcomingEvents: List<Event>) {
    Column(modifier = Modifier
        .padding(all = 24.dp)
        .fillMaxWidth()
        .displayCutoutPadding()) {

        Text(
            text = "Landing Page",
            fontSize = 28.sp,
            fontWeight = FontWeight.Bold
        )

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
                        text = "Level 23",
                        fontSize = 20.sp,
                        fontWeight = FontWeight.Bold,
                        modifier = Modifier.padding(top = 16.dp, start = 16.dp)
                    )

                    LinearProgressIndicator(
                        progress = {0.420f},
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 16.dp, vertical = 8.dp)
                    )

                    Text(
                        text = "420 / 1000 EXP",
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

            LazyColumn(modifier = Modifier
                .padding(16.dp)) {
                items(upcomingEvents) {
                    UpcomingCampaign(it.name, it.date)
                }
            }


        }
    }
}

@Composable
fun UpcomingCampaign(name: String, date: LocalDateTime) {
    val formatter = DateTimeFormatter.ofPattern("MMMM dd, yyyy hh:mm a")
    ElevatedCard(modifier = Modifier
        .fillMaxWidth()
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


//@Preview(showBackground = true)
@Composable
fun UpcomingCampaignPreview() {
    SustainaTheme {
        val date = LocalDateTime.of(2025, 3, 5, 13, 0)
        UpcomingCampaign("UP Hackathon Presentation", date)
    }
}

@Preview(showBackground = true)
@Composable
fun LandingPagePreview() {
    val events: List<Event> = listOf(Event("UP Hackathon", LocalDateTime.now()), Event("Midterms", LocalDateTime.now()))
    SustainaTheme {
        LandingPage(navController = rememberNavController(), events)
    }
}