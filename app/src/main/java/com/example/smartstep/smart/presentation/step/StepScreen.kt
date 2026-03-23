@file:OptIn(ExperimentalMaterial3Api::class)

package com.example.smartstep.smart.presentation.step

import android.Manifest
import android.annotation.SuppressLint
import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Build
import android.provider.Settings.ACTION_APPLICATION_DETAILS_SETTINGS
import android.provider.Settings.ACTION_REQUEST_IGNORE_BATTERY_OPTIMIZATIONS
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.DrawerState
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.adaptive.currentWindowAdaptiveInfo
import androidx.compose.material3.rememberDrawerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.core.app.ActivityCompat.shouldShowRequestPermissionRationale
import androidx.core.content.ContextCompat
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.smartstep.app.StepCounterService
import com.example.smartstep.core.presentation.designsystem.theme.SmartStepTheme
import com.example.smartstep.core.presentation.util.ObserveAsEvents
import com.example.smartstep.core.presentation.util.isIgnoringBatteryOptimizations
import com.example.smartstep.smart.presentation.preview.PreviewModels
import com.example.smartstep.smart.presentation.step.StepAction.OnSetPermission
import com.example.smartstep.smart.presentation.step.components.StepAdaptiveBackgroundAccess
import com.example.smartstep.smart.presentation.step.components.StepAdaptiveFirstPermissionDialog
import com.example.smartstep.smart.presentation.step.components.StepAdaptiveSecondPermissionDialog
import com.example.smartstep.smart.presentation.step.components.StepAdaptiveStepGoal
import com.example.smartstep.smart.presentation.step.components.StepAiBlock
import com.example.smartstep.smart.presentation.step.components.StepBackgroundAccess
import com.example.smartstep.smart.presentation.step.components.StepCounterCard
import com.example.smartstep.smart.presentation.step.components.StepDailyAverage
import com.example.smartstep.smart.presentation.step.components.StepDateDialog
import com.example.smartstep.smart.presentation.step.components.StepDialog
import com.example.smartstep.smart.presentation.step.components.StepEditStepsDialog
import com.example.smartstep.smart.presentation.step.components.StepExit
import com.example.smartstep.smart.presentation.step.components.StepFirstPermissionDialog
import com.example.smartstep.smart.presentation.step.components.StepGoal
import com.example.smartstep.smart.presentation.step.components.StepNavigationDrawer
import com.example.smartstep.smart.presentation.step.components.StepResetSteps
import com.example.smartstep.smart.presentation.step.components.StepSecondPermissionDialog
import com.example.smartstep.smart.presentation.step.components.StepTopAppBar
import com.example.smartstep.smart.presentation.step.models.Dialog
import com.example.smartstep.smart.presentation.step.models.Permission
import kotlinx.coroutines.launch
import org.koin.compose.viewmodel.koinViewModel

private val STEP_GOAL_ITEMS = (6000 downTo 0 step 1000).toList()

