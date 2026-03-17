package com.example.smartstep.app.di

import ai.koog.agents.core.agent.AIAgent
import ai.koog.agents.core.tools.ToolRegistry
import ai.koog.agents.core.tools.ToolRegistry.Companion.invoke
import ai.koog.agents.core.tools.reflect.tool
import ai.koog.prompt.executor.clients.google.GoogleModels
import ai.koog.prompt.executor.llms.all.simpleGoogleAIExecutor
import com.example.smartstep.BuildConfig
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