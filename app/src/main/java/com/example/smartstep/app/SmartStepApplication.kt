package com.example.smartstep.app

import android.app.Application
import com.example.smartstep.app.di.appModule
import com.example.smartstep.smart.di.smartModule
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.GlobalContext.startKoin

class SmartStepApplication: Application() {
    override fun onCreate() {
        super.onCreate()

        startKoin {
            androidContext(this@SmartStepApplication)
            modules(
                appModule,
                smartModule
            )
        }
    }
}