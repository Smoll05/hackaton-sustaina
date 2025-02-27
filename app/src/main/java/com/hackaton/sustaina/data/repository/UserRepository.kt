package com.hackaton.sustaina.data.repository

import com.hackaton.sustaina.data.datasource.UserDatabaseSource
import com.hackaton.sustaina.domain.models.User

class UserRepository(
    private val databaseSource: UserDatabaseSource
) {
    fun getUserFromId(userId: String): User {
        return databaseSource.getUserFromId(userId)
    }
}