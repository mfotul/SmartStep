package com.example.smartstep.smart.presentation.home.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.smartstep.R
import com.example.smartstep.core.presentation.designsystem.progress_indicator.StepCounterProgressIndicator
import com.example.smartstep.core.presentation.designsystem.theme.SmartStepTheme
import com.example.smartstep.smart.presentation.models.Units
import java.text.NumberFormat
import java.util.Locale

@Composable
fun HomeCounterCard(
    steps: Float,
    goal: Int,
    distance: String,
    calories: String,
    activeMinutes: String,
    isPaused: Boolean,
    units: Units,
    onEditClick: () -> Unit,
    onPauseClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Card(
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.primary
        ),
        elevation = CardDefaults.cardElevation(
            defaultElevation = 4.dp
        ),
        modifier = modifier
            .fillMaxWidth()
    ) {
        Column(
            verticalArrangement = Arrangement.spacedBy(16.dp),
            modifier = Modifier
                .padding(16.dp)
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
            ) {
                HomeIcon(
                    id = R.drawable.sneakers,
                    shape = RoundedCornerShape(8.dp),
                )
                Spacer(modifier = Modifier.weight(1f))
                IconButton(
                    onClick = onEditClick,
                ) {
                    HomeIcon(
                        id = R.drawable.pen_edit_2,
                        shape = CircleShape,
                    )
                }
                IconButton(
                    onClick = onPauseClick,
                ) {
                    HomeIcon(
                        id = if (isPaused) R.drawable.play else R.drawable.pause,
                        shape = CircleShape,
                    )
                }
            }
            Column {
                val usFormatter = NumberFormat.getNumberInstance(Locale.US)
                Text(
                    text = usFormatter.format(steps),
                    style = MaterialTheme.typography.titleLarge,
                    color = if (isPaused)
                        MaterialTheme.colorScheme.surfaceContainerHighest.copy(alpha = 0.2f)
                    else
                        MaterialTheme.colorScheme.surfaceContainerHighest
                )
                Text(
                    text = if (isPaused)
                        stringResource(R.string.paused)
                    else
                        stringResource(R.string.steps, goal),
                    style = MaterialTheme.typography.headlineMedium,
                    color = MaterialTheme.colorScheme.surface
                )
            }
            StepCounterProgressIndicator(
                progress = { (steps / goal).coerceIn(0f, 1f) }
            )
            Spacer(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(4.dp)
            )
            Row(
                horizontalArrangement = Arrangement.SpaceBetween,
                modifier = Modifier
                    .fillMaxWidth()
            ) {
                HomeTextWithIcon(
                    id = R.drawable.pin__location__direction,
                    text = distance,
                    unit = if (units == Units.CM)
                        stringResource(R.string.km)
                    else
                        stringResource(R.string.mi)
                )
                HomeTextWithIcon(
                    id = R.drawable.weight_diet,
                    text = calories,
                    unit = "kcal"
                )
                HomeTextWithIcon(
                    id = R.drawable.time_clock,
                    text = activeMinutes,
                    unit = "min"
                )
            }
        }
    }
}

@Preview
@Composable
private fun HomeCounterCardPreview() {
    SmartStepTheme {
        HomeCounterCard(
            steps = 1000f,
            goal = 2000,
            distance = "1000.0",
            calories = "1000",
            activeMinutes = "1000",
            isPaused = false,
            units = Units.KG,
            onEditClick = {},
            onPauseClick = {}
        )
    }
}
