@file:OptIn(ExperimentalMaterial3Api::class)

package com.example.smartstep.smart.presentation.home

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
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.widthIn
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
import com.example.smartstep.core.presentation.designsystem.theme.SmartStepTheme
import com.example.smartstep.core.presentation.util.ObserveAsEvents
import com.example.smartstep.core.presentation.util.isIgnoringBatteryOptimizations
import com.example.smartstep.smart.presentation.home.HomeAction.OnSetPermission
import com.example.smartstep.smart.presentation.home.components.HomeAdaptiveBackgroundAccess
import com.example.smartstep.smart.presentation.home.components.HomeAdaptiveFirstPermissionDialog
import com.example.smartstep.smart.presentation.home.components.HomeAdaptiveSecondPermissionDialog
import com.example.smartstep.smart.presentation.home.components.HomeAdaptiveStepGoal
import com.example.smartstep.smart.presentation.home.components.HomeBackgroundAccess
import com.example.smartstep.smart.presentation.home.components.HomeCounterCard
import com.example.smartstep.smart.presentation.home.components.HomeDailyAverage
import com.example.smartstep.smart.presentation.home.components.HomeDateDialog
import com.example.smartstep.smart.presentation.home.components.HomeDialog
import com.example.smartstep.smart.presentation.home.components.HomeEditStepsDialog
import com.example.smartstep.smart.presentation.home.components.HomeExit
import com.example.smartstep.smart.presentation.home.components.HomeFirstPermissionDialog
import com.example.smartstep.smart.presentation.home.components.HomeNavigationDrawer
import com.example.smartstep.smart.presentation.home.components.HomeResetSteps
import com.example.smartstep.smart.presentation.home.components.HomeSecondPermissionDialog
import com.example.smartstep.smart.presentation.home.components.HomeStepGoal
import com.example.smartstep.smart.presentation.home.components.HomeTopAppBar
import com.example.smartstep.smart.presentation.home.models.Dialog
import com.example.smartstep.smart.presentation.home.models.Permission
import com.example.smartstep.smart.presentation.models.Units
import com.example.smartstep.app.StepCounterService
import kotlinx.coroutines.launch
import org.koin.compose.viewmodel.koinViewModel

private val STEP_GOAL_ITEMS = (6000 downTo 0 step 1000).toList()

