package com.example.smartstep.app.navigation

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.navigation3.rememberViewModelStoreNavEntryDecorator
import androidx.navigation3.runtime.NavKey
import androidx.navigation3.runtime.entryProvider
import androidx.navigation3.runtime.rememberNavBackStack
import androidx.navigation3.runtime.rememberSaveableStateHolderNavEntryDecorator
import androidx.navigation3.ui.NavDisplay
import com.example.smartstep.smart.presentation.home.HomeScreenRoot
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
                        backStack.add(NavigationRoute.HomeScreen)
                    }
                )
            }

            entry<NavigationRoute.HomeScreen> {
                HomeScreenRoot(
                    onProfileScreenClick = {
                        backStack.add(NavigationRoute.ProfileScreen)
                    }
                )
            }
        }
    )
}
