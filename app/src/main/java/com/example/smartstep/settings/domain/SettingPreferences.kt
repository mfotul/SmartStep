package com.example.smartstep.settings.domain

import kotlinx.coroutines.flow.Flow

interface SettingPreferences {
    suspend fun saveProfileSettings(profileSettings: Profile)
    fun observeProfileSettings(): Flow<Profile>
    suspend fun saveGoal(goal: Int)
    fun observeGoal(): Flow<Int>
    suspend fun saveBackgroundAccessDisabled(disabled: Boolean)
    fun observeBackgroundAccessDisabled(): Flow<Boolean>
    fun observeLoading(): Flow<Start>
}