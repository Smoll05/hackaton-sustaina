package com.hackaton.sustaina

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
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.hackaton.sustaina.ui.theme.SustainaTheme
import java.security.AccessController.getContext

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AboutIssue(name: String, modifier: Modifier) {
    val sheetState = rememberModalBottomSheetState()
    val scope = rememberCoroutineScope()
    var showBottomSheet by remember { mutableStateOf(false) }

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
            text = "Campaign Name",
            fontSize = 28.sp,
            fontWeight = FontWeight.Bold,
            modifier = Modifier
                .padding(top = 16.dp)
        )

        Text(
            text = "Campaign Organizer",
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
                        modifier = Modifier.size(32.dp)
                    )
                    Text(
                        text = "Mar 1",
                        fontSize = 18.sp,
                        modifier = Modifier
                            .padding(start = 8.dp)
                            .align(Alignment.CenterVertically)
                    )
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
                        text = "UP Cebu",
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
            text = stringResource(R.string.placeholder)
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
            text = "University of the Philippines - Cebu",
            fontSize = 28.sp,
            fontWeight = FontWeight.ExtraBold,
            modifier = Modifier.padding(top = 4.dp)
        )
        Text(
            text = stringResource(R.string.address),
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
            text = "UP Cebu - Computer Science Guild",
            fontWeight = FontWeight.SemiBold,
            fontSize = 20.sp,
            modifier = Modifier.padding(top = 8.dp)
        )

        Button(
            onClick = { showBottomSheet = true },
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
            onClick = {
                Toast.makeText(context, "To be implemented!", Toast.LENGTH_LONG).show()
            },
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 4.dp)
        ) {
            Text(
                text = "OFFER SOLUTION",
                fontWeight = FontWeight.Bold
            )
        }

        if (showBottomSheet) {
            ModalBottomSheet(
                onDismissRequest = { showBottomSheet = false },
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
                    text = "Are you sure you want to join " + "Campaign" + "?",
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
                            showBottomSheet = false
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
                        onClick = { showBottomSheet = false },
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
    }
}

@Preview(showBackground = true)
@Composable
fun AboutIssuePreview() {
    SustainaTheme {
        AboutIssue("Android", modifier = Modifier)
    }
}

