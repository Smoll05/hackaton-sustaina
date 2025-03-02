package com.hackaton.sustaina.ui.campaigninfo

import android.util.Log
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.hackaton.sustaina.data.auth.AuthRepository
import com.hackaton.sustaina.data.campaign.CampaignRepository
import com.hackaton.sustaina.data.solution.SolutionRepository
import com.hackaton.sustaina.domain.models.Solution
import com.hackaton.sustaina.domain.usecases.JoinCampaignUseCase
import com.hackaton.sustaina.domain.usecases.LeaveCampaignUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class CampaignInfoViewModel @Inject constructor (
    private val campaignRepo: CampaignRepository,
    private val solutionRepo: SolutionRepository,
    private val joinCampaignUseCase: JoinCampaignUseCase,
    private val leaveCampaignUseCase: LeaveCampaignUseCase,
    val auth: AuthRepository,
    savedStateHandle: SavedStateHandle
) : ViewModel() {

    private val TAG = "AboutIssueViewModel"
    private val campaignId: String = savedStateHandle.get<String>("campaignId") ?: ""

    private val _uiState = MutableStateFlow(CampaignInfoState())
    val uiState = _uiState.asStateFlow()

    init {
        viewModelScope.launch {
            val user = auth.getCurrentUser()

            Log.d(TAG, "gonna get details for $campaignId")
            val startTime = System.currentTimeMillis()

            campaignRepo.fetchCampaign(campaignId) { campaign ->
                _uiState.value = campaign?.let { CampaignInfoState(it) }!!

                if (_uiState.value.campaign.campaignAttendingUser.any { it == user?.uid }) {
                    _uiState.update { it.copy(isUserAttending = true) }
                }
            }

            val elapsedTime = System.currentTimeMillis() - startTime
            val remainingTime = 500L - elapsedTime

            if (remainingTime > 0) {
                delay(remainingTime)
            }
            _uiState.update { it.copy(loading = false) }
        }
    }

    fun joinCampaign() {
        val user = auth.getCurrentUser()
        if (user != null) {
            joinCampaignUseCase.joinCampaign(user.uid, campaignId)
            _uiState.update { it.copy(isUserAttending = true) }
        }
    }

    fun leaveCampaign() {
        val user = auth.getCurrentUser()
        if (user != null) {
            leaveCampaignUseCase.leaveCampaign(user.uid, campaignId)
            _uiState.update { it.copy(isUserAttending = false) }
        }
    }

    fun submitSolution(submission: String, onComplete: (Boolean, String?) -> Unit) {
        val user = auth.getCurrentUser()
        if (user != null) {
            val solution =
                Solution(userId = user.uid, campaignId = campaignId, submission = submission)
            solutionRepo.addSolution(solution, onComplete)
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