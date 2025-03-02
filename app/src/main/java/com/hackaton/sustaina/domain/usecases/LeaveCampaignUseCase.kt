package com.hackaton.sustaina.domain.usecases

import com.hackaton.sustaina.data.campaign.CampaignRepository
import com.hackaton.sustaina.data.user.UserRepository
import javax.inject.Inject

class LeaveCampaignUseCase @Inject constructor(
    private val userRepo: UserRepository,
    private val campaignRepo: CampaignRepository
) {
    fun leaveCampaign(userId: String, campaignId: String) {
        campaignRepo.removeUserFromCampaign(campaignId, userId)
        userRepo.removeCampaignFromUser(userId, campaignId)
    }
}