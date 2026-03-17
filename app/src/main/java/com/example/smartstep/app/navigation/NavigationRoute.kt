package com.example.smartstep.app.navigation

import androidx.navigation3.runtime.NavKey
import kotlinx.serialization.Serializable

@Serializable
sealed interface NavigationRoute: NavKey {
    @Serializable
    data object ChatScreen: NavigationRoute, NavKey

    @Serializable
    data object ProfileScreen: NavigationRoute, NavKey

    @Serializable
    data object StepScreen: NavigationRoute, NavKey
}