package com.hackaton.sustaina.ui.campaigninfo

import com.google.android.gms.maps.model.LatLng
import com.hackaton.sustaina.domain.models.Campaign

data class CampaignInfoState(
    val campaign: Campaign = Campaign(),
    val isUserAttending: Boolean = false,
    val loading: Boolean = true,
    val showJoinCampaignSheet: Boolean = false,
    val showOfferSolutionSheet: Boolean = false
    ) {
    val campaignName: String = campaign.campaignName
    val campaignStartDate: Long = campaign.campaignStartDate
    val campaignAddress: String = campaign.campaignAddress
    val campaignVenue: String = campaign.campaignVenue
    val campaignOrganizer: String = campaign.campaignOrganizer
    val campaignAbout: String = campaign.campaignAbout
    val campaignLatLng: LatLng = LatLng(campaign.latitude, campaign.longitude)
}