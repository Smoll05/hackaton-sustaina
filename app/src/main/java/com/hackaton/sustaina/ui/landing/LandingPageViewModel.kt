package com.hackaton.sustaina.ui.landing

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.hackaton.sustaina.data.repository.CampaignRepository
import com.hackaton.sustaina.ui.aboutissue.CampaignState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class LandingPageViewModel @Inject constructor(
    private val repository: CampaignRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(LandingPageState())
    val uiState = _uiState.asStateFlow()

    fun loadUpcomingCampaigns(campaignIds: List<String>) {
        viewModelScope.launch {
            val campaigns = mutableListOf<CampaignState>()
            campaignIds.forEach{ campaigns.add(repository.getCampaignDetails(it)) }
            _uiState.value = _uiState.value.copy(upcomingCampaigns = campaigns)
        }
    }
}
