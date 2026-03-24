package com.example.smartstep.core.database.step

import androidx.room.Dao
import androidx.room.Query
import androidx.room.Upsert
import kotlinx.coroutines.flow.Flow

@Dao
interface StepDao {
    @Query("SELECT * FROM stepentity ORDER BY date")
    fun observeSteps(): Flow<List<StepEntity>>

    @Query("SELECT * FROM stepentity WHERE date BETWEEN :from AND :to")
    fun observeSteps(from: Long, to: Long): Flow<List<StepEntity>>

    @Query("SELECT * FROM stepentity WHERE date = :date")
    fun getStepsByDate(date: Long): Flow<StepEntity?>

    @Upsert
    suspend fun upsertStep(step: StepEntity)
}