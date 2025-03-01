package com.hackaton.sustaina.ui.landing

import android.util.Log
import androidx.lifecycle.ViewModel
import com.hackaton.sustaina.data.auth.AuthRepository
import com.hackaton.sustaina.data.campaign.CampaignRepository
import com.hackaton.sustaina.data.user.UserRepository
import com.hackaton.sustaina.domain.models.Campaign
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import javax.inject.Inject

@HiltViewModel
class LandingPageViewModel @Inject constructor(
    userRepo: UserRepository,
    campaignRepo: CampaignRepository,
    auth: AuthRepository,
) : ViewModel() {
    val user = auth.getCurrentUser()

    private val _uiState: MutableStateFlow<LandingPageState> = MutableStateFlow(LandingPageState())
    val uiState = _uiState.asStateFlow()

    init {
        user?.let { Log.d("USER ID", it.uid) }
        if (user != null) {
            userRepo.fetchUser(user.uid) { userData ->
                userData?.userUpcomingCampaigns?.let { campaignIds ->
                    val campaigns = mutableListOf<Campaign>()
                    var remaining = campaignIds.size

                    campaignIds.forEach { campaignId ->
                        campaignRepo.fetchCampaign(campaignId) { campaign ->
                            campaign?.let { campaigns.add(it) }
                            remaining--
                            if (remaining == 0) {
                                _uiState.value = LandingPageState(
                                    user = userData,
                                    progress = userData.userExp.toFloat() / 1000,
                                    upcomingCampaigns = campaigns
                                )
                                _uiState.update { it.copy(loading = false) }
                            }
                        }
                    }
                }
            }
        }
    }
}