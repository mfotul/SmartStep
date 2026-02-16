package com.example.smartstep.app

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.smartstep.settings.domain.SettingPreferences
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.take
import kotlinx.coroutines.flow.update


class MainViewModel(
    private val settingPreferences: SettingPreferences
) : ViewModel() {

    var hasLoadedInitialData = false

    private var _state = MutableStateFlow(MainState())
    val state = _state
        .onStart {
            if (!hasLoadedInitialData) {
                loadInitialData()
                hasLoadedInitialData = true
            }
        }
        .stateIn(
            viewModelScope,
            SharingStarted.WhileSubscribed(5000L),
            MainState()
        )

    private fun loadInitialData() {
        settingPreferences
            .observeLoading()
            .take(1)
            .onEach { pref ->
                _state.update {
                    it.copy(
                        isLoading = pref.isLoading,
                        height = pref.height
                    )
                }
            }
            .launchIn(viewModelScope)
    }
}