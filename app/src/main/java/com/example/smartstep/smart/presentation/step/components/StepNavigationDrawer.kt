package com.example.smartstep.smart.presentation.step.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.widthIn
import androidx.compose.material3.DrawerState
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalDrawerSheet
import androidx.compose.material3.ModalNavigationDrawer
import androidx.compose.material3.NavigationDrawerItem
import androidx.compose.material3.NavigationDrawerItemDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.rememberDrawerState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.zIndex
import com.example.smartstep.R
import com.example.smartstep.core.presentation.designsystem.theme.SmartStepTheme
import com.example.smartstep.smart.presentation.step.StepAction
import com.example.smartstep.smart.presentation.step.models.Dialog

@Composable
fun StepNavigationDrawer(
    drawerState: DrawerState,
    isBackgroundAccessEnabled: Boolean,
    onAction: (StepAction) -> Unit,
    modifier: Modifier = Modifier,
    content: @Composable () -> Unit = {}
) {
    ModalNavigationDrawer(
        gesturesEnabled = drawerState.isOpen,
        drawerContent = {
            ModalDrawerSheet(
                drawerContainerColor = MaterialTheme.colorScheme.surfaceContainerHigh,
                drawerContentColor = MaterialTheme.colorScheme.onPrimary,
                modifier = Modifier
                    .widthIn(max = 360.dp)
                    .fillMaxWidth(0.85f)
                    .zIndex(1f)
            ) {
                Column(
                    modifier = Modifier.padding(16.dp)
                ) {
                    if (isBackgroundAccessEnabled) {
                        NavigationDrawerItem(
                            label = {
                                Text(
                                    text = stringResource(R.string.fix_the_stop_counting_steps_issue),
                                    style = MaterialTheme.typography.headlineLarge,
                                    fontWeight = FontWeight(500),
                                )
                            },
                            selected = false,
                            onClick = { onAction(StepAction.OnFixStopCountingStepIssueClick) }
                        )
                        HorizontalDivider()
                    }
                    NavigationDrawerItem(
                        label = {
                            Text(
                                text = stringResource(R.string.step_goal),
                                style = MaterialTheme.typography.headlineLarge,
                                fontWeight = FontWeight(500),
                            )
                        },
                        selected = false,
                        onClick = { onAction(StepAction.OnDialogOpen(Dialog.GOAL_PICKER)) }
                    )
                    HorizontalDivider()
                    NavigationDrawerItem(
                        label = {
                            Text(
                                text = stringResource(R.string.personal_settings),
                                style = MaterialTheme.typography.headlineLarge,
                                fontWeight = FontWeight(500),
                            )
                        },
                        selected = false,
                        onClick = { onAction(StepAction.OnPersonalSettingsClick) }
                    )
                    HorizontalDivider()
                    NavigationDrawerItem(
                        label = {
                            Text(
                                text = stringResource(R.string.edit_steps),
                                style = MaterialTheme.typography.headlineLarge,
                                fontWeight = FontWeight(500),
                            )
                        },
                        selected = false,
                        onClick = { onAction(StepAction.OnDialogOpen(Dialog.EDIT_STEPS)) }
                    )
                    HorizontalDivider()
                    NavigationDrawerItem(
                        label = {
                            Text(
                                text = stringResource(R.string.reset_today_s_steps),
                                style = MaterialTheme.typography.headlineLarge,
                                fontWeight = FontWeight(500),
                            )
                        },
                        selected = false,
                        onClick = { onAction(StepAction.OnDialogOpen(Dialog.RESET_TODAY_STEPS)) }
                    )
                    HorizontalDivider()
                    NavigationDrawerItem(
                        label = {
                            Text(
                                text = "Exit",
                                style = MaterialTheme.typography.headlineLarge,
                                fontWeight = FontWeight(500),
                            )
                        },
                        colors = NavigationDrawerItemDefaults.colors(
                            unselectedTextColor = MaterialTheme.colorScheme.primary
                        ),
                        selected = false,
                        onClick = { onAction(StepAction.OnDialogOpen(Dialog.EXIT)) }
                    )
                }
            }
        },
        drawerState = drawerState,
        modifier = modifier,
    ) {
        content()
    }
}

@Preview
@Composable
fun StepNavigationDrawerPreview() {
    SmartStepTheme {
        StepNavigationDrawer(
            drawerState = rememberDrawerState(initialValue = DrawerValue.Open),
            isBackgroundAccessEnabled = true,
            onAction = {}
        ) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(Color(0xFFE0E0E0))
            )
        }
    }
}