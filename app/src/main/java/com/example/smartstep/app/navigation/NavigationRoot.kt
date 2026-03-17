package com.example.smartstep.app.navigation

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.navigation3.rememberViewModelStoreNavEntryDecorator
import androidx.navigation3.runtime.entryProvider
import androidx.navigation3.runtime.rememberNavBackStack
import androidx.navigation3.runtime.rememberSaveableStateHolderNavEntryDecorator
import androidx.navigation3.ui.NavDisplay
import com.example.smartstep.smart.presentation.chat.ChatScreenRoot
import com.example.smartstep.smart.presentation.step.StepScreenRoot
import com.example.smartstep.smart.presentation.profile.ProfileScreenRoot

@Composable
fun NavigationRoot(
    modifier: Modifier = Modifier,
    homeRoute: NavigationRoute = NavigationRoute.ProfileScreen
) {
    val backStack = rememberNavBackStack(homeRoute)

    NavDisplay(
        backStack = backStack,
        modifier = modifier,
        entryDecorators = listOf(
            rememberSaveableStateHolderNavEntryDecorator(),
            rememberViewModelStoreNavEntryDecorator(),
        ),
        entryProvider = entryProvider {
            entry<NavigationRoute.ProfileScreen> {
                ProfileScreenRoot(
                    onSkip = {
                        backStack.clear()
                        backStack.add(NavigationRoute.StepScreen)
                    }
                )
            }

            entry<NavigationRoute.StepScreen> {
                StepScreenRoot(
                    onProfileScreenClick = {
                        backStack.add(NavigationRoute.ProfileScreen)
                    },
                    onMoreClick = {
                        backStack.add(NavigationRoute.ChatScreen)
                    }
                )
            }

            entry<NavigationRoute.ChatScreen> {
                ChatScreenRoot(
                    onBackClick = {
                        backStack.remove(NavigationRoute.ChatScreen)
                    }
                )
            }
        }
    )
}
