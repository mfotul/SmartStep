package com.example.smartstep.smart.presentation.home

sealed interface HomeEvent {
    data object OnNavSheetClose : HomeEvent
    data object OnProfileScreenClick: HomeEvent
    data object OnExitClick: HomeEvent
}

