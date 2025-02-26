package com.hackaton.sustaina.di

import com.hackaton.sustaina.data.datasource.CampaignDatabaseSource
import com.hackaton.sustaina.data.repository.CampaignRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class) // Makes the dependencies available app-wide
object AppModule {

    @Provides
    @Singleton
    fun provideIssueRepository(): CampaignRepository {
        return CampaignRepository(CampaignDatabaseSource())
    }
}
