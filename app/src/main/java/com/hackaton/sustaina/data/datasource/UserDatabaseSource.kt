package com.hackaton.sustaina.data.datasource

import com.hackaton.sustaina.domain.models.User

class UserDatabaseSource {
    fun getUserFromId(userId: String): User {
        // TODO: get user details from Firebase
        when (userId) {
            "testUser" -> {
                return User(
                    userId = "testUser",
                    userName = Pair("John", "Doe"),
                    userEmail = "john.doe@cit.edu",
                    userUpcomingCampaigns = listOf("UP12345", "MDTM12345"),
                    userLevel = 23,
                    userExp = 420
                )
            }
        }
        return User()
    }
}