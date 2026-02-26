package com.example.smartstep.core.database

import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.smartstep.core.database.step.StepDao
import com.example.smartstep.core.database.step.StepEntity


@Database(
    entities = [StepEntity::class],
    version = 1,
)

abstract class StepDatabase: RoomDatabase() {
    abstract val stepDao: StepDao
}