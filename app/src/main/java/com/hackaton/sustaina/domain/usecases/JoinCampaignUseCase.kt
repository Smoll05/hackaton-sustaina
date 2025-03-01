package com.hackaton.sustaina.domain.usecases

import com.google.firebase.database.DatabaseReference
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class JoinCampaignUseCase @Inject constructor(
    val database: DatabaseReference
) {
    private val campaignsRef = database.child("campaigns").child("campaignAttendingUser")
    private val usersRef = database.child("users")

    fun joinCampaign(userId: String, campaignId: String) {
        val newUser = campaignsRef.push()
        newUser.setValue(userId)

        val newCampaign = usersRef.push()
        newCampaign.setValue(campaignId)
    }
}
