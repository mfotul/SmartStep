package com.example.smartstep.smart.di

import com.example.smartstep.settings.data.DataStoreSettings
import com.example.smartstep.settings.domain.SettingPreferences
import com.example.smartstep.smart.presentation.home.HomeViewModel
import com.example.smartstep.smart.presentation.profile.ProfileViewModel
import org.koin.core.module.dsl.singleOf
import org.koin.core.module.dsl.viewModelOf
import org.koin.dsl.bind
import org.koin.dsl.module

val smartModule = module {
    singleOf(::DataStoreSettings) bind SettingPreferences::class

    viewModelOf(::ProfileViewModel)
    viewModelOf(::HomeViewModel)
}