package com.hackaton.sustaina.data.user

import com.hackaton.sustaina.domain.models.User

class UserRepository(
    private val databaseSource: UserDataSource
) {
    fun registerUser(user: User, onComplete: (Boolean, String?) -> Unit) {
        databaseSource.addUser(user, onComplete)
    }

    fun fetchUser(userId: String, onComplete: (User?) -> Unit) {
        databaseSource.getUser(userId, onComplete)
    }
}