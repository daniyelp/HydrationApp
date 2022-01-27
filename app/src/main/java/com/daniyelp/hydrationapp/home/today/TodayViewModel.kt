package com.daniyelp.hydrationapp.home.today

import com.daniyelp.hydrationapp.BaseViewModel
import com.daniyelp.hydrationapp.data.model.Container
import com.daniyelp.hydrationapp.data.model.QuantityUnit
import dagger.hilt.android.lifecycle.HiltViewModel

class TodayViewModel
    : BaseViewModel<TodayContract.Event, TodayContract.State, TodayContract.Effect>() {
    override fun setInitialState(): TodayContract.State {
        return TodayContract.State(
            QuantityUnit.Milliliter,
            2000,
            0,
            Container.getContainers()
        )
    }

    override fun handleEvents(event: TodayContract.Event) {
        when(event) {
            TodayContract.Event.NavigateToSettings -> navigateToSettings()
            is TodayContract.Event.SelectContainer -> selectContainer(event.containerId)
        }
    }

    private fun selectContainer(containerId: Int) {
        val quantityAdded = Container.getContainer(containerId).quantityInMilliliters
        setState { viewState.value.copy(currentQuantity = currentQuantity + quantityAdded) }
    }

    private fun navigateToSettings() {
        setEffect { TodayContract.Effect.Navigation.ToSettings }
    }
}