@SuppressLint("BatteryLife")
@Composable
fun StepScreenRoot(
    onProfileScreenClick: () -> Unit,
    onMoreClick: () -> Unit,
    onReportClick: () -> Unit,
    viewModel: StepViewModel = koinViewModel()
) {
    val state by viewModel.state.collectAsStateWithLifecycle()

    val context = LocalContext.current
    val activity = context as Activity

    val scope = rememberCoroutineScope()

    val drawerState = rememberDrawerState(initialValue = DrawerValue.Closed)
    val windowSizeClass = currentWindowAdaptiveInfo().windowSizeClass

    ObserveAsEvents(viewModel.events) { event ->
        scope.launch {
            drawerState.close()
        }
        when (event) {
            is StepEvent.OnNavSheetClose -> {}
            is StepEvent.OnProfileScreenClick -> {
                onProfileScreenClick()
            }

            StepEvent.OnExitClick -> {
                Intent(context, StepCounterService::class.java).also {
                    it.action = StepCounterService.ACTION_STOP
                    context.startService(it)
                }
                activity.finishAndRemoveTask()
            }
        }
    }

    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
        val permission = arrayOf(
            Manifest.permission.ACTIVITY_RECOGNITION,
            Manifest.permission.POST_NOTIFICATIONS
        )

        val motionPermissionResultLauncher = rememberLauncherForActivityResult(
            ActivityResultContracts.RequestMultiplePermissions()
        )
        { permissions ->
            permissions.forEach { (permission, isGranted) ->
                if (permission == Manifest.permission.ACTIVITY_RECOGNITION)
                    if (!isGranted)
                        if (shouldShowRequestPermissionRationale(activity, permission)) {
                            viewModel.onAction(OnSetPermission(Permission.FIRST_DENIAL))
                        } else {
                            viewModel.onAction(OnSetPermission(Permission.SECOND_DENIAL))
                        }
                    else
                        viewModel.onAction(OnSetPermission(Permission.ALLOWED_REQUIRED_PERMISSIONS))
            }

        }

        val settingsLauncher = rememberLauncherForActivityResult(
            ActivityResultContracts.StartActivityForResult()
        ) {
            if (ContextCompat.checkSelfPermission(
                    context,
                    permission[0]
                ) == PackageManager.PERMISSION_GRANTED
            ) {
                viewModel.onAction(OnSetPermission(Permission.ALLOWED_REQUIRED_PERMISSIONS))
            } else {
                viewModel.onAction(OnSetPermission(Permission.SECOND_DENIAL))
            }
        }

        LaunchedEffect(Unit) {
            motionPermissionResultLauncher.launch(permission)
        }

        when (state.permission) {
            Permission.FIRST_DENIAL -> {
                StepAdaptiveFirstPermissionDialog(
                    windowSizeClass = windowSizeClass
                ) {
                    StepFirstPermissionDialog(
                        onClick = {
                            motionPermissionResultLauncher.launch(permission)
                        }
                    )
                }
            }

            Permission.SECOND_DENIAL -> {
                StepAdaptiveSecondPermissionDialog(
                    windowSizeClass = windowSizeClass
                ) {
                    StepSecondPermissionDialog(
                        onClick = {
                            val intent = Intent(
                                ACTION_APPLICATION_DETAILS_SETTINGS,
                                Uri.fromParts("package", context.packageName, null)
                            )
                            settingsLauncher.launch(intent)
                        }
                    )
                }
            }

            else -> {}
        }
    } else {
        viewModel.onAction(OnSetPermission(Permission.ALLOWED_REQUIRED_PERMISSIONS))
    }

    val ignoringBatteryOptimizationsLauncher = rememberLauncherForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    ) {
        if (isIgnoringBatteryOptimizations(context))
            viewModel.onAction(OnSetPermission(Permission.ALLOWED_ALL))
        else
            viewModel.onAction(StepAction.OnBackgroundAccessDisabled)
    }

    if (state.permission == Permission.ALLOWED_REQUIRED_PERMISSIONS) {
        Intent(context, StepCounterService::class.java).also {
            it.action = StepCounterService.ACTION_START
            context.startService(it)
        }

        if (!isIgnoringBatteryOptimizations(context) && !state.isBackgroundAccessEnabled) {
            StepAdaptiveBackgroundAccess(
                windowSizeClass = windowSizeClass,
                onDismiss = {
                    viewModel.onAction(StepAction.OnBackgroundAccessDisabled)
                },
                onClose = {
                    viewModel.onAction(StepAction.OnBackgroundAccessDisabled)
                }
            ) {
                StepBackgroundAccess(
                    onClick = {
                        val intent = Intent(
                            ACTION_REQUEST_IGNORE_BATTERY_OPTIMIZATIONS,
                            Uri.fromParts("package", context.packageName, null)
                        )
                        ignoringBatteryOptimizationsLauncher.launch(intent)
                    }
                )
            }
        } else {
            viewModel.onAction(OnSetPermission(Permission.ALLOWED_ALL))
        }
    }

    StepScreen(
        state = state,
        drawerState = drawerState,
        onAction = {
            when (it) {
                StepAction.OnMoreClick -> onMoreClick()
                StepAction.OnReportClick -> onReportClick()
                else -> viewModel.onAction(it)
            }
        }
    )

    when (state.dialogVisible) {
        Dialog.GOAL_PICKER -> {
            StepAdaptiveStepGoal(
                onDismiss = { },
                windowSizeClass = windowSizeClass
            ) {
                StepGoal(
                    items = STEP_GOAL_ITEMS,
                    selected = state.goalSelected,
                    onSelected = { viewModel.onAction(StepAction.OnStepGoalSelected(it)) },
                    onSaveClick = { viewModel.onAction(StepAction.OnStepGoalSave) },
                    onCancelClick = { viewModel.onAction(StepAction.OnDialogOpen(Dialog.NONE)) }
                )
            }
        }

        Dialog.EDIT_STEP_WITH_DATE, Dialog.EDIT_STEPS -> {
            StepDialog(
                onDismissRequest = {},
            ) {
                StepEditStepsDialog(
                    date = state.formattedDate,
                    stepsState = viewModel.goalTextFieldState,
                    onDateClick = { viewModel.onAction(StepAction.OnDialogOpen(Dialog.EDIT_STEP_WITH_DATE)) },
                    onCancelClick = { viewModel.onAction(StepAction.OnDialogOpen(Dialog.NONE)) },
                    onSaveClick = { viewModel.onAction(StepAction.OnEditStepsSave) }
                )
            }

            if (state.dialogVisible == Dialog.EDIT_STEP_WITH_DATE) {
                StepDialog(
                    onDismissRequest = {},
                ) {
                    StepDateDialog(
                        selectedYear = state.date.year,
                        selectedMonth = state.date.month.value,
                        selectedDay = state.date.dayOfMonth,
                        onCancelClick = { viewModel.onAction(StepAction.OnDialogOpen(Dialog.EDIT_STEPS)) },
                        onSaveClick = { viewModel.onAction(StepAction.OnDateSelected(it)) }
                    )
                }
            }
        }

        Dialog.RESET_TODAY_STEPS -> {
            StepDialog(
                onDismissRequest = { viewModel.onAction(StepAction.OnDialogOpen(Dialog.NONE)) },
            ) {
                StepResetSteps(
                    onCancelClick = { viewModel.onAction(StepAction.OnDialogOpen(Dialog.NONE)) },
                    onSaveClick = { viewModel.onAction(StepAction.OnResetStepsConfirmClick) }
                )
            }
        }

        Dialog.EXIT -> {
            StepDialog(
                onDismissRequest = { viewModel.onAction(StepAction.OnDialogOpen(Dialog.NONE)) },
            ) {
                StepExit(
                    onClick = {
                        viewModel.onAction(StepAction.OnExitConfirmClick)
                    }
                )
            }
        }

        Dialog.NONE -> {}
    }
}

