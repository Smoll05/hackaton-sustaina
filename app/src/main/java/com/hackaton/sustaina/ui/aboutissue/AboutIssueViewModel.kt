package com.hackaton.sustaina.ui.aboutissue

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import com.hackaton.sustaina.data.repository.CampaignRepository
import com.hackaton.sustaina.domain.models.Campaign
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import javax.inject.Inject

@HiltViewModel
class AboutIssueViewModel @Inject constructor (
    repository: CampaignRepository,
    savedStateHandle: SavedStateHandle
) : ViewModel() {

    private val campaignId: String = savedStateHandle.get<String>("campaignId") ?: ""

    private val _uiState = MutableStateFlow(AboutIssueState())
    val uiState = _uiState.asStateFlow()

    init {
        _uiState.value = AboutIssueState(repository.getCampaignDetails(campaignId))
    }
}