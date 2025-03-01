package com.hackaton.sustaina.ui.campaigninfo

import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.displayCutoutPadding
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.SheetState
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
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.hackaton.sustaina.R
import com.hackaton.sustaina.domain.models.toLocalDateTime
import com.hackaton.sustaina.ui.loadingscreen.LoadingScreen
import java.time.format.DateTimeFormatter

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CampaignInfoScreen(navController: NavController, viewModel: CampaignInfoViewModel = hiltViewModel()) {
    val uiState by viewModel.uiState.collectAsState()

    val sheetState = rememberModalBottomSheetState()
    val context = LocalContext.current

    if (uiState.loading) {
        LoadingScreen()
        return
    }

    Column(
        modifier = Modifier
            .padding(all = 20.dp)
            .verticalScroll(rememberScrollState())
            .fillMaxWidth()
            .displayCutoutPadding()
    ) {
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
            modifier = Modifier.padding(top = 16.dp)
        )

        Text(
            text = uiState.campaignOrganizer,
            fontSize = 18.sp,
            modifier = Modifier.padding(top = 8.dp)
        )

        CampaignDateLocation(uiState)

        HorizontalDivider(
            thickness = 2.dp,
            color = colorResource(R.color.off_grey),
            modifier = Modifier.padding(vertical = 16.dp)
        )

        Text("About Event", fontSize = 18.sp, fontWeight = FontWeight.Bold)
        Text(text = uiState.campaignAbout)

        HorizontalDivider(
            thickness = 2.dp,
            color = colorResource(R.color.off_grey),
            modifier = Modifier.padding(vertical = 16.dp)
        )

        Text("Location", fontSize = 18.sp, fontWeight = FontWeight.Bold)
        Text(uiState.campaignVenue, fontSize = 28.sp, fontWeight = FontWeight.ExtraBold)
        Text(uiState.campaignAddress, fontWeight = FontWeight.SemiBold, fontSize = 16.sp)

        Image(
            painter = painterResource(id = R.drawable.placeholder),
            contentDescription = "Campaign Map",
            modifier = Modifier
                .fillMaxWidth()
                .height(300.dp)
                .clip(RoundedCornerShape(16.dp))
                .padding(vertical = 16.dp)
        )

        HorizontalDivider(
            thickness = 2.dp,
            color = colorResource(R.color.off_grey),
            modifier = Modifier.padding(vertical = 16.dp)
        )

        Text("Organizer", fontSize = 18.sp, fontWeight = FontWeight.Bold)
        Text(uiState.campaignOrganizer, fontWeight = FontWeight.SemiBold, fontSize = 20.sp, modifier = Modifier.padding(top = 8.dp))

        // Buttons
        Button(
            onClick = { viewModel.showJoinCampaignSheet() },
            modifier = Modifier.fillMaxWidth().padding(top = 16.dp)
        ) {
            Text("JOIN CAMPAIGN", fontWeight = FontWeight.Bold)
        }

        OutlinedButton(
            onClick = { viewModel.showOfferSolutionSheet() },
            modifier = Modifier.fillMaxWidth().padding(top = 4.dp)
        ) {
            Text("OFFER SOLUTION", fontWeight = FontWeight.Bold)
        }
    }

    if (uiState.showJoinCampaignSheet) {
        JoinCampaignSheet(
            sheetState = sheetState,
            campaignName = uiState.campaignName,
            onDismiss = { viewModel.hideJoinCampaignSheet() },
            onConfirm = {
                Toast.makeText(context, "You have joined this campaign!", Toast.LENGTH_LONG).show()
                viewModel.joinCampaign()
                viewModel.hideJoinCampaignSheet()
            }
        )
    }

    if (uiState.showOfferSolutionSheet) {
        OfferSolutionSheet(
            sheetState = sheetState,
            onDismiss = { viewModel.hideOfferSolutionSheet() },
            onSubmit = {
                viewModel.submitSolution(it) { success, message ->
                    if (success) {
                        Toast.makeText(context, "Your solution has been offered!", Toast.LENGTH_LONG).show()
                        viewModel.hideOfferSolutionSheet()
                    } else {
                        Toast.makeText(context, "An error occurred: $message", Toast.LENGTH_LONG).show()
                    }
                }
            }
        )
    }
}

