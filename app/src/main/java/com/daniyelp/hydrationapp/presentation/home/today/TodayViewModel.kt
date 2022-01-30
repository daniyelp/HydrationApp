package com.daniyelp.hydrationapp.presentation.home.today

import androidx.lifecycle.viewModelScope
import com.daniyelp.hydrationapp.BaseViewModel
import com.daniyelp.hydrationapp.data.model.Container
import com.daniyelp.hydrationapp.data.model.Quantity
import com.daniyelp.hydrationapp.data.model.QuantityUnit
import com.daniyelp.hydrationapp.data.model.plus
import com.daniyelp.hydrationapp.data.repository.impl.PreferencesRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class TodayViewModel @Inject constructor(
    private val preferencesRepository: PreferencesRepository,
): BaseViewModel<TodayContract.Event, TodayContract.State, TodayContract.Effect>() {
    override fun setInitialState(): TodayContract.State {
        return TodayContract.State(
            QuantityUnit.Milliliter,
            Quantity(2000, QuantityUnit.Milliliter),
            Quantity(0, QuantityUnit.Milliliter),
            Container.getContainers()
        )
    }

    override fun handleEvents(event: TodayContract.Event) {
        when(event) {
            TodayContract.Event.NavigateToSettings -> navigateToSettings()
            is TodayContract.Event.SelectContainer -> selectContainer(event.containerId)
        }
    }

    init {
        preferencesRepository.readPreferredUnit(viewModelScope) {
            setState { viewState.value.copy(unit = it) }
        }
    }

    private fun selectContainer(containerId: Int) {
        val quantityAdded = Container.getContainer(containerId).quantity
        setState { viewState.value.copy(currentQuantity = currentQuantity + quantityAdded) }
    }

    private fun navigateToSettings() {
        setEffect { TodayContract.Effect.Navigation.ToSettings }
    }
}