package com.example.smartstep.core.database.di

import androidx.room.Room
import com.example.smartstep.core.database.StepDatabase
import org.koin.android.ext.koin.androidApplication
import org.koin.dsl.module

val databaseModule = module {
    single {
        Room.databaseBuilder(
            androidApplication(),
            StepDatabase::class.java,
            "steps.db"
        ).build()
    }
    single {
        get<StepDatabase>().stepDao
    }
}
