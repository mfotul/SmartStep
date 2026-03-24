package com.example.smartstep.smart.data.step

import com.example.smartstep.core.database.step.StepDao
import com.example.smartstep.smart.domain.step.Step
import com.example.smartstep.smart.domain.step.StepDatasource
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import java.time.Instant

class RoomStepDataSource(
    private val stepDao: StepDao
) : StepDatasource {
    override fun observeFromToSteps(
        from: Instant,
        to: Instant
    ): Flow<List<Step>> {
        return stepDao
            .observeSteps(from.toEpochMilli(), to.toEpochMilli())
            .map { steps ->
                steps.map {
                    it.toStep()
                }
            }
    }

    override fun getStepsByDate(date: Instant): Flow<Step?> {
        return stepDao
            .getStepsByDate(date.toEpochMilli())
            .map { steps ->
                steps?.toStep()
            }
    }

    override suspend fun upsertStep(step: Step) {
        stepDao.upsertStep(step.toStepEntity())
    }
}