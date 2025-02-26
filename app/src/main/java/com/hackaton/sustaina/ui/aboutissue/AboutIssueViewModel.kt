package com.hackaton.sustaina.ui.aboutissue

import androidx.lifecycle.ViewModel
import com.hackaton.sustaina.data.repository.CampaignRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import javax.inject.Inject

@HiltViewModel
class AboutIssueViewModel @Inject constructor (
    private val repository: CampaignRepository
) : ViewModel() {
    // TODO: get campaign id (somehow)
    private val _uiState = MutableStateFlow(repository.getCampaignDetails("UP12345"))
    val uiState = _uiState.asStateFlow()
}