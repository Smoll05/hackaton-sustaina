package com.hackaton.sustaina.data.hotspot

import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.ValueEventListener
import com.hackaton.sustaina.domain.models.Hotspot
import javax.inject.Inject

class HotspotDataSource @Inject constructor(
    database: DatabaseReference
) {
    private val hotspotRef = database.child("hotspots")

    fun addHotspot(hotspot: Hotspot, onComplete: (Boolean, String?) -> Unit) {
        hotspotRef.child(hotspot.hotspotId).setValue(hotspot)
            .addOnSuccessListener { onComplete(true, null) }
            .addOnFailureListener { e -> onComplete(false, e.message) }
    }

    fun getHotspot(hotspotId: String, onComplete: (Hotspot?) -> Unit) {
        hotspotRef.child(hotspotId).get()
            .addOnSuccessListener { snapshot ->
                val hotspot = snapshot.getValue(Hotspot::class.java)
                onComplete(hotspot)
            }
            .addOnFailureListener { onComplete(null) }
    }

    fun getHotspots(onComplete: (List<Hotspot>) -> Unit) {
        hotspotRef.get()
            .addOnSuccessListener { snapshot ->
                val hotspots = mutableListOf<Hotspot>()
                for (campaignSnapshot in snapshot.children) {
                    val hotspot = campaignSnapshot.getValue(Hotspot::class.java)
                    hotspot?.let { hotspots.add(hotspot) }
            }
            onComplete(hotspots)
        }
        .addOnFailureListener { onComplete(emptyList()) }
    }

    fun observeHotspots(onComplete: (List<Hotspot>) -> Unit) {
        hotspotRef.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                val hotspots = mutableListOf<Hotspot>()
                for (hotspotSnapshot in snapshot.children) {
                    val hotspot = hotspotSnapshot.getValue(Hotspot::class.java)
                    hotspot?.let { hotspots.add(hotspot) }
                }
                onComplete(hotspots)
            }
            override fun onCancelled(error: DatabaseError) {
                onComplete(emptyList())
            }
        })
    }
}