@SuppressLint("BatteryLife")
@Composable
fun HomeScreenRoot(
    onProfileScreenClick: () -> Unit,
    viewModel: HomeViewModel = koinViewModel()
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
            is HomeEvent.OnNavSheetClose -> {}
            is HomeEvent.OnProfileScreenClick -> {
                onProfileScreenClick()
            }

            HomeEvent.OnExitClick -> {
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
                HomeAdaptiveFirstPermissionDialog(
                    windowSizeClass = windowSizeClass
                ) {
                    HomeFirstPermissionDialog(
                        onClick = {
                            motionPermissionResultLauncher.launch(permission)
                        }
                    )
                }
            }

            Permission.SECOND_DENIAL -> {
                HomeAdaptiveSecondPermissionDialog(
                    windowSizeClass = windowSizeClass
                ) {
                    HomeSecondPermissionDialog(
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
            viewModel.onAction(HomeAction.OnBackgroundAccessDisabled)
    }

    if (state.permission == Permission.ALLOWED_REQUIRED_PERMISSIONS) {
        Intent(context, StepCounterService::class.java).also {
            it.action = StepCounterService.ACTION_START
            context.startService(it)
        }

        if (!isIgnoringBatteryOptimizations(context) && !state.isBackgroundAccessEnabled) {
            HomeAdaptiveBackgroundAccess(
                windowSizeClass = windowSizeClass,
                onDismiss = {
                    viewModel.onAction(HomeAction.OnBackgroundAccessDisabled)
                },
                onClose = {
                    viewModel.onAction(HomeAction.OnBackgroundAccessDisabled)
                }
            ) {
                HomeBackgroundAccess(
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

    HomeScreen(
        goal = state.goal,
        steps = state.steps,
        distance = state.distance,
        calories = state.calories,
        activeMinutes = state.activeMinutes,
        isPaused = state.isPaused,
        units = state.units,
        drawerState = drawerState,
        isBackgroundAccessEnabled = state.isBackgroundAccessEnabled,
        onAction = viewModel::onAction
    )

    when (state.dialogVisible) {
        Dialog.GOAL_PICKER -> {
            HomeAdaptiveStepGoal(
                onDismiss = { },
                windowSizeClass = windowSizeClass
            ) {
                HomeStepGoal(
                    items = STEP_GOAL_ITEMS,
                    selected = state.goalSelected,
                    onSelected = { viewModel.onAction(HomeAction.OnStepGoalSelected(it)) },
                    onSaveClick = { viewModel.onAction(HomeAction.OnStepGoalSave) },
                    onCancelClick = { viewModel.onAction(HomeAction.OnDialogOpen(Dialog.NONE)) }
                )
            }
        }

        Dialog.EDIT_STEP_WITH_DATE, Dialog.EDIT_STEPS -> {
            HomeDialog(
                onDismissRequest = {},
            ) {
                HomeEditStepsDialog(
                    date = state.formattedDate,
                    stepsState = viewModel.goalTextFieldState,
                    onDateClick = { viewModel.onAction(HomeAction.OnDialogOpen(Dialog.EDIT_STEP_WITH_DATE)) },
                    onCancelClick = { viewModel.onAction(HomeAction.OnDialogOpen(Dialog.NONE)) },
                    onSaveClick = { viewModel.onAction(HomeAction.OnEditStepsSave) }
                )
            }

            if (state.dialogVisible == Dialog.EDIT_STEP_WITH_DATE) {
                HomeDialog(
                    onDismissRequest = {},
                ) {
                    HomeDateDialog(
                        selectedYear = state.date.year,
                        selectedMonth = state.date.month.value,
                        selectedDay = state.date.dayOfMonth,
                        onCancelClick = { viewModel.onAction(HomeAction.OnDialogOpen(Dialog.EDIT_STEPS)) },
                        onSaveClick = { viewModel.onAction(HomeAction.OnDateSelected(it)) }
                    )
                }
            }
        }

        Dialog.RESET_TODAY_STEPS -> {
            HomeDialog(
                onDismissRequest = { viewModel.onAction(HomeAction.OnDialogOpen(Dialog.NONE)) },
            ) {
                HomeResetSteps(
                    onCancelClick = { viewModel.onAction(HomeAction.OnDialogOpen(Dialog.NONE)) },
                    onSaveClick = { viewModel.onAction(HomeAction.OnResetStepsConfirmClick) }
                )
            }
        }

        Dialog.EXIT -> {
            HomeDialog(
                onDismissRequest = { viewModel.onAction(HomeAction.OnDialogOpen(Dialog.NONE)) },
            ) {
                HomeExit(
                    onClick = {
                        viewModel.onAction(HomeAction.OnExitConfirmClick)
                    }
                )
            }
        }

        Dialog.NONE -> {}
    }
}

@Composable
fun HomeScreen(
    goal: Int,
    steps: Float,
    distance: String,
    calories: String,
    activeMinutes: String,
    isPaused: Boolean,
    units: Units,
    drawerState: DrawerState,
    isBackgroundAccessEnabled: Boolean,
    onAction: (HomeAction) -> Unit,
    modifier: Modifier = Modifier
) {
    val scope = rememberCoroutineScope()

    HomeNavigationDrawer(
        drawerState = drawerState,
        isBackgroundAccessEnabled = isBackgroundAccessEnabled,
        onAction = onAction
    ) {
        Scaffold(
            topBar = {
                HomeTopAppBar(
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
                contentAlignment = Alignment.Center,
                modifier = Modifier
                    .padding(innerPadding)
                    .fillMaxSize()
            ) {
                Column(
                    verticalArrangement = Arrangement.spacedBy(16.dp),
                    modifier = Modifier
                        .padding(16.dp)
                        .widthIn(max = 380.dp)
                ) {
                    HomeCounterCard(
                        steps = steps,
                        goal = goal,
                        distance = distance,
                        calories = calories,
                        activeMinutes = activeMinutes,
                        isPaused = isPaused,
                        units = units,
                        onEditClick = {
                            onAction(HomeAction.OnDialogOpen(Dialog.EDIT_STEPS))
                        },
                        onPauseClick = {
                            onAction(HomeAction.OnPauseClick)
                        },
                    )
                    HomeDailyAverage(
                        dailyAverage = 1000,
                    )
                }
            }
        }
    }
}

@Preview
@Composable
private fun HomeScreenPreview() {
    SmartStepTheme {
        HomeScreen(
            goal = 1000,
            steps = 0f,
            distance = "3.2",
            calories = "3",
            activeMinutes = "2",
            isPaused = false,
            units = Units.KG,
            drawerState = rememberDrawerState(initialValue = DrawerValue.Closed),
            isBackgroundAccessEnabled = false,
            onAction = {}
        )
    }
}