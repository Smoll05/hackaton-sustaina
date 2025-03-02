package com.hackaton.sustaina.domain.usecases

import com.hackaton.sustaina.data.campaign.CampaignRepository
import com.hackaton.sustaina.data.user.UserRepository
import javax.inject.Inject

class JoinCampaignUseCase @Inject constructor(
    private val campaignRepo: CampaignRepository,
    private val userRepo: UserRepository
) {
    fun joinCampaign(userId: String, campaignId: String) {
        campaignRepo.addUserToCampaign(campaignId, userId)
        userRepo.addCampaignToUser(userId, campaignId)
    }
}