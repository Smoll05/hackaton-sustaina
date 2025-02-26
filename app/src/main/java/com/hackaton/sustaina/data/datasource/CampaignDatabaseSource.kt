package com.hackaton.sustaina.data.datasource

import com.hackaton.sustaina.ui.aboutissue.CampaignState
import java.time.LocalDateTime

class CampaignDatabaseSource {
    fun getCampaignById(campaignId: String): CampaignState {
        // TODO: get details from Firebase
        return CampaignState(
            campaignName = "Komsai Week Hackathon Presentation",
            campaignOrganizer = "UP Computer Science Guild",
            campaignStartDate = LocalDateTime.of(2025, 3, 5, 13, 0),
            campaignAbout = "Present your project at the UP campus! GOODLUCKKKKK",
            campaignVenue = "University of the Philippines - Cebu",
            campaignLocation = "Gorordo Ave, Cebu City, 6000 Cebu"
        )
    }
}