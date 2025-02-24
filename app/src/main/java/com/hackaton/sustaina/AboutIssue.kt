package com.hackaton.sustaina

import androidx.compose.foundation.Image
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
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.hackaton.sustaina.ui.theme.SustainaTheme

@Composable
fun AboutIssue(name: String, modifier: Modifier = Modifier) {
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

        Text(
            text = "Organizer",
            fontSize = 18.sp,
            fontWeight = FontWeight.Bold,
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 16.dp)
        )
    }
}

@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    SustainaTheme {
        AboutIssue("Android")
    }
}