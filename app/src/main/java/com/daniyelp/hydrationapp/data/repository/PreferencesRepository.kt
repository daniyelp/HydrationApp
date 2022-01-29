package com.daniyelp.hydrationapp.data.repository

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.intPreferencesKey
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import com.daniyelp.hydrationapp.data.model.Container
import com.daniyelp.hydrationapp.data.model.Quantity
import com.daniyelp.hydrationapp.data.model.QuantityUnit
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch

private open class PreferencesObject<T>(val key: Preferences.Key<T>, val defaultValue: T) {
    object Unit: PreferencesObject<String>(stringPreferencesKey("UNIT"), QuantityUnit.Milliliter.toString())
    object DailyGoal: PreferencesObject<Int>(intPreferencesKey("DAILY_GOAL"), 2000)
    companion object {
        fun containerPreferencesObject(containerId: Int) =
            PreferencesObject<Int>(intPreferencesKey("CONTAINER_${containerId}"), Container.getContainer(containerId).quantity.getValue(QuantityUnit.Milliliter))
    }
}

class PreferencesRepository(context: Context) {
    private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "settings")
    private val dataStore = context.dataStore

    private fun <T> readPreference(
        scope: CoroutineScope,
        preferencesObject: PreferencesObject<T>,
        onPreferenceUpdated: (T) -> Unit
    ) = dataStore.data
        .map { preferences -> preferences[preferencesObject.key] ?: preferencesObject.defaultValue}
        .onEach { onPreferenceUpdated(it) }
        .launchIn(scope)

    private fun <T> editPreference(
        scope: CoroutineScope,
        preferencesObject: PreferencesObject<T>,
        newValue: T
    ) {
        scope.launch {
            dataStore.edit { preferences ->
                preferences[preferencesObject.key] = newValue
            }
        }
    }

    fun readPreferredUnit(
        scope: CoroutineScope,
        onUnitUpdated: (QuantityUnit) -> Unit
    ) = readPreference(
        scope = scope,
        preferencesObject = PreferencesObject.Unit,
        onPreferenceUpdated = {
            onUnitUpdated(QuantityUnit.fromString(it))
        }
    )

    fun editPreferredUnit(
        scope: CoroutineScope,
        newUnit: QuantityUnit
    ) = editPreference(scope, PreferencesObject.Unit, newUnit.toString())

    fun readDailyGoal(
        scope: CoroutineScope,
        onDailyGoalUpdated: (Quantity) -> Unit
    ) = readPreference(
        scope = scope,
        preferencesObject = PreferencesObject.DailyGoal,
        onPreferenceUpdated = {
            onDailyGoalUpdated(Quantity(it, QuantityUnit.Milliliter))
        }
    )

    fun editDailyGoal(
        scope: CoroutineScope,
        newDailyGoal: Quantity
    ) = editPreference(scope, PreferencesObject.DailyGoal, newDailyGoal.getValue(QuantityUnit.Milliliter))

    fun readContainerQuantity(
        containerId: Int,
        scope: CoroutineScope,
        onContainerQuantityUpdated: (Quantity) -> Unit
    ) = readPreference(
        scope = scope,
        preferencesObject = PreferencesObject.containerPreferencesObject(containerId),
        onPreferenceUpdated = {
            onContainerQuantityUpdated(Quantity(it, QuantityUnit.Milliliter))
        }
    )

    fun editContainerQuantity(
        containerId: Int,
        scope: CoroutineScope,
        newContainerQuantity: Quantity
    ) = editPreference(scope, PreferencesObject.containerPreferencesObject(containerId), newContainerQuantity.getValue(QuantityUnit.Milliliter))
}