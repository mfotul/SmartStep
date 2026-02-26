package com.example.smartstep.smart.presentation.step

sealed interface StepEvent {
    data object OnNavSheetClose : StepEvent
    data object OnProfileScreenClick: StepEvent
    data object OnExitClick: StepEvent
}

