package com.hackaton.sustaina.di

import android.content.Context
import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.database
import com.hackaton.sustaina.data.auth.AuthDataSource
import com.hackaton.sustaina.data.auth.AuthRepository
import com.hackaton.sustaina.data.campaign.CampaignDataSource
import com.hackaton.sustaina.data.campaign.CampaignRepository
import com.hackaton.sustaina.data.hotspot.HotspotDataSource
import com.hackaton.sustaina.data.hotspot.HotspotRepository
import com.hackaton.sustaina.data.ml.TrashDetectorDataSource
import com.hackaton.sustaina.data.ml.TrashDetectorRepository
import com.hackaton.sustaina.data.solution.SolutionDataSource
import com.hackaton.sustaina.data.solution.SolutionRepository
import com.hackaton.sustaina.data.user.UserDataSource
import com.hackaton.sustaina.data.user.UserRepository
import com.hackaton.sustaina.domain.usecases.JoinCampaignUseCase
import com.hackaton.sustaina.domain.usecases.LeaveCampaignUseCase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
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
    fun provideAuthRepository(firebaseAuth: AuthDataSource): AuthRepository {
        return AuthRepository(firebaseAuth)
    }

    @Provides
    @Singleton
    fun provideFirebaseDatabase() : DatabaseReference = Firebase.database.reference

    @Provides
    @Singleton
    fun provideUserRepository(): UserRepository {
        return UserRepository(UserDataSource(provideFirebaseDatabase()))
    }

    @Provides
    @Singleton
    fun provideCampaignRepository(): CampaignRepository {
        return CampaignRepository(CampaignDataSource(provideFirebaseDatabase()))
    }

    @Provides
    @Singleton
    fun provideHotspotRepository(): HotspotRepository {
        return HotspotRepository(HotspotDataSource(provideFirebaseDatabase()))
    }

    @Provides
    @Singleton
    fun provideSolutionRepository(): SolutionRepository {
        return SolutionRepository(SolutionDataSource(provideFirebaseDatabase()))
    }

    @Provides
    @Singleton
    fun provideJoinCampaignUseCase(
        campaignRepository: CampaignRepository,
        userRepository: UserRepository
    ): JoinCampaignUseCase {
        return JoinCampaignUseCase(campaignRepository, userRepository)
    }

    @Provides
    @Singleton
    fun provideLeaveCampaignUseCase(
        campaignRepository: CampaignRepository,
        userRepository: UserRepository
    ): LeaveCampaignUseCase {
        return LeaveCampaignUseCase(userRepository, campaignRepository)
    }

    @Provides
    @Singleton
    fun provideTrashDetectorDataSource(@ApplicationContext context: Context): TrashDetectorDataSource {
        return TrashDetectorDataSource(context)
    }

    @Provides
    @Singleton
    fun provideTrashDetectorRepository(
        trashDetectorDataSource: TrashDetectorDataSource
    ): TrashDetectorRepository {
        return TrashDetectorRepository(trashDetectorDataSource)
    }
}
