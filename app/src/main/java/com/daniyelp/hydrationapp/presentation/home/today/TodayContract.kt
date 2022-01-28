package com.daniyelp.hydrationapp.presentation.home.today

import com.daniyelp.hydrationapp.ViewEvent
import com.daniyelp.hydrationapp.ViewSideEffect
import com.daniyelp.hydrationapp.ViewState
import com.daniyelp.hydrationapp.data.model.Container
import com.daniyelp.hydrationapp.data.model.Quantity
import com.daniyelp.hydrationapp.data.model.QuantityUnit

class TodayContract {
    sealed class Event : ViewEvent {
        object NavigateToSettings: Event()
        data class SelectContainer(val containerId: Int): Event()
    }

    data class State(
        val unit: QuantityUnit,
        val dailyGoal: Quantity,
        val currentQuantity: Quantity,
        val containers: List<Container> = Container.getContainers()
    ) : ViewState

    sealed class Effect : ViewSideEffect {
        sealed class Navigation: Effect() {
            object ToSettings: Navigation()
        }
    }
}