package com.daniyelp.hydrationapp.presentation.settings

import com.daniyelp.hydrationapp.presentation.ViewEvent
import com.daniyelp.hydrationapp.presentation.ViewSideEffect
import com.daniyelp.hydrationapp.presentation.ViewState
import com.daniyelp.hydrationapp.data.model.Container
import com.daniyelp.hydrationapp.data.model.Quantity
import com.daniyelp.hydrationapp.data.model.QuantityUnit

class SettingsContract {
    sealed class Event : ViewEvent {
        object NavigateUp: Event()
        data class SelectUnit(val unit: QuantityUnit): Event()
        object SelectDailyGoal: Event()
        data class SelectContainer(val containerId: Int): Event()
    }

    data class State(
        val unit: QuantityUnit,
        val dailyGoal: Quantity,
        val containers: List<Container> = emptyList()
    ) : ViewState

    sealed class Effect : ViewSideEffect {
        sealed class Navigation: Effect() {
            object Up: Navigation()
            object ToDailyGoal: Navigation()
            data class ToContainer(val containerId: Int): Navigation()
        }
    }
}