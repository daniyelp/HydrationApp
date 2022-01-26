package com.daniyelp.hydrationapp.settings

import com.daniyelp.hydrationapp.ViewEvent
import com.daniyelp.hydrationapp.ViewSideEffect
import com.daniyelp.hydrationapp.ViewState
import com.daniyelp.hydrationapp.data.model.Container
import com.daniyelp.hydrationapp.data.model.QuantityUnit

class SettingsContract {
    sealed class Event : ViewEvent {
        object NavigateUp: Event()
        object SelectUnits: Event()
        object SelectDailyGoal: Event()
        data class SelectContainer(val containerId: Int): Event()
    }

    data class State(
        val unit: QuantityUnit,
        val dailyGoal: Int,
        val containers: List<Container>
    ) : ViewState

    sealed class Effect : ViewSideEffect {
        sealed class Navigation: Effect() {
            object NavigateUp: Navigation()
            object NavigateToUnits: Navigation()
            object NavigateToDailyGoal: Navigation()
            data class NavigateToContainer(val containerId: Int)
        }
    }
}