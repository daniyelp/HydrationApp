package com.daniyelp.hydrationapp.presentation.home.history

import com.daniyelp.hydrationapp.ViewEvent
import com.daniyelp.hydrationapp.ViewSideEffect
import com.daniyelp.hydrationapp.ViewState
import com.daniyelp.hydrationapp.data.model.DayProgress
import com.daniyelp.hydrationapp.data.model.QuantityUnit

class HistoryContract {
    sealed class Event : ViewEvent

    data class State(
        val unit: QuantityUnit,
        val dayProgressList: List<DayProgress> = emptyList()
    ) : ViewState

    sealed class Effect : ViewSideEffect
}