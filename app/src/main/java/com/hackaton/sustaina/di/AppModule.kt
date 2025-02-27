package com.hackaton.sustaina.di

import com.google.firebase.auth.FirebaseAuth
import com.hackaton.sustaina.data.AUTH_PORT
import com.hackaton.sustaina.data.LOCAL_HOST
import com.hackaton.sustaina.data.datasource.CampaignDatabaseSource
import com.hackaton.sustaina.data.datasource.FirebaseAuthSource
import com.hackaton.sustaina.data.repository.AuthRepository
import com.hackaton.sustaina.data.repository.CampaignRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Provides
    @Singleton
    fun provideFirebaseAuth(): FirebaseAuth = FirebaseAuth.getInstance()

    @Provides
    @Singleton
    fun provideAuthRepository(firebaseAuth: FirebaseAuthSource): AuthRepository {
        return AuthRepository(firebaseAuth)
    }

    @Provides
    @Singleton
    fun provideIssueRepository(): CampaignRepository {
        return CampaignRepository(CampaignDatabaseSource())
    }
}
