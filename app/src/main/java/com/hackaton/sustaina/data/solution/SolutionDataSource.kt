package com.hackaton.sustaina.data.solution

import com.google.firebase.database.DatabaseReference
import com.hackaton.sustaina.domain.models.Solution
import javax.inject.Inject

class SolutionDataSource @Inject constructor(
    database: DatabaseReference
) {
    private val solutionRef = database.child("solutions")

    fun addSolution(solution: Solution, onComplete: (Boolean, String?) -> Unit) {
        solutionRef.child(solution.solutionId).setValue(solution)
            .addOnSuccessListener { onComplete(true, null) }
            .addOnFailureListener { e -> onComplete(false, e.message) }
    }
}