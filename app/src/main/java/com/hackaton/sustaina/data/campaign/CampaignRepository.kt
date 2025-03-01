package com.hackaton.sustaina.data.campaign

import com.hackaton.sustaina.domain.models.Campaign

class CampaignRepository(
    private val databaseSource: CampaignDataSource
) {
    fun createCampaign(campaign: Campaign, onComplete: (Boolean, String?) -> Unit) {
        databaseSource.addCampaign(campaign, onComplete)
    }

    fun fetchCampaign(campaignId: String, onComplete: (Campaign?) -> Unit) {
        databaseSource.getCampaign(campaignId, onComplete)
    }

    fun fetchCampaigns(onComplete: (List<Campaign>) -> Unit) {
        databaseSource.getCampaigns(onComplete)
    }

    fun observeCampaigns(onComplete: (List<Campaign>) -> Unit) {
        databaseSource.observeCampaigns(onComplete)
    }
}