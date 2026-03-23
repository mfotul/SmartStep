package com.example.smartstep.app.di

import com.example.smartstep.app.MainViewModel
import com.example.smartstep.app.SmartStepApplication
import com.example.smartstep.smart.data.step.StepWorker
import kotlinx.coroutines.CoroutineScope
import org.koin.android.ext.koin.androidApplication
import org.koin.androidx.workmanager.dsl.workerOf
import org.koin.core.module.dsl.viewModelOf
import org.koin.dsl.module

val appModule = module {
    single<CoroutineScope> {
        (androidApplication() as SmartStepApplication).applicationScope
    }
    viewModelOf(::MainViewModel)
}