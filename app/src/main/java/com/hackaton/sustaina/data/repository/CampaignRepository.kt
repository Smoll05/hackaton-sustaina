package com.hackaton.sustaina.data.repository

import com.hackaton.sustaina.data.datasource.CampaignDatabaseSource
import com.hackaton.sustaina.ui.aboutissue.CampaignState

class CampaignRepository(
    private val databaseSource: CampaignDatabaseSource
) {
    fun getCampaignDetails(campaignId: String): CampaignState {
        return databaseSource.getCampaignById(campaignId)
    }
}