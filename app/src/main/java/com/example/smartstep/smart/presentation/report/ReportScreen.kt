package com.example.smartstep.smart.presentation.report

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.asPaddingValues
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBars
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBars
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.GridItemSpan
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.material3.FabPosition
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.adaptive.currentWindowAdaptiveInfo
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.structuralEqualityPolicy
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Devices.TABLET
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.window.core.layout.WindowSizeClass
import com.example.smartstep.R
import com.example.smartstep.core.presentation.designsystem.theme.SmartStepTheme
import com.example.smartstep.core.presentation.designsystem.theme.borderColor
import com.example.smartstep.core.presentation.designsystem.topappbar.SmartStepTopAppBar
import com.example.smartstep.smart.presentation.preview.PreviewModels
import com.example.smartstep.smart.presentation.report.components.ReportCounterCard
import com.example.smartstep.smart.presentation.report.components.ReportDayItem
import com.example.smartstep.smart.presentation.report.components.ReportFloatingActionButtons
import com.example.smartstep.smart.presentation.report.components.ReportWeekSelector
import org.koin.compose.viewmodel.koinViewModel

@Composable
fun ReportScreenRoot(
    onBackClick: () -> Unit,
    viewModel: ReportViewModel = koinViewModel(),
) {
    val state by viewModel.state.collectAsStateWithLifecycle()

    ReportScreen(
        state = state,
        onAction = {
            when (it) {
                ReportAction.OnBackClick -> {
                    onBackClick()
                }

                else -> viewModel.onAction(it)
            }
        }
    )
}

@Composable
fun ReportScreen(
    state: ReportState,
    onAction: (ReportAction) -> Unit,
    modifier: Modifier = Modifier
) {
    val windowSizeClass = currentWindowAdaptiveInfo().windowSizeClass

    val isTablet =
        windowSizeClass.isWidthAtLeastBreakpoint(WindowSizeClass.WIDTH_DP_MEDIUM_LOWER_BOUND)

    val columns = if (isTablet) 2 else 1

    Scaffold(
        topBar = {
            SmartStepTopAppBar(
                text = stringResource(R.string.report),
                onBackClick = {
                    onAction(ReportAction.OnBackClick)
                }
            )
        },
        floatingActionButton = {
            ReportFloatingActionButtons(
                selectedDataType = state.selectedDataType,
                onDataTypeChange = {
                    onAction(ReportAction.OnDataTypeChange(it))
                },
                modifier = Modifier
                    .padding(horizontal = 16.dp)
                    .widthIn(max = 600.dp)
                    .offset(0.dp, 16.dp)
                    .padding(WindowInsets.navigationBars.asPaddingValues())
            )
        },
        floatingActionButtonPosition = FabPosition.Center,
        containerColor = MaterialTheme.colorScheme.background,
        contentWindowInsets = WindowInsets.statusBars,
        modifier = modifier
    ) { innerPadding ->
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier
                .padding(innerPadding)
                .fillMaxSize()
        ) {
            HorizontalDivider(
                color = MaterialTheme.colorScheme.borderColor,
                thickness = 1.dp
            )
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(16.dp),
                modifier = Modifier
                    .fillMaxHeight()
                    .padding(horizontal = 16.dp)
                    .padding(top = 16.dp)
                    .widthIn(max = 600.dp)
            ) {
                ReportCounterCard(
                    isTablet = isTablet,
                    value = state.cardValue,
                    dataType = state.selectedDataType,
                    dailyAverageValue = state.cardDailyAverageValue
                )
                ReportWeekSelector(
                    week = state.weekTitle,
                    isLeftArrowEnabled = state.isLeftArrowEnabled,
                    isRightArrowEnabled = state.isRightArrowEnabled,
                    onLeftArrowClick = {
                        onAction(ReportAction.OnLeftArrowClick)
                    },
                    onRightArrowClick = {
                        onAction(ReportAction.OnRightArrowClick)
                    }
                )
                LazyVerticalGrid(
                    columns = GridCells.Fixed(columns),
                    verticalArrangement = Arrangement.spacedBy(8.dp),
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                    contentPadding = WindowInsets.navigationBars.asPaddingValues()
                ) {
                    items(items = state.weekData) { dailyData ->
                        ReportDayItem(
                            dailyData = dailyData
                        )
                    }
                    item(
                        span = {
                            GridItemSpan(columns)
                        }
                    ) {
                        Spacer(
                            Modifier.height(80.dp)
                        )
                    }
                }
            }
        }
    }
}

@Preview(widthDp = 1500, heightDp = 700, device = TABLET)
@Composable
private fun ReportScreenPreview() {
    SmartStepTheme {
        ReportScreen(
            state = PreviewModels.reportState,
            onAction = {}
        )
    }
}