package com.hackaton.sustaina.ui.landing

import androidx.lifecycle.ViewModel
import com.hackaton.sustaina.data.repository.AuthRepository
import com.hackaton.sustaina.data.repository.CampaignRepository
import com.hackaton.sustaina.data.repository.UserRepository
import com.hackaton.sustaina.domain.models.Campaign
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import javax.inject.Inject

@HiltViewModel
class LandingPageViewModel @Inject constructor(
    userRepo: UserRepository,
    campaignRepo: CampaignRepository,
    auth: AuthRepository,

    ) : ViewModel() {
    val user = auth.getCurrentUser()

    private val _uiState: MutableStateFlow<LandingPageState> = MutableStateFlow<LandingPageState>(LandingPageState())
    val uiState = _uiState.asStateFlow()

    init {
        val user = auth.getCurrentUser()
        if (user != null) {
            val userData = userRepo.getUserFromId(user.uid)
            val campaigns: MutableList<Campaign> = mutableListOf()

            userData.userUpcomingCampaigns.forEach { campaigns.add(campaignRepo.getCampaignDetails(it)) }

            _uiState.value = LandingPageState(
                user = userData,
                progress = userData.userExp.toFloat() / 1000,
                upcomingCampaigns = campaigns.toList()
            )
        }
    }
}
