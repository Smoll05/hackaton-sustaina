package com.hackaton.sustaina.data.solution

import com.hackaton.sustaina.domain.models.Solution

class SolutionRepository(
    private val dataSource: SolutionDataSource
) {
    fun addSolution(solution: Solution, onComplete: (Boolean, String?) -> Unit) {
        dataSource.addSolution(solution, onComplete)
    }
}