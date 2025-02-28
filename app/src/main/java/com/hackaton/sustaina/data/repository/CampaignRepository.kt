package com.hackaton.sustaina.data.repository

import com.hackaton.sustaina.data.datasource.CampaignDatabaseSource
import com.hackaton.sustaina.domain.models.Campaign

class CampaignRepository(
    private val databaseSource: CampaignDatabaseSource
) {
    fun getCampaignDetails(campaignId: String): Campaign {
        return databaseSource.getCampaignById(campaignId)
    }
}