package com.hackaton.sustaina.data.campaign

import android.util.Log
import com.google.firebase.database.DatabaseReference
import com.hackaton.sustaina.domain.models.Campaign
import javax.inject.Inject

class CampaignDataSource @Inject constructor(
    database: DatabaseReference
){
    private val TAG = "CampaignDataSource"
    private val campaignRef = database.child("campaign")

    fun addCampaign(campaign: Campaign, onComplete: (Boolean, String?) -> Unit) {
        campaignRef.child(campaign.campaignId).setValue(campaign)
            .addOnSuccessListener { onComplete(true, null) }
            .addOnFailureListener { e -> onComplete(false, e.message) }
    }

    fun getCampaign(campaignId: String, onComplete: (Campaign?) -> Unit) {
        campaignRef.child(campaignId).get()
            .addOnSuccessListener { snapshot ->
                val campaign = snapshot.getValue(Campaign::class.java)
                Log.d(TAG, "getCampaign for $campaignId success!")
                Log.d(TAG, campaign.toString())
                onComplete(campaign)
            }
            .addOnFailureListener { onComplete(null) }
    }
}