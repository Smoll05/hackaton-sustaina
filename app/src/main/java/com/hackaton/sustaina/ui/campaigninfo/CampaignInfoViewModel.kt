package com.hackaton.sustaina.ui.campaigninfo

import android.util.Log
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import com.hackaton.sustaina.data.campaign.CampaignRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import javax.inject.Inject

@HiltViewModel
class CampaignInfoViewModel @Inject constructor (
    repository: CampaignRepository,
    savedStateHandle: SavedStateHandle
) : ViewModel() {

    private val TAG = "AboutIssueViewModel"
    private val campaignId: String = savedStateHandle.get<String>("campaignId") ?: ""

    private val _uiState = MutableStateFlow(CampaignInfoState())
    val uiState = _uiState.asStateFlow()

    init {
        Log.d(TAG, "gonna get details for $campaignId")
        repository.fetchCampaign(campaignId) { campaign ->
            _uiState.value = campaign?.let { CampaignInfoState(it) }!!
            _uiState.update { it.copy(loading = false) }
        }
    }

    fun showJoinCampaignSheet() {
        _uiState.update { it.copy(showJoinCampaignSheet = true) }
    }

    fun hideJoinCampaignSheet() {
        _uiState.update { it.copy(showJoinCampaignSheet = false) }
    }

    fun showOfferSolutionSheet() {
        _uiState.update { it.copy(showOfferSolutionSheet = true) }
    }

    fun hideOfferSolutionSheet() {
        _uiState.update { it.copy(showOfferSolutionSheet = false) }
    }
}