package com.daniyelp.hydrationapp.home.today

import com.daniyelp.hydrationapp.ViewEvent
import com.daniyelp.hydrationapp.ViewSideEffect
import com.daniyelp.hydrationapp.ViewState
import com.daniyelp.hydrationapp.data.model.Container
import com.daniyelp.hydrationapp.data.model.QuantityUnit

class TodayContract {
    sealed class Event : ViewEvent {
        object NavigateToSettings: Event()
        data class SelectContainer(val containerId: Int): Event()
    }

    data class State(
        val unit: QuantityUnit,
        val dailyGoal: Int,
        val currentQuantity: Int,
        val containers: List<Container>
    ) : ViewState

    sealed class Effect : ViewSideEffect {}
}