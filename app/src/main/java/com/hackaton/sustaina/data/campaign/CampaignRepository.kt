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

    fun addUserToCampaign(campaignId: String, userId: String) {
        databaseSource.getCampaign(campaignId) { campaign ->
            if (campaign != null) {
                val usersList = campaign.campaignAttendingUser.toMutableList()
                usersList.add(userId)
                databaseSource.updateCampaignUsers(campaignId, usersList)
            }
        }
    }

    fun removeUserFromCampaign(campaignId: String, userId: String) {
        databaseSource.getCampaign(campaignId) { campaign ->
            if (campaign != null) {
                val usersList = campaign.campaignAttendingUser.toMutableList()
                usersList.remove(userId)
                databaseSource.updateCampaignUsers(campaignId, usersList)
            }
        }
    }
}