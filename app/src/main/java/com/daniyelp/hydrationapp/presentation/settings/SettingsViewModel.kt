package com.daniyelp.hydrationapp.presentation.settings

import androidx.lifecycle.viewModelScope
import com.daniyelp.hydrationapp.presentation.BaseViewModel
import com.daniyelp.hydrationapp.data.model.Quantity
import com.daniyelp.hydrationapp.data.model.QuantityUnit
import com.daniyelp.hydrationapp.data.repository.impl.PreferencesRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class SettingsViewModel @Inject constructor(
    private val preferencesRepository: PreferencesRepository,
): BaseViewModel<SettingsContract.Event, SettingsContract.State, SettingsContract.Effect>() {

    override fun setInitialState(): SettingsContract.State {
        return SettingsContract.State(
            QuantityUnit.Milliliter,
            Quantity(0, QuantityUnit.Milliliter),
        )
    }

    override fun handleEvents(event: SettingsContract.Event) {
        when(event) {
            SettingsContract.Event.NavigateUp -> navigateUp()
            is SettingsContract.Event.SelectUnit -> selectUnit(event.unit)
            SettingsContract.Event.SelectDailyGoal -> selectDailyGoal()
            is SettingsContract.Event.SelectContainer -> selectContainer(event.containerId)
        }
    }

    init {
        preferencesRepository.readPreferredUnit(viewModelScope) {
            setState { copy(unit = it) }
        }
        preferencesRepository.readDailyGoal(viewModelScope) {
            setState { copy(dailyGoal = it) }
        }
        preferencesRepository.readContainers(viewModelScope) {
            setState { copy(containers = it)}
        }
    }

    private fun selectDailyGoal() {
        sendEffect(SettingsContract.Effect.Navigation.ToDailyGoal)
    }

    private fun selectContainer(containerId: Int) {
        sendEffect(SettingsContract.Effect.Navigation.ToContainer(containerId))
    }

    private fun selectUnit(unit: QuantityUnit) {
        preferencesRepository.editPreferredUnit(viewModelScope, unit)
    }

    private fun navigateUp() {
        sendEffect(SettingsContract.Effect.Navigation.Up)
    }
}