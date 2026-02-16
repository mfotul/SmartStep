package com.example.smartstep.smart.presentation.home.components

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
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.zIndex
import com.example.smartstep.core.presentation.designsystem.theme.SmartStepTheme
import com.example.smartstep.smart.presentation.home.HomeAction

@Composable
fun HomeNavigationDrawer(
    drawerState: DrawerState,
    isBackgroundAccessEnabled: Boolean,
    onAction: (HomeAction) -> Unit,
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
                ){
                    if (isBackgroundAccessEnabled) {
                        NavigationDrawerItem(
                            label = {
                                Text(
                                    text = "Fix the “Stop Counting Steps” issue",
                                    style = MaterialTheme.typography.headlineLarge,
                                    fontWeight = FontWeight(500),
                                )
                            },
                            selected = false,
                            onClick = { onAction(HomeAction.OnFixStopCountingStepIssueClick) }
                        )
                        HorizontalDivider()
                    }
                    NavigationDrawerItem(
                        label = {
                            Text(
                                text = "Step Goal",
                                style = MaterialTheme.typography.headlineLarge,
                                fontWeight = FontWeight(500),
                            )
                        },
                        selected = false,
                        onClick = { onAction(HomeAction.OnStepGoalClick) }
                    )
                    HorizontalDivider()
                    NavigationDrawerItem(
                        label = {
                            Text(
                                text = "Personal Settings",
                                style = MaterialTheme.typography.headlineLarge,
                                fontWeight = FontWeight(500),
                            )
                        },
                        selected = false,
                        onClick = { onAction(HomeAction.OnPersonalSettingsClick) }
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
                        onClick = { onAction(HomeAction.OnMenuExitClick) }
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
fun HomeNavigationDrawerPreview() {
    SmartStepTheme {
        HomeNavigationDrawer(
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