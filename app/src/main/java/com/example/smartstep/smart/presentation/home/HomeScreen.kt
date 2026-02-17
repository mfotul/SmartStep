@file:OptIn(ExperimentalMaterial3Api::class)

package com.example.smartstep.smart.presentation.home

import android.Manifest
import android.annotation.SuppressLint
import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.provider.Settings.ACTION_APPLICATION_DETAILS_SETTINGS
import android.provider.Settings.ACTION_REQUEST_IGNORE_BATTERY_OPTIMIZATIONS
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.Box
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
import androidx.compose.runtime.remember
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
import com.example.smartstep.smart.presentation.home.components.HomeDialog
import com.example.smartstep.smart.presentation.home.components.HomeExit
import com.example.smartstep.smart.presentation.home.components.HomeFirstPermissionDialog
import com.example.smartstep.smart.presentation.home.components.HomeNavigationDrawer
import com.example.smartstep.smart.presentation.home.components.HomeSecondPermissionDialog
import com.example.smartstep.smart.presentation.home.components.HomeStepGoal
import com.example.smartstep.smart.presentation.home.components.HomeTopAppBar
import com.example.smartstep.smart.presentation.home.components.StepCounterCard
import com.example.smartstep.smart.presentation.home.models.Permission
import kotlinx.coroutines.launch
import org.koin.compose.viewmodel.koinViewModel
import kotlin.math.roundToInt


@SuppressLint("BatteryLife")
@Composable
fun HomeScreenRoot(
    onProfileScreenClick: () -> Unit,
    viewModel: HomeViewModel = koinViewModel()
) {
    val state by viewModel.state.collectAsStateWithLifecycle()

    val context = LocalContext.current
    val activity = context as Activity
    val permission = Manifest.permission.ACTIVITY_RECOGNITION
    val scope = rememberCoroutineScope()

    val drawerState = rememberDrawerState(initialValue = DrawerValue.Closed)
    val windowSizeClass = currentWindowAdaptiveInfo().windowSizeClass

    val stepGoalItems = remember { (6000 downTo 0 step 1000).toList() }

    val motionPermissionResultLauncher = rememberLauncherForActivityResult(
        ActivityResultContracts.RequestPermission()
    )
    { isGranted ->
        if (!isGranted)
            if (shouldShowRequestPermissionRationale(activity, permission)) {
                viewModel.onAction(OnSetPermission(Permission.FIRST_DENIAL))
            } else {
                viewModel.onAction(OnSetPermission(Permission.SECOND_DENIAL))
            }
        else
            viewModel.onAction(OnSetPermission(Permission.ALLOWED_MOTION_SENSORS))
    }

    val settingsLauncher = rememberLauncherForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    ) {
        if (ContextCompat.checkSelfPermission(
                context,
                permission
            ) == PackageManager.PERMISSION_GRANTED
        ) {
            viewModel.onAction(OnSetPermission(Permission.ALLOWED_MOTION_SENSORS))
        } else {
            viewModel.onAction(OnSetPermission(Permission.SECOND_DENIAL))
        }
    }

    val ignoringBatteryOptimizationsLauncher = rememberLauncherForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    ) {
        if (isIgnoringBatteryOptimizations(context))
            viewModel.onAction(OnSetPermission(Permission.ALLOWED_BOTH))
        else
            viewModel.onAction(HomeAction.OnBackgroundAccessDisabled)
    }

    LaunchedEffect(Unit) {
        motionPermissionResultLauncher.launch(permission)
    }

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
                activity.finishAndRemoveTask()
            }
        }
    }

    HomeScreen(
        goal = state.goal,
        steps = state.steps,
        drawerState = drawerState,
        isBackgroundAccessEnabled = state.isBackgroundAccessEnabled,
        onAction = viewModel::onAction
    )

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

        Permission.ALLOWED_MOTION_SENSORS -> {
            viewModel.onAction(HomeAction.OnStartStepSensor)
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
            }
        }

        else -> {}
    }

    if (state.isGoalPickerVisible)
        HomeAdaptiveStepGoal(
            onDismiss = { },
            windowSizeClass = windowSizeClass
        ) {
            HomeStepGoal(
                items = stepGoalItems,
                selected = state.goalSelected,
                onSelected = { viewModel.onAction(HomeAction.OnStepGoalSelected(it)) },
                onSaveClick = { viewModel.onAction(HomeAction.OnStepGoalSave) },
                onCancelClick = { viewModel.onAction(HomeAction.OnStepGoalCancel) }
            )
        }

    if (state.isExitDialogVisible)
        HomeDialog(
            onDismissRequest = { },
        ) {
            HomeExit(
                onClick = {
                    viewModel.onAction(HomeAction.OnExitConfirmClick)
                }
            )
        }

}

@Composable
fun HomeScreen(
    goal: Int,
    steps: Float,
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
                StepCounterCard(
                    steps = steps.roundToInt(),
                    goal = goal,
                    modifier = Modifier
                        .widthIn(max = 380.dp)
                )
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
            drawerState = rememberDrawerState(initialValue = DrawerValue.Closed),
            isBackgroundAccessEnabled = false,
            onAction = {}
        )
    }
}