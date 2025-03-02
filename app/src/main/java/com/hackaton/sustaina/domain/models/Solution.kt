package com.hackaton.sustaina.domain.models

import java.util.UUID

data class Solution(
    val solutionId: String = UUID.randomUUID().toString(),
    val userId: String = "",
    val campaignId: String = "",
    val submission: String = ""
)
