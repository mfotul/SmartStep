package com.example.smartstep.settings.data

import android.content.Context
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.intPreferencesKey
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import com.example.smartstep.settings.domain.Start
import com.example.smartstep.settings.domain.SettingPreferences
import com.example.smartstep.settings.domain.Profile
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.map

class DataStoreSettings(
    private val context: Context,
) : SettingPreferences {

    companion object {
        private val Context.settingDataStore by preferencesDataStore(
            name = "settings_datastore"
        )
    }

    private val units = stringPreferencesKey("units")
    private val genderKey = stringPreferencesKey("gender")
    private val heightKey = intPreferencesKey("height")
    private val weightKey = intPreferencesKey("weight")
    private val goalKey = intPreferencesKey("goal")
    private val batterySettingKey = booleanPreferencesKey("battery")
    private val loadingKey = booleanPreferencesKey("loading")

    override suspend fun saveProfileSettings(profileSettings: Profile) {
        context.settingDataStore.edit { prefs ->
            prefs[genderKey] = profileSettings.gender
            prefs[heightKey] = profileSettings.height
            prefs[weightKey] = profileSettings.weight
            prefs[units] = profileSettings.units
        }
    }

    override fun observeProfileSettings(): Flow<Profile> {
        return context.settingDataStore
            .data
            .map { prefs ->
                Profile(
                    gender = prefs[genderKey] ?: "",
                    height = prefs[heightKey] ?: Int.MIN_VALUE,
                    weight = prefs[weightKey] ?: Int.MIN_VALUE,
                    units = prefs[units] ?: ""
                )
            }
            .distinctUntilChanged()
    }

    override suspend fun saveGoal(goal: Int) {
        context.settingDataStore.edit { prefs ->
            prefs[goalKey] = goal
        }
    }

    override fun observeGoal(): Flow<Int> {
        return context.settingDataStore
            .data
            .map { prefs ->
                prefs[goalKey] ?: 4000
            }
            .distinctUntilChanged()
    }

    override suspend fun saveBackgroundAccessDisabled(disabled: Boolean) {
        context.settingDataStore.edit { prefs ->
            prefs[batterySettingKey] = disabled
        }
    }

    override fun observeBackgroundAccessDisabled(): Flow<Boolean> {
        return context.settingDataStore
            .data
            .map { prefs ->
                prefs[batterySettingKey] ?: false
            }
            .distinctUntilChanged()
    }

    override fun observeLoading(): Flow<Start> {
        return context.settingDataStore
            .data
            .map { prefs ->
                Start(
                    isLoading = prefs[loadingKey] ?: false,
                    height = prefs[heightKey] ?: Int.MIN_VALUE,
                )
            }
            .distinctUntilChanged()
    }
}