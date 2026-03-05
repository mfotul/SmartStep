package com.example.smartstep.smart.di

import com.example.smartstep.settings.data.DataStoreSettings
import com.example.smartstep.settings.domain.SettingPreferences
import com.example.smartstep.smart.data.step.GeminiAiCoach
import com.example.smartstep.smart.data.step.NetworkConnectivityObserver
import com.example.smartstep.smart.data.step.RoomStepDataSource
import com.example.smartstep.smart.data.step_counter.DefaultStepTrackerManager
import com.example.smartstep.smart.domain.step.AiCoach
import com.example.smartstep.smart.domain.step.ConnectivityObserver
import com.example.smartstep.smart.domain.step.StepDatasource
import com.example.smartstep.smart.domain.step_counter.StepTrackerManager
import com.example.smartstep.smart.presentation.step.StepViewModel
import com.example.smartstep.smart.presentation.profile.ProfileViewModel
import org.koin.core.module.dsl.singleOf
import org.koin.core.module.dsl.viewModelOf
import org.koin.dsl.bind
import org.koin.dsl.module

val smartModule = module {
    singleOf(::DataStoreSettings) bind SettingPreferences::class
    singleOf(::DefaultStepTrackerManager) bind StepTrackerManager::class
    singleOf(::RoomStepDataSource) bind StepDatasource::class
    singleOf(::NetworkConnectivityObserver) bind ConnectivityObserver::class
    singleOf(::GeminiAiCoach) bind AiCoach::class

    viewModelOf(::ProfileViewModel)
    viewModelOf(::StepViewModel)
}