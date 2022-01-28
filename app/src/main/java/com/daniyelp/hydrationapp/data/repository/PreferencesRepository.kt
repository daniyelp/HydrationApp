package com.daniyelp.hydrationapp.data.repository

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import com.daniyelp.hydrationapp.data.model.QuantityUnit
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch

private sealed class PreferencesObject<T>(val key: Preferences.Key<T>, val defaultValue: T) {
    object Unit: PreferencesObject<String>(stringPreferencesKey("UNIT"), QuantityUnit.Milliliter.toString())
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
        unit: QuantityUnit
    ) = editPreference(scope, PreferencesObject.Unit, unit.toString())
}