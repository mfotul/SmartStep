package com.example.smartstep.smart.data.step

import android.content.Context
import androidx.work.CoroutineWorker
import androidx.work.ExistingWorkPolicy
import androidx.work.OneTimeWorkRequestBuilder
import androidx.work.WorkManager
import androidx.work.WorkerParameters
import com.example.smartstep.app.StepCounterService
import com.example.smartstep.core.presentation.util.calculateInitialTimeDelay
import com.example.smartstep.smart.domain.step.Step
import com.example.smartstep.smart.domain.step.StepDatasource
import com.example.smartstep.smart.domain.step_counter.StepTrackerManager
import java.time.Instant
import java.time.temporal.ChronoUnit
import java.util.concurrent.TimeUnit

class StepWorker(
    context: Context,
    params: WorkerParameters,
    private val stepDatasource: StepDatasource,
    private val stepTrackerManager: StepTrackerManager
) : CoroutineWorker(context, params) {

    override suspend fun doWork(): Result {
        val yesterday = Instant.now()
            .minus(1, ChronoUnit.DAYS)
            .truncatedTo(ChronoUnit.DAYS)
        stepDatasource.upsertStep(
            Step(
                steps = stepTrackerManager.data.value.steps,
                date = yesterday,
                dailyGoal = stepTrackerManager.data.value.goal
            )
        )
        stepTrackerManager.reset()

        val nextDelay = calculateInitialTimeDelay()
        val nextRequest = OneTimeWorkRequestBuilder<StepWorker>()
            .setInitialDelay(nextDelay, TimeUnit.MILLISECONDS)
            .build()

        WorkManager.getInstance(applicationContext).enqueueUniqueWork(
            StepCounterService.DAILY_DATABASE_INSERT_TASK,
            ExistingWorkPolicy.REPLACE,
            nextRequest
        )

        return Result.success()
    }
}