package com.daniyelp.hydrationapp.presentation.units

import com.daniyelp.hydrationapp.BaseViewModel
import com.daniyelp.hydrationapp.data.model.QuantityUnit

class UnitsViewModel
    : BaseViewModel<UnitsContract.Event, UnitsContract.State, UnitsContract.Effect>() {
    override fun setInitialState(): UnitsContract.State {
        return UnitsContract.State(QuantityUnit.getUnits())
    }

    override fun handleEvents(event: UnitsContract.Event) {
        when(event) {
            UnitsContract.Event.NavigateUp -> navigateUp()
            is UnitsContract.Event.SelectUnit -> selectUnit(event.unit)
        }
    }

    private fun selectUnit(unit: QuantityUnit) {

    }

    private fun navigateUp() {
        setEffect { UnitsContract.Effect.Navigation.Up }
    }
}