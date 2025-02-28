package com.hackaton.sustaina.ui.aboutissue

import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.displayCutoutPadding
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.hackaton.sustaina.R
import com.hackaton.sustaina.domain.models.toLocalDateTime
import com.hackaton.sustaina.ui.theme.SustainaTheme
import java.time.format.DateTimeFormatter

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AboutIssue(navController: NavController, viewModel: AboutIssueViewModel = hiltViewModel()) {
    val uiState by viewModel.uiState.collectAsState()

    val sheetState = rememberModalBottomSheetState()
    var showJoinCampaignSheet by remember { mutableStateOf(false) }
    var showOfferSolutionSheet by remember { mutableStateOf(false) }

    // TODO: this is user-based
    var solutionText by remember { mutableStateOf("") }

    val context = LocalContext.current

    Column(modifier = Modifier
        .padding(all = 20.dp)
        .verticalScroll(rememberScrollState())
        .fillMaxWidth()
        .displayCutoutPadding()) {
        Image(
            painter = painterResource(id = R.drawable.placeholder),
            contentDescription = "Campaign Banner",
            modifier = Modifier
                .size(width = 300.dp, height = 300.dp)
                .align(Alignment.CenterHorizontally)
                .clip(RoundedCornerShape(16.dp))
        )

        Text(
            text = uiState.campaignName,
            fontSize = 28.sp,
            fontWeight = FontWeight.Bold,
            modifier = Modifier
                .padding(top = 16.dp)
        )

        Text(
            text = uiState.campaignOrganizer,
            fontSize = 18.sp,
            modifier = Modifier.padding(top = 8.dp)
        )

        Row(modifier = Modifier
            .padding(top = 16.dp)
            .fillMaxWidth()) {

            Spacer(modifier = Modifier.weight(1f))

            Row(modifier = Modifier
                .align(Alignment.CenterVertically)
                .padding(8.dp)) {
                Row {
                    Image(
                        painter = painterResource(R.drawable.calendar),
                        contentDescription = "",
                        modifier = Modifier
                            .size(32.dp)
                            .align(Alignment.CenterVertically)
                    )
                    uiState.campaignStartDate.toLocalDateTime().format(DateTimeFormatter.ofPattern("MMM dd"))?.let {
                        Text(
                            text = it,
                            fontSize = 18.sp,
                            modifier = Modifier
                                .padding(start = 8.dp)
                                .align(Alignment.CenterVertically)
                        )
                    }
//                    Text(
//                        text = uiState.campaignStartDate,
//                        fontSize = 18.sp,
//                        modifier = Modifier
//                            .padding(start = 8.dp)
//                            .align(Alignment.CenterVertically)
//                    )
                }
            }

            Spacer(modifier = Modifier.weight(1f))

            Row(modifier = Modifier
                .align(Alignment.CenterVertically)
                .padding(8.dp)) {
                Row {
                    Image(
                        painter = painterResource(R.drawable.location),
                        contentDescription = "",
                        modifier = Modifier.size(32.dp)
                    )
                    Text(
                        text = uiState.campaignVenue,
                        fontSize = 18.sp,
                        modifier = Modifier
                            .padding(start = 8.dp)
                            .align(Alignment.CenterVertically)
                    )
                }
            }

            Spacer(modifier = Modifier.weight(1f))
        }

        HorizontalDivider(
            thickness = 2.dp,
            color = colorResource(R.color.off_grey),
            modifier = Modifier.padding(vertical = 16.dp))

        Text(
            text = "About Event",
            fontSize = 18.sp,
            fontWeight = FontWeight.Bold,
            modifier = Modifier
                .fillMaxWidth()
        )
        Text(
            text = uiState.campaignAbout
        )

        HorizontalDivider(
            thickness = 2.dp,
            color = colorResource(R.color.off_grey),
            modifier = Modifier.padding(vertical = 16.dp))

        Text(
            text = "Location",
            fontSize = 18.sp,
            fontWeight = FontWeight.Bold,
            modifier = Modifier
                .fillMaxWidth()
        )
        Text(
            text = uiState.campaignVenue,
            fontSize = 28.sp,
            fontWeight = FontWeight.ExtraBold,
            modifier = Modifier.padding(top = 4.dp)
        )
        Text(
            text = uiState.campaignAddress,
            fontWeight = FontWeight.SemiBold,
            fontSize = 16.sp
        )

        // TODO: Map will be here
        Image(
            painter = painterResource(id = R.drawable.placeholder),
            contentDescription = "Campaign Banner",
            modifier = Modifier
                .fillMaxWidth()
                .height(300.dp)
                .align(Alignment.CenterHorizontally)
                .clip(RoundedCornerShape(16.dp))
                .padding(vertical = 16.dp)
        )

        HorizontalDivider(
            thickness = 2.dp,
            color = colorResource(R.color.off_grey),
            modifier = Modifier.padding(vertical = 16.dp))

        Text(
            text = "Organizer",
            fontSize = 18.sp,
            fontWeight = FontWeight.Bold,
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 4.dp)
        )
        Text(
            text = uiState.campaignOrganizer,
            fontWeight = FontWeight.SemiBold,
            fontSize = 20.sp,
            modifier = Modifier.padding(top = 8.dp)
        )

        // TODO: change state depending on user status
        Button(
            onClick = { showJoinCampaignSheet = true },
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 16.dp)
        ) {
            Text(
                text = "JOIN CAMPAIGN",
                fontWeight = FontWeight.Bold
            )
        }

        OutlinedButton(
            onClick = { showOfferSolutionSheet = true },
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 4.dp)
        ) {
            Text(
                text = "OFFER SOLUTION",
                fontWeight = FontWeight.Bold
            )
        }

        if (showJoinCampaignSheet) {
            ModalBottomSheet(
                onDismissRequest = { showJoinCampaignSheet = false },
                sheetState = sheetState,
                modifier = Modifier
                    .fillMaxWidth()
            ) {
                Text(
                    text = "Join Campaign",
                    fontSize = 22.sp,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.align(Alignment.CenterHorizontally)
                )

                Text(
                    text = "Are you sure you want to join " + uiState.campaignName + "?",
                    modifier = Modifier
                        .align(Alignment.CenterHorizontally)
                        .padding(top = 8.dp)
                )

                Row (
                    horizontalArrangement = Arrangement.SpaceEvenly,
                    modifier = Modifier
                        .align(Alignment.CenterHorizontally)
                        .padding(top = 8.dp)
                ) {
                    Button(
                        onClick = {
                            showJoinCampaignSheet = false
                            Toast.makeText(context, "You have joined this campaign!", Toast.LENGTH_LONG).show()
                        },
                        modifier = Modifier
                            .padding(vertical = 16.dp, horizontal = 4.dp)
                            .weight(1.0f)
                    ) {
                        Text(
                            text = "Yes",
                            fontWeight = FontWeight.Bold
                        )
                    }

                    OutlinedButton(
                        onClick = { showJoinCampaignSheet = false },
                        modifier = Modifier
                            .padding(vertical = 16.dp, horizontal = 4.dp)
                            .weight(1.0f)
                    ) {
                        Text(
                            text = "No",
                            fontWeight = FontWeight.Bold
                        )
                    }
                }

                Spacer(modifier = Modifier.height(8.dp))
            }
        }

        if (showOfferSolutionSheet) {
            ModalBottomSheet(
                onDismissRequest = { showOfferSolutionSheet = false },
                sheetState = sheetState,
                modifier = Modifier
                    .fillMaxWidth()
            ) {
                Box(modifier = Modifier
                        .padding(horizontal = 16.dp)
                        .align(Alignment.CenterHorizontally)) {
                    Column {
                        Text(
                            text = "Offer Solution",
                            fontSize = 22.sp,
                            fontWeight = FontWeight.Bold,
                            modifier = Modifier.align(Alignment.CenterHorizontally)
                        )

                        Text(
                            text = "Suggest a solution to this issue by filling up this form",
                            textAlign = TextAlign.Center,
                            fontSize = 14.sp,
                            modifier = Modifier
                                .padding(top = 8.dp)
                        )

                        Text(
                            text = "Brief description of the proposed solution",
                            modifier = Modifier.padding(top = 8.dp)
                        )
                        TextField(
                            value = solutionText,
                            onValueChange = { solutionText = it },
                            minLines = 3,
                            modifier = Modifier.fillMaxWidth().wrapContentHeight().padding(16.dp),
                            singleLine = false
                        )

                        Row(
                            horizontalArrangement = Arrangement.SpaceEvenly,
                            modifier = Modifier
                                .padding(top = 8.dp)
                        ) {
                            Button(
                                onClick = {
                                    showOfferSolutionSheet = false
                                    Toast.makeText(
                                        context,
                                        "Your solution has been offered!",
                                        Toast.LENGTH_LONG
                                    ).show()
                                },
                                modifier = Modifier
                                    .padding(vertical = 16.dp, horizontal = 4.dp)
                                    .weight(1.0f)
                            ) {
                                Text(
                                    text = "SUBMIT SOLUTION",
                                    fontWeight = FontWeight.Bold
                                )
                            }

                            OutlinedButton(
                                onClick = { showOfferSolutionSheet = false },
                                modifier = Modifier
                                    .padding(vertical = 16.dp, horizontal = 4.dp)
                                    .weight(1.0f)
                            ) {
                                Text(
                                    text = "CANCEL",
                                    fontWeight = FontWeight.Bold
                                )
                            }
                        }

                        Spacer(modifier = Modifier.height(8.dp))
                    }
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun AboutIssuePreview() {
    SustainaTheme {
        AboutIssue(rememberNavController())
    }
}

