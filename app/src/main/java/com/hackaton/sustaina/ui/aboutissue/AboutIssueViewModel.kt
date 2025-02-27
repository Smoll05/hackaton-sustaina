package com.hackaton.sustaina.ui.aboutissue

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import com.hackaton.sustaina.data.repository.CampaignRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import javax.inject.Inject

@HiltViewModel
class AboutIssueViewModel @Inject constructor (
    private val repository: CampaignRepository,
    savedStateHandle: SavedStateHandle
) : ViewModel() {

    val campaignId: String = savedStateHandle.get<String>("campaignId") ?: ""

    private val _uiState = MutableStateFlow(repository.getCampaignDetails(campaignId))
    val uiState = _uiState.asStateFlow()
}