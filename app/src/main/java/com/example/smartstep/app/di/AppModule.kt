package com.example.smartstep.app.di

import com.example.smartstep.app.MainViewModel
import com.example.smartstep.app.SmartStepApplication
import kotlinx.coroutines.CoroutineScope
import org.koin.android.ext.koin.androidApplication
import org.koin.core.module.dsl.viewModelOf
import org.koin.dsl.module

val appModule = module {
    single<CoroutineScope> {
        (androidApplication() as SmartStepApplication).applicationScope
    }

    viewModelOf(::MainViewModel)
}