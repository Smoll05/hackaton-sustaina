package com.hackaton.sustaina.data.datasource

import com.hackaton.sustaina.domain.models.Campaign
import java.time.LocalDateTime

class CampaignDatabaseSource {
    fun getCampaignById(campaignId: String): Campaign {
        // TODO: get details from Firebase
        when (campaignId) {
            "UP12345" -> {
                return Campaign(
                    campaignId = "UP12345",
                    campaignName = "Komsai Week Hackathon Presentation",
                    campaignOrganizer = "UP Computer Science Guild",
                    campaignStartDate = LocalDateTime.of(2025, 3, 5, 13, 0),
                    campaignAbout = "Present your project at the UP campus! GOODLUCKKKKK",
                    campaignVenue = "University of the Philippines - Cebu",
                    campaignAddress = "Gorordo Ave, Cebu City, 6000 Cebu"
                )
            }
            "MDTM12345" -> {
                return Campaign(
                    campaignId = "MDTM12345",
                    campaignName = "Midterm Examinations",
                    campaignOrganizer = "CIT-U",
                    campaignStartDate = LocalDateTime.of(2025, 1, 5, 7, 0),
                    campaignAbout = "2nd round of exams for this semester",
                    campaignVenue = "Cebu Institute of Technology - University",
                    campaignAddress = "N. Bacalso Ave, Cebu City, 6000 Cebu"
                )
            }
            else -> {
                return Campaign()
            }
        }
    }
}