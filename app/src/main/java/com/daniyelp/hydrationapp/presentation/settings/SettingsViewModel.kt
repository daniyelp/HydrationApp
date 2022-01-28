package com.daniyelp.hydrationapp.presentation.settings

import androidx.lifecycle.viewModelScope
import com.daniyelp.hydrationapp.BaseViewModel
import com.daniyelp.hydrationapp.data.model.Container
import com.daniyelp.hydrationapp.data.model.QuantityUnit
import com.daniyelp.hydrationapp.data.repository.PreferencesRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class SettingsViewModel @Inject constructor(
    private val preferencesRepository: PreferencesRepository,
): BaseViewModel<SettingsContract.Event, SettingsContract.State, SettingsContract.Effect>() {

    override fun setInitialState(): SettingsContract.State {
        return SettingsContract.State(
           QuantityUnit.Milliliter,
            2000,
            Container.getContainers()
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
            setState { viewState.value.copy(unit = it) }
        }
    }

    private fun selectDailyGoal() {
        setEffect { SettingsContract.Effect.Navigation.ToDailyGoal }
    }

    private fun selectContainer(containerId: Int) {
        setEffect { SettingsContract.Effect.Navigation.ToContainer(containerId) }
    }

    private fun selectUnit(unit: QuantityUnit) {
        preferencesRepository.editPreferredUnit(viewModelScope, unit)
    }

    private fun navigateUp() {
        setEffect { SettingsContract.Effect.Navigation.Up }
    }
}