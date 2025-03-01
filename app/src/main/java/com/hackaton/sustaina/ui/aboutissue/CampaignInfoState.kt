package com.hackaton.sustaina.ui.aboutissue

import com.hackaton.sustaina.domain.models.Campaign

data class CampaignInfoState(
    val campaign: Campaign = Campaign(),
    val loading: Boolean = true
    ) {
    val campaignName: String = campaign.campaignName
    val campaignStartDate: Long = campaign.campaignStartDate
    val campaignAddress: String = campaign.campaignAddress
    val campaignVenue: String = campaign.campaignVenue
    val campaignOrganizer: String = campaign.campaignOrganizer
    val campaignAbout: String = campaign.campaignAbout
}