@Composable
fun StepScreen(
    state: StepState,
    drawerState: DrawerState,
    onAction: (StepAction) -> Unit,
    modifier: Modifier = Modifier
) {
    val scope = rememberCoroutineScope()

    StepNavigationDrawer(
        drawerState = drawerState,
        isBackgroundAccessEnabled = state.isBackgroundAccessEnabled,
        onAction = onAction
    ) {
        Scaffold(
            topBar = {
                StepTopAppBar(
                    onMenuClick = {
                        scope.launch {
                            drawerState.apply {
                                if (isClosed) open() else close()
                            }
                        }
                    }
                )
            },
            containerColor = MaterialTheme.colorScheme.background,
            modifier = modifier
        ) { innerPadding ->
            Box(
                contentAlignment = Alignment.TopCenter,
                modifier = Modifier.fillMaxSize()
            ) {
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.spacedBy(16.dp),
                    modifier = Modifier
                        .verticalScroll(state = rememberScrollState())
                        .fillMaxHeight()
                        .padding(innerPadding)
                        .padding(horizontal = 16.dp)
                        .widthIn(max = 380.dp)
                ) {
                    StepCounterCard(
                        steps = state.steps,
                        goal = state.goal,
                        distance = state.distance,
                        calories = state.calories,
                        activeMinutes = state.activeMinutes,
                        isPaused = state.isPaused,
                        units = state.units,
                        onEditClick = {
                            onAction(StepAction.OnDialogOpen(Dialog.EDIT_STEPS))
                        },
                        onPauseClick = {
                            onAction(StepAction.OnPauseClick)
                        },
                        onReportClick = {
                            onAction(StepAction.OnReportClick)
                        }
                    )
                    StepDailyAverage(
                        weeklyStats = state.weeklyStats,
                        dailyAverage = state.averageDailySteps,
                    )
                    StepAiBlock(
                        isOffline = state.isOffline,
                        aiOutput = state.aiResult,
                        onMoreClick = { onAction(StepAction.OnMoreClick) },
                        onTryAgainClick = { onAction(StepAction.OnTryAgainClick) }
                    )
                }
            }
        }
    }
}

@Preview()
@Composable
private fun StepScreenPreview() {
    SmartStepTheme {
        val previewState = PreviewModels.stepState
        StepScreen(
            state = previewState,
            drawerState = rememberDrawerState(initialValue = DrawerValue.Closed),
            onAction = {}
        )
    }
}