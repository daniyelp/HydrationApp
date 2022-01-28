package com.daniyelp.hydrationapp.presentation.home.history

import com.daniyelp.hydrationapp.BaseViewModel

class HistoryViewModel
    : BaseViewModel<HistoryContract.Event, HistoryContract.State, HistoryContract.Effect>() {
    override fun setInitialState(): HistoryContract.State {
        return HistoryContract.State()
    }

    override fun handleEvents(event: HistoryContract.Event) {}
}