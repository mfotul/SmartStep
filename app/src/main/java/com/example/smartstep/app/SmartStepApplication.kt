package com.example.smartstep.app

import android.app.Application
import android.app.NotificationChannel
import android.app.NotificationManager
import com.example.smartstep.app.di.appModule
import com.example.smartstep.core.database.di.databaseModule
import com.example.smartstep.smart.di.smartModule
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import org.koin.android.ext.koin.androidContext
import org.koin.androidx.workmanager.koin.workManagerFactory
import org.koin.core.context.GlobalContext.startKoin

const val CHANNEL_ID = "step_counter"

class SmartStepApplication : Application() {
    val applicationScope = CoroutineScope(SupervisorJob() + Dispatchers.Default)

    override fun onCreate() {
        super.onCreate()

        val channel = NotificationChannel(
            CHANNEL_ID,
            "Step Counter",
            NotificationManager.IMPORTANCE_LOW
        )
        val notificationManager = getSystemService(NotificationManager::class.java)
        notificationManager.createNotificationChannel(channel)

        startKoin {
            androidContext(this@SmartStepApplication)
            workManagerFactory()
            modules(
                appModule,
                smartModule,
                databaseModule
            )
        }
    }
}