@Composable
fun CampaignDateLocation(uiState: CampaignInfoState) {
    Row(horizontalArrangement = Arrangement.SpaceBetween,
        modifier = Modifier
            .padding(top = 16.dp)
            .fillMaxWidth()) {

        Spacer(modifier = Modifier.weight(1f))

        Row(horizontalArrangement = Arrangement.Center,
            modifier = Modifier
                .align(Alignment.CenterVertically)
                .padding(8.dp)) {
            Row(horizontalArrangement = Arrangement.Center) {
                Image(
                    painter = painterResource(R.drawable.calendar),
                    contentDescription = "",
                    alignment = Alignment.Center,
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
            }
        }

        Spacer(modifier = Modifier.weight(1f))

        Row(modifier = Modifier
            .align(Alignment.CenterVertically)
            .padding(8.dp)) {
            Row(horizontalArrangement = Arrangement.Center) {
                Image(
                    painter = painterResource(R.drawable.location),
                    contentDescription = "",
                    alignment = Alignment.Center,
                    modifier = Modifier
                        .size(32.dp)
                        .align(Alignment.CenterVertically)

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
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun JoinCampaignSheet(sheetState: SheetState, campaignName: String, onDismiss: () -> Unit, onConfirm: () -> Unit) {
    ModalBottomSheet(
        onDismissRequest = onDismiss,
        sheetState = sheetState,
        modifier = Modifier.fillMaxWidth()
    ) {
        Column(modifier = Modifier.padding(16.dp), horizontalAlignment = Alignment.CenterHorizontally) {
            Text("Join Campaign", fontSize = 22.sp, fontWeight = FontWeight.Bold)

            Text(
                text = "Are you sure you want to join $campaignName?",
                modifier = Modifier.padding(top = 8.dp)
            )

            Row(
                horizontalArrangement = Arrangement.SpaceEvenly,
                modifier = Modifier.padding(top = 8.dp)
            ) {
                Button(
                    onClick = onConfirm,
                    modifier = Modifier
                        .padding(vertical = 16.dp, horizontal = 4.dp)
                        .weight(1.0f)
                ) {
                    Text("Yes", fontWeight = FontWeight.Bold)
                }

                OutlinedButton(
                    onClick = onDismiss,
                    modifier = Modifier
                        .padding(vertical = 16.dp, horizontal = 4.dp)
                        .weight(1.0f)
                ) {
                    Text("No", fontWeight = FontWeight.Bold)
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun OfferSolutionSheet(sheetState: SheetState, onDismiss: () -> Unit, onSubmit: (String) -> Unit) {
    var solutionText by remember { mutableStateOf("") }

    ModalBottomSheet(
        onDismissRequest = onDismiss,
        sheetState = sheetState,
        modifier = Modifier.fillMaxWidth()
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text("Offer Solution", fontSize = 22.sp, fontWeight = FontWeight.Bold, modifier = Modifier.align(Alignment.CenterHorizontally))

            Text(
                text = "Suggest a solution to this issue by filling up this form",
                textAlign = TextAlign.Center,
                fontSize = 14.sp,
                modifier = Modifier.padding(top = 8.dp)
            )

            Text("Brief description of the proposed solution", modifier = Modifier.padding(top = 8.dp))
            TextField(
                value = solutionText,
                onValueChange = { solutionText = it },
                minLines = 3,
                modifier = Modifier.fillMaxWidth().padding(16.dp),
                singleLine = false
            )

            Row(horizontalArrangement = Arrangement.SpaceEvenly, modifier = Modifier.padding(top = 8.dp)) {
                Button(
                    onClick = {
                        onSubmit(solutionText)
                    },
                    modifier = Modifier.padding(vertical = 16.dp, horizontal = 4.dp).weight(1.0f)
                ) {
                    Text("SUBMIT SOLUTION", fontWeight = FontWeight.Bold)
                }

                OutlinedButton(
                    onClick = onDismiss,
                    modifier = Modifier.padding(vertical = 16.dp, horizontal = 4.dp).weight(1.0f)
                ) {
                    Text("CANCEL", fontWeight = FontWeight.Bold)
                }
            }
        }
    }
}

