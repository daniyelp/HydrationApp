package com.daniyelp.hydrationapp.units

import com.daniyelp.hydrationapp.ViewEvent
import com.daniyelp.hydrationapp.ViewSideEffect
import com.daniyelp.hydrationapp.ViewState
import com.daniyelp.hydrationapp.data.model.QuantityUnit

class UnitsContract {
    sealed class Event : ViewEvent {
        data class SelectUnit(val unit: QuantityUnit): Event()
        object NavigateUp: Event()
    }

    data class State(
        val units: List<QuantityUnit>
    ) : ViewState

    sealed class Effect : ViewSideEffect {
        sealed class Navigation: Effect() {
            object NavigateUp: Navigation()
        }
    }
}